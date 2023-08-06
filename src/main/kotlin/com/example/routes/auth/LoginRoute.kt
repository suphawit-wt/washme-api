package com.example.routes.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.MongoDatabaseInterface
import com.example.model.ApiResponse
import com.example.model.SaltedHash
import com.example.model.Users
import com.example.util.Constants
import com.example.util.HashingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.loginRoute(
    mongoDatabase: MongoDatabaseInterface,
    hashingService: HashingService,
) {
    val secret = Constants.SECRET
    val issuer = Constants.ISSUER
    val audience = Constants.AUDIENCE

    post("/login") {
        val request = kotlin.runCatching { call.receiveNullable<Users>() }.getOrNull() ?: kotlin.run {
            call.respond(
                message = ApiResponse(
                    statusCode = 400,
                ),
                status = HttpStatusCode.BadRequest
            )
            return@post
        }

        val user = mongoDatabase.userGetByUsername(request.username)
        if(user == null) {
            call.respond(
                message = ApiResponse(
                    statusCode = 401,
                ),
                status = HttpStatusCode.Unauthorized
            )
            return@post
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if(!isValidPassword) {
            call.respond(
                message = ApiResponse(
                    statusCode = 401,
                ),
                status = HttpStatusCode.Unauthorized
            )
            return@post
        }

        val token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("user_id", user.id.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7)))
            .sign(Algorithm.HMAC256(secret))

        call.respond(
            message = ApiResponse(
                statusCode = 200,
                token = token,
            ),
            status = HttpStatusCode.OK
        )
    }
}