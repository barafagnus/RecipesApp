package ru.vysokov.recipesapp.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.vysokov.recipesapp.data.network.RecipeApiService
import ru.vysokov.recipesapp.model.Category
import ru.vysokov.recipesapp.model.Recipe


class RecipesRepository(
    private val apiService: RecipeApiService
) {

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getRecipeById(recipeId).execute()
                response.body()
            } catch (e: Exception) {
                Log.e("!!!", "Network error on get recipe by id")
                null
            }
        }
    }

    suspend fun getRecipesByIds(recipesIds: Set<Int>): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getRecipesById(recipesIds.joinToString(",")).execute()
                response.body()
            } catch (e: Exception) {
                Log.e("!!!", "Network error on get recipes list")
                null
            }
        }

    }

    suspend fun getCategory(categoryId: Int): Category? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCategory(categoryId).execute()
                response.body()
            } catch (e: Exception) {
                Log.e("!!!", "Network error on get category")
                null
            }
        }
    }

    suspend fun getRecipesByCategory(categoryId: Int?): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getRecipesByCategory(categoryId).execute()
                response.body()
            } catch (e: Exception) {
                Log.e("!!!", "Network error on get recipes list by category")
                null
            }
        }
    }

    suspend fun getCategories(): List<Category>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCategories().execute()
                response.body()
            } catch (e: Exception) {
                Log.e("!!!", "Network error on get categories")
                null
            }
        }
    }
}