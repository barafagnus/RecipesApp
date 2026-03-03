package ru.vysokov.recipesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.vysokov.recipesapp.data.local.dao.CategoryDao
import ru.vysokov.recipesapp.data.local.dao.RecipeDao
import ru.vysokov.recipesapp.data.local.entities.CategoryEntity
import ru.vysokov.recipesapp.data.local.entities.RecipeEntity

@Database(entities = [CategoryEntity::class, RecipeEntity::class], version = 4)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun recipesDao(): RecipeDao
}