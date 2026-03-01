package ru.vysokov.recipesapp.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.vysokov.recipesapp.data.local.dao.CategoryDao
import ru.vysokov.recipesapp.data.local.entities.CategoryEntity
import ru.vysokov.recipesapp.data.network.RecipeApiService
import ru.vysokov.recipesapp.model.Category
import ru.vysokov.recipesapp.model.Recipe


class RecipesRepository(
    private val apiService: RecipeApiService,
    private val categoryDao: CategoryDao,
) {

    suspend fun getCategoriesFromCache(): List<Category> {
        return withContext(Dispatchers.IO) {
            categoryDao.getAllCategories().map { entity ->
                Category(
                    id = entity.id,
                    title = entity.title,
                    description = entity.description,
                    imageUrl = entity.imageUrl
                )
            }
        }
    }

    suspend fun saveCategoriesToCache(categories: List<Category>) {
        return withContext(Dispatchers.IO) {
            categoryDao.insertAllCategories(
                categories.map {
                    CategoryEntity(
                        id = it.id,
                        title = it.title,
                        description = it.description,
                        imageUrl = it.imageUrl
                    )
                }
            )
        }
    }

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