package ru.vysokov.recipesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vysokov.recipesapp.data.local.entities.CategoryEntity

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAllCategories(): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCategories(vararg categories: List<CategoryEntity>)
}