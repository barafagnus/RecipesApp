package ru.vysokov.recipesapp

import android.app.Application
import ru.vysokov.recipesapp.di.AppContainer

class RecipesApplication: Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(this)
    }
}