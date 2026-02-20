package ru.vysokov.recipesapp.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object NetworkClient {
    private const val BASE_URL = "https://recipes.androidsprint.ru/api/"
    private val json = Json { ignoreUnknownKeys = true }
    val contentType = "application/json".toMediaType()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    val recipesService: RecipeApiService by lazy {
        retrofit.create(RecipeApiService::class.java)
    }
}