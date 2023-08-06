package com.example.plugins

import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import com.example.module.appModule

fun Application.configureKoin() {
    install(Koin) {
        modules(appModule)
    }
}