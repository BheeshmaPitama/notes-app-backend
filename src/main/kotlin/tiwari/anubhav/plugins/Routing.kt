package tiwari.anubhav.plugins

import io.ktor.server.routing.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import tiwari.anubhav.data.checkIfUserExists
import tiwari.anubhav.data.collections.User
import tiwari.anubhav.data.registerUser
import tiwari.anubhav.data.requests.AccountRequest
import tiwari.anubhav.data.responses.SimpleResponse

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("This is Anubhav's Note API!")
        }

        route("register"){
            post {
                val request = try{
                    call.receive<AccountRequest>()
                }catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                val userExists = checkIfUserExists(request.email)
                if(!userExists){
                    if(registerUser(User(request.email,request.password))){
                        call.respond(OK,SimpleResponse(true,"Successfully Created Account!"))
                    }else{
                        call.respond(OK,SimpleResponse(false,"Unknown Error Occurred!"))
                    }
                }else{
                    call.respond(OK,SimpleResponse(false,"User With That E-Mail Already Exists!"))
                }
            }
        }
    }
}
