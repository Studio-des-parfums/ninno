package com.ninno.app.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private const val BASE_URL = "https://backend-ninno-production.up.railway.app"

@Serializable
data class NoteChoiceDto(
    val liked: List<String>,
    val disliked: List<String>
)

@Serializable
data class FormulaRequest(
    val top: NoteChoiceDto,
    val heart: NoteChoiceDto,
    val base: NoteChoiceDto
)

@Serializable
data class FormulaResponse(
    val topNotes: List<String>,
    val heartNotes: List<String>,
    val baseNotes: List<String>,
    val description: String
)

object NinnoHttpClient {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 45_000
            connectTimeoutMillis = 10_000
        }
    }
}

object FormulaApiClient {
    suspend fun generateFormula(request: FormulaRequest): FormulaResponse =
        NinnoHttpClient.client.post("$BASE_URL/formula") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
}
