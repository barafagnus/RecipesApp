package ru.vysokov.recipesapp.data.local

import android.content.Context
import androidx.room.Room
import ru.vysokov.recipesapp.core.Database

object DatabaseClient {
    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, Database.DB_NAME
            )
                .fallbackToDestructiveMigration(true)
                .build()
            instance = db
            db
        }
    }
}