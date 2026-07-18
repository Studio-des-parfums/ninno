package com.ninno.app.network

import retrofit2.http.GET

data class RemoteAppConfig(
    val backgroundImageUrl: String?,
    val backgroundOtherImageUrl: String?,
    val logoImageUrl: String?
)

data class RemoteNote(
    val id: Int,
    val name: String,
    val type: String,
    val imageUrl: String?,
    val happyImageUrl: String?,
    val sadImageUrl: String?,
    val happyAnimUrl: String?,
    val sadAnimUrl: String?
)

interface ContentApi {
    @GET("/config")
    suspend fun getConfig(): RemoteAppConfig

    @GET("/notes")
    suspend fun getNotes(): List<RemoteNote>
}

object ContentApiClient {
    val api: ContentApi by lazy {
        FormulaApiClient.retrofit.create(ContentApi::class.java)
    }
}
