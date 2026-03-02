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


class RecipesRepository(
    private val apiService: RecipeApiService,
    private val categoryDao: CategoryDao,
    private val recipeDao: RecipeDao
) {
    suspend fun getRecipesFromCache(): List<Recipe> {
        return withContext(Dispatchers.IO) {
            recipeDao.getAllRecipes().map { it.toModel() }
        }
    }

    suspend fun getRecipesFromCacheByCategory(categoryId: Int?): List<Recipe> {
        return withContext(Dispatchers.IO) {
            recipeDao.getRecipesByCategory(categoryId).map { it.toModel() }
        }
    }

    suspend fun saveRecipesToCache(recipes: List<Recipe>, categoryId: Int?, isFavorite: Boolean?) {
        return withContext(Dispatchers.IO) {
            recipeDao.insertAllRecipes(recipes.map { it.toEntity(categoryId, isFavorite) })
        }
    }

    suspend fun getRecipeFromCache(recipeId: Int): Recipe? {
        val entity = recipeDao.getRecipeById(recipeId)
        return entity?.toModel()
    }

    suspend fun saveRecipeToCache(recipe: Recipe, categoryId: Int?, isFavorite: Boolean?) {
        recipeDao.insertRecipe(recipe.toEntity(categoryId, isFavorite))

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

    suspend fun saveFavoritesToCache(recipes: List<Recipe>) {
        recipeDao.insertFavorites(recipes.map { it.toEntity(null, true) })
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