package com.example.routes.wash

import com.example.data.MongoDatabaseInterface
import com.example.model.ApiResponse
import com.example.model.WashingMachine
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.washCreateRoute(
    mongoDatabase: MongoDatabaseInterface,
) {
    post("/wash") {
        val request = kotlin.runCatching { call.receiveNullable<WashingMachine>() }.getOrNull() ?: kotlin.run {
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

        val wash = WashingMachine(
            washNo = request.washNo,
            price = request.price,
            size = request.size,
            time = request.time,
            type = request.type,
            status = request.status,
            branchId = request.branchId
        )
        val wasAcknowledged = mongoDatabase.washingMachineCreate(wash)

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