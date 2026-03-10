package ru.vysokov.recipesapp.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.vysokov.recipesapp.data.local.dao.CategoryDao
import ru.vysokov.recipesapp.data.local.dao.RecipeDao
import ru.vysokov.recipesapp.data.local.entities.toEntity
import ru.vysokov.recipesapp.data.local.entities.toModel
import ru.vysokov.recipesapp.data.network.RecipeApiService
import ru.vysokov.recipesapp.model.Category
import ru.vysokov.recipesapp.model.Recipe
import javax.inject.Inject


class RecipesRepository @Inject constructor(
    private val apiService: RecipeApiService,
    private val categoryDao: CategoryDao,
    private val recipeDao: RecipeDao
) {
    suspend fun getRecipesFromCacheByCategory(categoryId: Int?): List<Recipe> {
        return withContext(Dispatchers.IO) {
            recipeDao.getRecipesByCategory(categoryId).map { it.toModel() }
        }
    }

    suspend fun saveRecipesToCache(recipes: List<Recipe>, categoryId: Int?) {
        return withContext(Dispatchers.IO) {
            recipeDao.insertAllRecipes(recipes.map { recipe ->
                val isFavorite = recipeDao.isFavorite(recipe.id) ?: false
                val finalCategoryId = categoryId ?: recipeDao.getCategoryById(recipe.id)
                recipe.toEntity(finalCategoryId, isFavorite)
            })
        }
    }

    suspend fun getRecipeFromCache(recipeId: Int): Recipe? {
        return withContext(Dispatchers.IO) {
            recipeDao.getRecipeById(recipeId)?.toModel()
        }
    }

    suspend fun isFavorite(recipeId: Int?): Boolean? {
        return withContext(Dispatchers.IO) {
            recipeDao.isFavorite(recipeId)
        }
    }

    suspend fun saveRecipeToCache(recipe: Recipe, categoryId: Int?) {
        return withContext(Dispatchers.IO) {
            val isFavorite = recipeDao.isFavorite(recipe.id) ?: false
            val finalCategoryId = categoryId ?: recipeDao.getCategoryById(recipe.id)
            recipeDao.insertRecipe(recipe.toEntity(finalCategoryId, isFavorite))
        }
    }

    suspend fun getCategoriesFromCache(): List<Category> {
        return withContext(Dispatchers.IO) {
            categoryDao.getAllCategories().map {
                it.toModel()
            }
        }
    }

    suspend fun saveCategoriesToCache(categories: List<Category>) {
        return withContext(Dispatchers.IO) {
            categoryDao.insertAllCategories(categories.map { it.toEntity() })
        }
    }

    suspend fun getFavoritesFromCache(): List<Recipe> {
        return withContext(Dispatchers.IO) {
            recipeDao.getFavorites().map { it.toModel() }
        }
    }

    suspend fun updateFavoriteInCache(recipeId: Int?, isFavorite: Boolean) {
        return withContext(Dispatchers.IO) {
            recipeDao.updateFavoriteStatus(recipeId, isFavorite)
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