package com.example.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import com.example.data.MongoDatabaseInterface
import com.example.routes.auth.authenticateRoute
import com.example.routes.auth.loginRoute
import com.example.routes.auth.registerRoute
import com.example.routes.branch.*
import com.example.routes.users.userInfoRoute
import com.example.routes.users.userUpdateRoute
import com.example.routes.wash.*
import com.example.util.HashingServiceImpl
import org.koin.java.KoinJavaComponent

fun Application.configureRouting() {
    val mongoDatabase: MongoDatabaseInterface by KoinJavaComponent.inject(MongoDatabaseInterface::class.java)
    val hashingService = HashingServiceImpl()

    routing {
        route("/api") {
            loginRoute(mongoDatabase, hashingService)
            registerRoute(mongoDatabase, hashingService)

            authenticate {
                authenticateRoute()

                userInfoRoute(mongoDatabase)
                userUpdateRoute(mongoDatabase, hashingService)

                branchCreateRoute(mongoDatabase)
                branchGetAllRoute(mongoDatabase)
                branchGetByIdRoute(mongoDatabase)
                branchUpdateRoute(mongoDatabase)
                branchDeleteRoute(mongoDatabase)

                washCreateRoute(mongoDatabase)
                washGetAllRoute(mongoDatabase)
                washGetByIdRoute(mongoDatabase)
                washUpdateRoute(mongoDatabase)
                washDeleteRoute(mongoDatabase)
            }

        }
    }
}
