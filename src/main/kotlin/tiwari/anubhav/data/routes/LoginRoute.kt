package tiwari.anubhav.data.routes

import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tiwari.anubhav.data.checkPasswordForEmail
import tiwari.anubhav.data.requests.AccountRequest
import tiwari.anubhav.data.responses.SimpleResponse

fun Route.loginRoute() {
    route("/login") {
        post {
            val request = try {
                call.receive<AccountRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val isPasswordCorrect = checkPasswordForEmail(request.email,request.password)
            if(isPasswordCorrect){
                call.respond(OK,SimpleResponse(true,"You are now logged in!"))
            }else{
                call.respond(OK,SimpleResponse(false,"The E-Mail Or Password Is Incorrect!"))
            }
        }
    }
}