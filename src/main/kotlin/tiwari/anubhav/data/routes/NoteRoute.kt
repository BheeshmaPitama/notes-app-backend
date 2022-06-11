package tiwari.anubhav.data.routes

import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tiwari.anubhav.data.*
import tiwari.anubhav.data.collections.Note
import tiwari.anubhav.data.requests.AddOwnerRequest
import tiwari.anubhav.data.requests.DeleteNoteRequest
import tiwari.anubhav.data.responses.SimpleResponse

fun Route.noteRoutes(){
    route("/getNotes"){
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name
                val notes = getNotesForUser(email)
                call.respond(OK,notes)
            }
        }
    }

    route("/addNote"){
        authenticate {
            post {
                val note = try {
                    call.receive<Note>()
                } catch (e: ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }

                if(saveNote(note)){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/deleteNote"){
        authenticate {
            post {
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<DeleteNoteRequest>()
                }catch (e:ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }

                if(deleteNoteForUser(email,request.id)){
                    call.respond(OK)
                }else{
                    call.respond(Conflict)
                }
            }

        }
    }

    route("/addOwnerToNote"){
        authenticate {
            post {
                val request = try {
                    call.receive<AddOwnerRequest>()
                }catch (e:ContentTransformationException){
                    call.respond(BadRequest)
                    return@post
                }
                if(!checkIfUserExists(request.owner)){
                    call.respond(OK,SimpleResponse(false,"No User With This E-Mail Exists!"))
                    return@post
                }
                if(isOwnerOfNote(request.noteID,request.owner)){
                    call.respond(OK,SimpleResponse(false,"This user is already an owner of this note!"))
                    return@post
                }
                if(addOwnerToNote(request.noteID,request.owner)){
                    call.respond(OK,SimpleResponse(true,"${request.owner} can now see this note!"))
                }else{
                    call.respond(Conflict)
                }
            }
        }
    }
}