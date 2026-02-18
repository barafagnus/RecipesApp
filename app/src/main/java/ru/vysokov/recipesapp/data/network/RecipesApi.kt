package ru.vysokov.recipesapp.data.network

import java.net.HttpURLConnection
import java.net.URL

object RecipesApi {
    fun getNetworkData(url: String): String {
        val url = URL(url)
        val connection = url.openConnection() as HttpURLConnection
        return try {
            connection.inputStream.bufferedReader().use { it.readText() }
        } finally {
            connection.disconnect()
        }
    }
}