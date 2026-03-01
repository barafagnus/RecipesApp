package ru.vysokov.recipesapp.data.local

import android.content.Context
import androidx.room.Room

object DatabaseClient {
    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "database-recipes"
            ).build()
            instance = db
            db
        }
    }
}