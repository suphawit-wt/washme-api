package com.example.routes.wash

import com.example.data.MongoDatabaseInterface
import com.example.model.ApiResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.washDeleteRoute(
    mongoDatabase: MongoDatabaseInterface,
) {
    delete("/wash/{washId}") {

        val washId = call.parameters["washId"]
        val washObjectId = ObjectId(washId)
        val wasAcknowledged = mongoDatabase.washingMachineDelete(washObjectId)

        if(!wasAcknowledged)  {
            call.respond(
                message = ApiResponse(
                    statusCode = 409,
                ),
                status = HttpStatusCode.Conflict
            )
            return@delete
        }

        call.respond(
            message = ApiResponse(
                statusCode = 200,
            ),
            status = HttpStatusCode.OK
        )
    }
}