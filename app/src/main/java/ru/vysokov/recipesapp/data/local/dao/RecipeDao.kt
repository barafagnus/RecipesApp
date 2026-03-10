package ru.vysokov.recipesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vysokov.recipesapp.data.local.entities.RecipeEntity

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE category_id = :categoryId")
    suspend fun getRecipesByCategory(categoryId: Int?): List<RecipeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRecipes(recipes: List<RecipeEntity>)

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: Int): RecipeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM recipes WHERE is_favorite = 1")
    suspend fun getFavorites(): List<RecipeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorites(recipes: List<RecipeEntity>)

    @Query("UPDATE recipes SET is_favorite = :isFavorite WHERE id = :recipeId")
    suspend fun updateFavoriteStatus(recipeId: Int?, isFavorite: Boolean)

    @Query("SELECT is_favorite FROM recipes WHERE id = :id")
    suspend fun isFavorite(id: Int?): Boolean?

    @Query("SELECT category_id FROM recipes WHERE id = :id")
    suspend fun getCategoryById(id: Int): Int?
}