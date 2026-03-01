package ru.vysokov.recipesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.vysokov.recipesapp.data.local.dao.CategoryDao
import ru.vysokov.recipesapp.data.local.entities.CategoryEntity

@Database(entities = [CategoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}