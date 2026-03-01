package ru.vysokov.recipesapp.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.vysokov.recipesapp.data.local.dao.CategoryDao
import ru.vysokov.recipesapp.data.local.dao.RecipeDao
import ru.vysokov.recipesapp.data.local.entities.CategoryEntity
import ru.vysokov.recipesapp.data.local.entities.RecipeEntity
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
            recipeDao.getAllRecipes().map { entity ->
                Recipe(
                    id = entity.id,
                    title = entity.title,
                    ingredients = entity.ingredients,
                    method = entity.method,
                    imageUrl = entity.imageUrl
                )
            }
        }
    }


    suspend fun saveRecipesToCache(recipes: List<Recipe>) {
        return withContext(Dispatchers.IO) {
            recipeDao.insertAllRecipes(
                recipes.map {
                    RecipeEntity(
                        id = it.id,
                        title = it.title,
                        ingredients = it.ingredients,
                        method = it.method,
                        imageUrl = it.imageUrl
                    )
                }
            )
        }
    }

    suspend fun getRecipeFromCache(recipeId: Int): Recipe? {
        val entity = recipeDao.getRecipeById(recipeId)
        return entity?.let {
            Recipe(
                id = entity.id,
                ingredients = entity.ingredients,
                title = entity.title,
                method = entity.method,
                imageUrl = entity.imageUrl
            )
        }
    }

    suspend fun saveRecipeToCache(recipe: Recipe) {
        recipeDao.insertRecipe(
            RecipeEntity(
                id = recipe.id,
                title = recipe.title,
                ingredients = recipe.ingredients,
                method = recipe.method,
                imageUrl = recipe.imageUrl
            )
        )
    }

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