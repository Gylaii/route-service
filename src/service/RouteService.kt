package service

import domain.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import messaging.MessagePublisher
import org.slf4j.LoggerFactory

class RouteService {
    private val logger = LoggerFactory.getLogger(RouteService::class.java)
    private val messagePublisher = MessagePublisher()

    // Временное хранилище для MVP
    private val routes = mutableMapOf<String, Route>()

    init {
        // Добавим тестовый маршрут для демонстрации
        val testRoute =
            Route(
                id = "route-123",
                userId = "user-456",
                startPoint = Point(59.9343, 30.3351),
                endPoint = Point(59.9378, 30.3175),
                waypoints =
                    listOf(Point(59.9355, 30.3255), Point(59.9368, 30.3210)),
                pointsOfInterest =
                    listOf(
                        PointOfInterest(
                            id = "poi-1",
                            name = "Казанский собор",
                            location = Point(59.9344, 30.3242),
                            category = "historic",
                            description =
                                "Один из крупнейших храмов Санкт-Петербурга",
                        )
                    ),
                distance = 1200.0,
                estimatedTime = 20,
                caloriesBurn = 150,
                createdAt = System.currentTimeMillis(),
            )
        routes[testRoute.id] = testRoute
    }

    fun getRouteById(id: String): Route? {
        logger.debug("Getting route by ID: $id")
        return routes[id]
    }

    fun generateRoute(
        userId: String,
        startPoint: Point,
        caloriesBurn: Int,
    ): Route {
        // Создаем простой маршрут (в реальности здесь была бы интеграция с OSM)
        val route =
            Route(
                id = "route-${System.currentTimeMillis()}",
                userId = userId,
                startPoint = startPoint,
                endPoint = Point(startPoint.lat + 0.01, startPoint.lng + 0.01),
                waypoints =
                    listOf(
                        Point(startPoint.lat + 0.005, startPoint.lng + 0.005)
                    ),
                pointsOfInterest =
                    listOf(
                        PointOfInterest(
                            id = "poi-${System.currentTimeMillis()}",
                            name = "Интересное место",
                            location =
                                Point(
                                    startPoint.lat + 0.007,
                                    startPoint.lng + 0.006,
                                ),
                            category = "tourism",
                        )
                    ),
                distance = 1500.0,
                estimatedTime = 25,
                caloriesBurn = caloriesBurn,
                createdAt = System.currentTimeMillis(),
            )

        // Сохраняем маршрут
        routes[route.id] = route

        // Публикуем событие
        messagePublisher.publishEvent(
            "events:route-generated",
            """
            {
                "meta": {
                    "eventId": "event-${System.currentTimeMillis()}",
                    "eventType": "route-generated",
                    "timestamp": ${System.currentTimeMillis()},
                    "source": "route-service"
                },
                "payload": {
                    "routeId": "${route.id}",
                    "userId": "$userId",
                    "caloriesBurn": $caloriesBurn
                }
            }
            """,
        )

        return route
    }

    fun handleMealLoggedEvent(eventData: String) {
        logger.info("Handling meal-logged event: $eventData")
        // В реальном сервисе здесь была бы логика реакции на событие
    }
}
