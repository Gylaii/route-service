import api.configureRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import messaging.setupMessageListeners

fun main() {
    embeddedServer(Netty, port = 8080, watchPaths = listOf("config")) {
            install(ContentNegotiation) { json() }

            configureRouting()

            setupMessageListeners()
        }
        .start(wait = true)
}
