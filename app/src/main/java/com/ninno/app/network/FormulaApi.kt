package com.ninno.app.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

data class NoteChoiceDto(
    val liked: List<String>,
    val disliked: List<String>
)

data class FormulaRequest(
    val top: NoteChoiceDto,
    val heart: NoteChoiceDto,
    val base: NoteChoiceDto
)

data class FormulaResponse(
    val topNotes: List<String>,
    val heartNotes: List<String>,
    val baseNotes: List<String>,
    val description: String
)

interface FormulaApi {
    @POST("/formula")
    suspend fun generateFormula(@Body request: FormulaRequest): FormulaResponse
}

object FormulaApiClient {
    private const val BASE_URL = "https://backend-ninno-production.up.railway.app/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .writeTimeout(45, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: FormulaApi by lazy { retrofit.create(FormulaApi::class.java) }
}
