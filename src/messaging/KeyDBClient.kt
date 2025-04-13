package messaging

import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.RedisPubSubAdapter
import org.slf4j.LoggerFactory
import service.RouteService

class MessagePublisher {
    private val logger = LoggerFactory.getLogger(MessagePublisher::class.java)
    private val client = RedisClient.create("redis://localhost:6379")
    private val connection = client.connect()

    fun publishEvent(channel: String, message: String) {
        try {
            logger.info("Publishing to channel $channel: $message")
            connection.sync().publish(channel, message)
        } catch (e: Exception) {
            logger.error("Error publishing message: ${e.message}", e)
        }
    }

    fun publishResponse(correlationId: String, response: String) {
        publishEvent(
            "responses:all",
            """
            {
                "correlationId": "$correlationId",
                "response": $response
            }
        """,
        )
    }
}

class MessageListener(private val routeService: RouteService) :
    RedisPubSubAdapter<String, String>() {
    private val logger = LoggerFactory.getLogger(MessageListener::class.java)

    override fun message(channel: String, message: String) {
        logger.info("Received message on channel $channel")

        when (channel) {
            "events:meal-logged" -> routeService.handleMealLoggedEvent(message)
        // Добавить другие каналы по мере необходимости
        }
    }
}

fun setupMessageListeners() {
    val logger = LoggerFactory.getLogger(MessagePublisher::class.java)
    val routeService = RouteService()
    val keydbHost = System.getenv("KEYDB_HOST") ?: "localhost"
    val keydbPort = System.getenv("KEYDB_PORT")?.toIntOrNull() ?: 6379
    val client = RedisClient.create("redis://$keydbHost:$keydbPort")
    val pubSubConnection = client.connectPubSub()
    val listener = MessageListener(routeService)

    pubSubConnection.addListener(listener)

    // Подписываемся на события с помощью обычного потока вместо корутины
    Thread {
            try {
                pubSubConnection.sync().subscribe("events:meal-logged")
                logger.info("Subscribed to events:meal-logged channel")
            } catch (e: Exception) {
                logger.error(
                    "Error subscribing to event channel: ${e.message}",
                    e,
                )
            }
        }
        .start()

    // Обработка запросов из очереди с помощью обычного потока вместо корутины
    Thread {
            val connection = client.connect()

            while (true) {
                try {
                    val message =
                        connection.sync().brpop(10, "route-service:requests")
                    if (message != null) {
                        // Здесь обработка запросов из очереди
                        logger.info(
                            "Received request from queue: ${message.value}"
                        )

                        // Извлекаем correlationId для ответа
                        // Упрощенная обработка для MVP
                    }
                } catch (e: Exception) {
                    logger.error(
                        "Error processing queue message: ${e.message}",
                        e,
                    )
                }
            }
        }
        .start()
}
