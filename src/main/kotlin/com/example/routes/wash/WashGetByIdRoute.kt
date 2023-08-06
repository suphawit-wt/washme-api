package com.example.routes.wash

import com.example.data.MongoDatabaseInterface
import com.example.model.ApiResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.washGetByIdRoute(
    mongoDatabase: MongoDatabaseInterface,
) {
    get("/wash/{washId}") {

        val washId = call.parameters["washId"]
        val washObjectId = ObjectId(washId)
        val wash = mongoDatabase.washingMachineGetById(washObjectId)

        if (wash == null){
            call.respond(
                message = ApiResponse(
                    statusCode = 404,
                ),
                status = HttpStatusCode.NotFound
            )
        }

        call.respond(
            message = ApiResponse(
                statusCode = 200,
                wash = wash,
            ),
            status = HttpStatusCode.OK
        )
    }
}