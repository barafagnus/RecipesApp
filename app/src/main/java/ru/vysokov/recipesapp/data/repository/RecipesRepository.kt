package ru.vysokov.recipesapp.data.repository

import kotlinx.serialization.json.Json
import ru.vysokov.recipesapp.data.network.RecipesApi
import ru.vysokov.recipesapp.model.Category
import ru.vysokov.recipesapp.model.Recipe


class RecipesRepository {
    fun getCategories(): List<Category> {
        return try {
            val raw = RecipesApi.getNetworkData("https://recipes.androidsprint.ru/api/category")
            Json.decodeFromString<List<Category>>(raw)
        } catch (e: Exception) {
            emptyList()
        }

    }

    fun getRecipes(id: Int): List<Recipe> {
        return try {
            val raw =
                RecipesApi.getNetworkData("https://recipes.androidsprint.ru/api/category/${id}/recipes")
            Json.decodeFromString<List<Recipe>>(raw)
        } catch (e: Exception) {
            emptyList()
        }
    }
}