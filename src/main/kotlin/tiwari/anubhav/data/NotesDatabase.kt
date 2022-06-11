package tiwari.anubhav.data

import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.setValue
import tiwari.anubhav.data.collections.Note
import tiwari.anubhav.data.collections.User

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("NotesDatabase")
private val users = database.getCollection<User>()
private val notes = database.getCollection<Note>()

suspend fun registerUser(user: User):Boolean{
    return users.insertOne(user).wasAcknowledged()
}

suspend fun checkIfUserExists(email: String):Boolean{
    return users.findOne(User::email eq email) != null
}

suspend fun checkPasswordForEmail(email: String,passwordToCheck:String):Boolean{
    val actualPassword = users.findOne(User::email eq email)?.password ?:return false
    return actualPassword == passwordToCheck
}

suspend fun getNotesForUser(email: String):List<Note>{
    return notes.find(Note::owners contains email).toList()
}

suspend fun saveNote(note:Note):Boolean{
    val noteExists = notes.findOneById(note.id)!=null
    return if(noteExists){
        notes.updateOneById(note.id,note).wasAcknowledged()
    }else{
        notes.insertOne(note).wasAcknowledged()
    }
}

suspend fun deleteNoteForUser(email:String,noteID: String):Boolean{
    val note = notes.findOne(Note::id eq noteID,Note::owners contains email)
    note?.let { currentNote ->
        if (currentNote.owners.size > 1) {
            //Multiple Owners. Remove the current E-Mail from the List.
            val newOwners = currentNote.owners - email
            val updateResult = notes.updateOne(Note::id eq currentNote.id, setValue(Note::owners, newOwners))
            return updateResult.wasAcknowledged()
        }
        return notes.deleteOneById(currentNote.id).wasAcknowledged()
    } ?:return false
}

suspend fun addOwnerToNote(noteID: String,ownerEmail: String):Boolean{
    val owners = notes.findOneById(noteID)?.owners?:return false
    return notes.updateOneById(noteID, setValue(Note::owners,owners+ownerEmail)).wasAcknowledged()
}

suspend fun isOwnerOfNote(noteID:String,ownerEmail:String):Boolean{
    val note = notes.findOneById(noteID)?:return false
    return ownerEmail in note.owners
}
