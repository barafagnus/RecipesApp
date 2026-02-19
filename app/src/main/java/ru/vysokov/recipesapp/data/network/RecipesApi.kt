package ru.vysokov.recipesapp.data.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

object RecipesApi {
    private val client =
        OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    fun getNetworkData(urlString: String): String {
        val request = Request.Builder().url(urlString).build()

        return client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) ""
            else response.body.string()
        }
    }
}