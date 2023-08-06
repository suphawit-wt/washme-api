package com.example.routes.wash

import com.example.data.MongoDatabaseInterface
import com.example.model.ApiResponse
import com.example.model.WashingMachine
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.washUpdateRoute(
    mongoDatabase: MongoDatabaseInterface,
) {
    put("/wash/{washId}") {
        val request = kotlin.runCatching { call.receiveNullable<WashingMachine>() }.getOrNull() ?: kotlin.run {
            call.respond(
                message = ApiResponse(
                    statusCode = 400,
                ),
                status = HttpStatusCode.BadRequest
            )
            return@put
        }

        val washId = call.parameters["washId"]
        val washObjectId = ObjectId(washId)
        val wash = WashingMachine(
            washNo = request.washNo,
            price = request.price,
            size = request.size,
            time = request.time,
            type = request.type,
            status = request.status,
            branchId = request.branchId
        )
        val wasAcknowledged = mongoDatabase.washingMachineUpdate(washObjectId, wash)

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