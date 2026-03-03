package ru.vysokov.recipesapp.di

import android.app.Application
import android.content.Context
import ru.vysokov.recipesapp.data.local.DatabaseClient
import ru.vysokov.recipesapp.data.network.NetworkClient
import ru.vysokov.recipesapp.data.repository.RecipesRepository

class AppContainer(context: Context) {
    private val application = context.applicationContext as Application
    private val databaseClient by lazy { DatabaseClient.getInstance(context) }

    val repository = RecipesRepository(
        NetworkClient.recipesService,
        databaseClient.categoryDao(),
        databaseClient.recipesDao()
    )

    val categoriesListViewModelFactory = CategoriesListViewModelFactory(repository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(repository, application)
    val recipesListViewModelFactory = RecipesListViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(repository, application)
}