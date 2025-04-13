package api

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import service.RouteService

fun Application.configureRouting() {
    val logger = LoggerFactory.getLogger(RouteService::class.java)
    val routeService = RouteService()

    routing {
        get("/health") { call.respondText("Route Service is OK") }

        get("/routes/{id}") {
            val id =
                call.parameters["id"]
                    ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        "Missing id",
                    )

            routeService.getRouteById(id)?.let { call.respond(it) }
                ?: call.respond(HttpStatusCode.NotFound, "Route not found")
        }
    }
}
