package com.example.routes.users

import com.example.data.MongoDatabaseInterface
import com.example.model.*
import com.example.util.HashingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.userUpdateRoute(
    mongoDatabase: MongoDatabaseInterface,
    hashingService: HashingService,
) {
    put("/user/update") {
        val request = kotlin.runCatching { call.receiveNullable<Users>() }.getOrNull() ?: kotlin.run {
            call.respond(
                message = ApiResponse(
                    statusCode = 400,
                ),
                status = HttpStatusCode.BadRequest
            )
            return@put
        }

        val principal = call.principal<JWTPrincipal>()
        val userIdFromToken = principal!!.payload.getClaim("user_id").asString()
        val userObjectId = ObjectId(userIdFromToken)

        val UserOldInfo = mongoDatabase.userGetById(userObjectId)
        val isUsernameExist = mongoDatabase.userGetByUsername(request.username)
        val isEmailExist = mongoDatabase.userGetByEmail(request.email)

        if (UserOldInfo != null) {
            if (UserOldInfo.username != request.username){
                if (isUsernameExist != null){
                    call.respond(
                        message = ApiResponse(
                            statusCode = 409,
                        ),
                        status = HttpStatusCode.Conflict
                    )
                    return@put
                }
            }
            if (UserOldInfo.email != request.email){
                if (isEmailExist != null){
                    call.respond(
                        message = ApiResponse(
                            statusCode = 409,
                        ),
                        status = HttpStatusCode.Conflict
                    )
                    return@put
                }
            }
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = Users(
            username = request.username,
            email = request.email,
            fullname = request.fullname,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )
        val wasAcknowledged = mongoDatabase.userUpdate(userObjectId,user)

        if(!wasAcknowledged)  {
            call.respond(
                message = ApiResponse(
                    statusCode = 409,
                ),
                status = HttpStatusCode.Conflict
            )
            return@put
        }

        call.respond(
            message = ApiResponse(
                statusCode = 200,
            ),
            status = HttpStatusCode.OK
        )
    }
}