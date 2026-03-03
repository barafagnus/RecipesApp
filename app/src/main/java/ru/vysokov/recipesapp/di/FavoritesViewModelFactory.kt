package ru.vysokov.recipesapp.di

import android.app.Application
import ru.vysokov.recipesapp.data.repository.RecipesRepository
import ru.vysokov.recipesapp.ui.recipes.favorites.FavoritesViewModel

class FavoritesViewModelFactory(
    private val recipesRepository: RecipesRepository,
    private val application: Application,
): Factory<FavoritesViewModel> {

    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(recipesRepository, application)
    }

}