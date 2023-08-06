package com.example.routes.auth

import com.example.data.MongoDatabaseInterface
import com.example.model.ApiResponse
import com.example.model.Users
import com.example.util.HashingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerRoute(
    mongoDatabase: MongoDatabaseInterface,
    hashingService: HashingService,
) {
    post("/register") {
        val request = kotlin.runCatching { call.receiveNullable<Users>() }.getOrNull() ?: kotlin.run {
            call.respond(
                message = ApiResponse(
                    statusCode = 400,
                ),
                status = HttpStatusCode.BadRequest
            )
            return@post
        }

        val isUsernameExist = mongoDatabase.userGetByUsername(request.username)
        val isEmailExist = mongoDatabase.userGetByEmail(request.email)

        if (isUsernameExist != null || isEmailExist != null){
            call.respond(
                message = ApiResponse(
                    statusCode = 409,
                ),
                status = HttpStatusCode.Conflict
            )
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = Users(
            username = request.username,
            email = request.email,
            fullname = request.fullname,
            password = saltedHash.hash,
            salt = saltedHash.salt,
            isAdmin = request.isAdmin
        )
        val wasAcknowledged = mongoDatabase.userCreate(user)

        if(!wasAcknowledged)  {
            call.respond(
                message = ApiResponse(
                    statusCode = 409,
                ),
                status = HttpStatusCode.Conflict
            )
            return@post
        }

        call.respond(
            message = ApiResponse(
                statusCode = 201,
            ),
            status = HttpStatusCode.Created
        )

    }
}