package tiwari.anubhav.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tiwari.anubhav.data.routes.loginRoute
import tiwari.anubhav.data.routes.registerRoute

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("This is Anubhav's Note API!")
        }

        registerRoute()
        loginRoute()

    }
}
