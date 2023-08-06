package com.example.routes.wash

import com.example.data.MongoDatabaseInterface
import com.example.model.ApiResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.washGetAllRoute(
    mongoDatabase: MongoDatabaseInterface,
) {
    get("/wash") {

        val washList = mongoDatabase.washingMachineGetAll()

        call.respond(
            message = ApiResponse(
                statusCode = 200,
                washList = washList,
            ),
            status = HttpStatusCode.OK
        )
    }
}