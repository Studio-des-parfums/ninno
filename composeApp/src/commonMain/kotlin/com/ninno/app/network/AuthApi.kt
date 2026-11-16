package com.ninno.app.network

import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable

@Serializable
data class ChildDto(
    val id: Int,
    val firstName: String,
    /** "girl" / "boy", null pour un enfant inscrit avant l'ajout du genre. */
    val gender: String? = null,
    val age: Int? = null
)

@Serializable
data class ParentDto(val id: Int, val email: String, val children: List<ChildDto> = emptyList())

@Serializable
data class AuthResponse(val token: String, val parent: ParentDto)

@Serializable
private data class SerialCheckRequest(val serial: String)

@Serializable
private data class SerialCheckResponse(val status: String)

@Serializable
private data class RegisterRequest(
    val serial: String,
    val email: String,
    val password: String,
    val childFirstName: String,
    val childGender: String,
    val childAge: Int
)

@Serializable
private data class LoginRequest(val email: String, val password: String)

@Serializable
private data class AddChildRequest(val serial: String, val childFirstName: String, val childGender: String, val childAge: Int)

@Serializable
private data class ErrorResponse(val error: String = "")

enum class SerialStatus { AVAILABLE, USED, UNKNOWN }

/** Erreur renvoyée par le backend (message lisible déjà en français). */
class ApiException(val status: Int, message: String) : Exception(message)

object AuthApiClient {
    private val http get() = NinnoHttpClient.client

    private suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T {
        if (!status.isSuccess()) {
            val message = runCatching { body<ErrorResponse>().error }.getOrNull()?.takeIf { it.isNotBlank() }
            throw ApiException(status.value, message ?: "Erreur ${status.value}")
        }
        return body()
    }

    suspend fun checkSerial(serial: String): SerialStatus {
        val res = http.post("$BASE_URL/serials/check") {
            contentType(ContentType.Application.Json)
            setBody(SerialCheckRequest(serial))
        }.bodyOrThrow<SerialCheckResponse>()
        return when (res.status) {
            "available" -> SerialStatus.AVAILABLE
            "used" -> SerialStatus.USED
            else -> SerialStatus.UNKNOWN
        }
    }

    suspend fun register(serial: String, email: String, password: String, childFirstName: String, childGender: String, childAge: Int): AuthResponse =
        http.post("$BASE_URL/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(serial, email, password, childFirstName, childGender, childAge))
        }.bodyOrThrow()

    suspend fun login(email: String, password: String): AuthResponse =
        http.post("$BASE_URL/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }.bodyOrThrow()

    suspend fun me(token: String): ParentDto =
        http.get("$BASE_URL/me") { bearerAuth(token) }.bodyOrThrow()

    suspend fun addChild(token: String, serial: String, childFirstName: String, childGender: String, childAge: Int): ChildDto =
        http.post("$BASE_URL/children") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(AddChildRequest(serial, childFirstName, childGender, childAge))
        }.bodyOrThrow()
}
