package com.example.routes.users

import com.example.data.MongoDatabaseInterface
import com.example.model.ApiResponse
import com.example.model.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.userInfoRoute(
    mongoDatabase: MongoDatabaseInterface
) {
    get("/user/info") {
        val principal = call.principal<JWTPrincipal>()
        val userIdFromToken = principal!!.payload.getClaim("user_id").asString()
        val userObjectId = ObjectId(userIdFromToken)
        val user = mongoDatabase.userGetById(userObjectId)

        if (user != null) {
            call.respond(
                message = ApiResponse(
                    statusCode = 200,
                    user = Users(
                        username = user.username,
                        email = user.email,
                        fullname = user.fullname
                    )
                ),
                status = HttpStatusCode.OK
            )
        }
        else {
            call.respond(
                message = ApiResponse(
                    statusCode = 404,
                ),
                status = HttpStatusCode.NotFound
            )
        }
    }
}