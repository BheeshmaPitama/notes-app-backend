package tiwari.anubhav.data

import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
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
