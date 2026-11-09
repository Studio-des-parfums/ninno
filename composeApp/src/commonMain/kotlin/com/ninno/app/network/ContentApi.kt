package com.ninno.app.network

import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable


@Serializable
data class RemoteAppConfig(
    val backgroundImageUrl: String? = null,
    val backgroundOtherImageUrl: String? = null,
    val logoImageUrl: String? = null
)

@Serializable
data class RemoteNote(
    val id: Int,
    val name: String,
    val type: String,
    val imageUrl: String? = null,
    val happyImageUrl: String? = null,
    val sadImageUrl: String? = null,
    val happyAnimUrl: String? = null,
    val sadAnimUrl: String? = null
)

object ContentApiClient {
    suspend fun getConfig(): RemoteAppConfig =
        NinnoHttpClient.client.get("$BASE_URL/config").body()

    suspend fun getNotes(): List<RemoteNote> =
        NinnoHttpClient.client.get("$BASE_URL/notes").body()
}
