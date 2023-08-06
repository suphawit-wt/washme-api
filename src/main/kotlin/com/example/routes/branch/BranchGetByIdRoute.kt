package com.example.routes.branch

import com.example.data.MongoDatabaseInterface
import com.example.model.ApiResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.branchGetByIdRoute(
    mongoDatabase: MongoDatabaseInterface,
) {
    get("/branch/{branchId}") {

        val branchId = call.parameters["branchId"]
        val branchObjectId = ObjectId(branchId)
        val branch = mongoDatabase.branchGetById(branchObjectId)

        if (branch == null){
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
                branch = branch,
            ),
            status = HttpStatusCode.OK
        )
    }
}