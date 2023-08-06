package com.example.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.ApiResponse
import com.example.util.Constants
import io.ktor.http.*
import io.ktor.server.sessions.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureSecurity() {

    val secret = Constants.SECRET
    val issuer = Constants.ISSUER
    val audience = Constants.AUDIENCE
    val myrealm = Constants.REALM
    
    authentication {
            jwt {
                realm = myrealm
                verifier(JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build())
                validate { credential ->
                    if (credential.payload.audience.contains(audience)) {
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
                }
                challenge { defaultScheme, realm ->
                    call.respond(
                        message = ApiResponse(
                            statusCode = 401,
                        ),
                        status = HttpStatusCode.Unauthorized
                    )
                }
            }
        }

}
