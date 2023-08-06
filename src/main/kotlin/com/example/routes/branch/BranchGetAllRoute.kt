package com.example.routes.branch

import com.example.data.MongoDatabaseInterface
import com.example.model.ApiResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.branchGetAllRoute(
    mongoDatabase: MongoDatabaseInterface,
) {
    get("/branch") {

        val branchList = mongoDatabase.branchGetAll()

        call.respond(
            message = ApiResponse(
                statusCode = 200,
                branchList = branchList,
            ),
            status = HttpStatusCode.OK
        )
    }
}