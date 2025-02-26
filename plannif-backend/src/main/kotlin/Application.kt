package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080) {
        routing {
            get("/") {
                call.respondRedirect("/tasks")
            }
            get("/tasks") {
                val fakeTasks = listOf(
                    mapOf("id" to 1, "title" to "Acheter du lait"),
                    mapOf("id" to 2, "title" to "Appeler Jean")
                )
                call.respond(fakeTasks)
            }
        }
    }.start(wait = true)
}