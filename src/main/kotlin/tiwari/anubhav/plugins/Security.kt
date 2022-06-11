package tiwari.anubhav.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import tiwari.anubhav.data.checkPasswordForEmail

fun Application.configureSecurity() {
    install(Authentication){
        configureAuth()
    }
}

private fun AuthenticationConfig.configureAuth(){
    basic {
        realm="Note Server"
        validate {credentials->
            val email = credentials.name
            val password = credentials.password
            if(checkPasswordForEmail(email,password)){
                UserIdPrincipal(email)
            }else null
        }
    }
}
