package com.example.routes.branch

import com.example.data.MongoDatabaseInterface
import com.example.model.ApiResponse
import com.example.model.Branch
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.branchCreateRoute(
    mongoDatabase: MongoDatabaseInterface,
) {
    post("/branch") {
        val request = kotlin.runCatching { call.receiveNullable<Branch>() }.getOrNull() ?: kotlin.run {
            call.respond(
                message = ApiResponse(
                    statusCode = 400,
                ),
                status = HttpStatusCode.BadRequest
            )
            return@post
        }

        val principal = call.principal<JWTPrincipal>()
        val userIdFromToken = principal!!.payload.getClaim("user_id").asString()
        val userObjectId = ObjectId(userIdFromToken)

        val user = mongoDatabase.userGetById(userObjectId)

        if (user!!.isAdmin == false) {
            call.respond(
                message = ApiResponse(
                    statusCode = 403,
                ),
                status = HttpStatusCode.Forbidden
            )
            return@post
        }

        val branch = Branch(
            branchName = request.branchName
        )
        val wasAcknowledged = mongoDatabase.branchCreate(branch)

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