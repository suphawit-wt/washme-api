package com.example.routes.branch

import com.example.data.MongoDatabaseInterface
import com.example.model.ApiResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.branchDeleteRoute(
    mongoDatabase: MongoDatabaseInterface,
) {
    delete("/branch/{branchId}") {

        val branchId = call.parameters["branchId"]
        val branchObjectId = ObjectId(branchId)
        val wasAcknowledged = mongoDatabase.branchDelete(branchObjectId)

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