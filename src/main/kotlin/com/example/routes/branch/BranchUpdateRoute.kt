package com.example.routes.branch

import com.example.data.MongoDatabaseInterface
import com.example.model.ApiResponse
import com.example.model.Branch
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.branchUpdateRoute(
    mongoDatabase: MongoDatabaseInterface,
) {
    put("/branch/{branchId}") {
        val request = kotlin.runCatching { call.receiveNullable<Branch>() }.getOrNull() ?: kotlin.run {
            call.respond(
                message = ApiResponse(
                    statusCode = 400,
                ),
                status = HttpStatusCode.BadRequest
            )
            return@put
        }

        val branchId = call.parameters["branchId"]
        val branchObjectId = ObjectId(branchId)
        val branch = Branch(
            branchName = request.branchName
        )
        val wasAcknowledged = mongoDatabase.branchUpdate(branchObjectId, branch)

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