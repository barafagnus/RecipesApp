package ru.vysokov.recipesapp.data.repository

import android.util.Log
import ru.vysokov.recipesapp.data.network.RecipeApiService
import ru.vysokov.recipesapp.model.Category
import ru.vysokov.recipesapp.model.Recipe
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object AppExecutor {
    val io: ExecutorService = Executors.newFixedThreadPool(10)
}

class RecipesRepository(
    private val apiService: RecipeApiService
) {

    private val io: ExecutorService = AppExecutor.io

    fun getRecipeById(recipeId: Int, onComplete: (Recipe?) -> Unit) {
        io.execute {
            try {
                val response = apiService.getRecipeById(recipeId).execute()

                if (response.isSuccessful) onComplete(response.body())
                else onComplete(null)
            } catch (e: Exception) {
                Log.e("!!!", "Network error on get recipe")
                onComplete(null)
            }
        }
    }

    fun getRecipesByIds(recipesIds: Set<Int>, onComplete: (List<Recipe>?) -> Unit) {
        io.execute {
            try {
                val response = apiService.getRecipesById(recipesIds).execute()
                if (response.isSuccessful) onComplete(response.body())
                else onComplete(null)
            } catch (e: Exception) {
                Log.e("!!!", "Network error on get recipes list")
                onComplete(null)
            }
        }
    }

    fun getCategory(categoryId: Int, onComplete: (Category?) -> Unit) {
        io.execute {
            try {
                val response = apiService.getCategory(categoryId).execute()
                if (response.isSuccessful) onComplete(response.body())
                else onComplete(null)
            } catch (e: Exception) {
                Log.e("!!!", "Network error on get category")
                onComplete(null)
            }
        }
    }

    fun getRecipesByCategory(categoryId: Int?, onComplete: (List<Recipe>?) -> Unit) {
        io.execute {
            try {
                val response = apiService.getRecipesByCategory(categoryId).execute()
                if (response.isSuccessful) onComplete(response.body())
                else onComplete(null)
            } catch (e: Exception) {
                Log.e("!!!", "Network error on get recipes list by category")
                onComplete(null)
            }
        }
    }

    fun getCategories(onComplete: (List<Category>?) -> Unit) {
        io.execute {
            try {
                val response = apiService.getCategories().execute()
                if (response.isSuccessful) onComplete(response.body())
                else onComplete(null)
            } catch (e: Exception) {
                Log.e("!!!", "Network error on get categories")
                onComplete(null)
            }
        }
    }
}