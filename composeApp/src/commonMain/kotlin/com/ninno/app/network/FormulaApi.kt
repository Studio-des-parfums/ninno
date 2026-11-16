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
data class FormulaNoteDto(
    val name: String,
    /** "top", "heart" ou "base" */
    val type: String,
    /** Quantité en mL pour le flacon configuré. */
    val ml: Double
)

@Serializable
data class FormulaResponse(
    /** Volume du flacon (mL), réglé depuis sdp-dashboard. */
    val volumeMl: Int,
    val notes: List<FormulaNoteDto>,
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
