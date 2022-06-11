package tiwari.anubhav.data.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tiwari.anubhav.data.checkIfUserExists
import tiwari.anubhav.data.collections.User
import tiwari.anubhav.data.registerUser
import tiwari.anubhav.data.requests.AccountRequest
import tiwari.anubhav.data.responses.SimpleResponse

fun Route.registerRoute(){
    route("/register"){
        post {
            val request = try{
                call.receive<AccountRequest>()
            }catch (e: ContentTransformationException){
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val userExists = checkIfUserExists(request.email)
            if(!userExists){
                if(registerUser(User(request.email,request.password))){
                    call.respond(HttpStatusCode.OK, SimpleResponse(true,"Successfully Created Account!"))
                }else{
                    call.respond(HttpStatusCode.OK, SimpleResponse(false,"Unknown Error Occurred!"))
                }
            }else{
                call.respond(HttpStatusCode.OK, SimpleResponse(false,"User With That E-Mail Already Exists!"))
            }
        }
    }
}