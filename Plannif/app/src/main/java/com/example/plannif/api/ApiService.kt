package com.example.plannif.api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ApiService(private val client: HttpClient) {
    suspend fun getTasks(): List<Map<String, Any>> {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/tasks")
        val jsonResponse = response.bodyAsText()
        return Json.decodeFromString(jsonResponse)
    }
}