package ru.vysokov.recipesapp.di

import android.app.Application
import ru.vysokov.recipesapp.data.repository.RecipesRepository
import ru.vysokov.recipesapp.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(
    private val recipesRepository: RecipesRepository,
    private val application: Application
): Factory<RecipeViewModel> {

    override fun create(): RecipeViewModel {
        return RecipeViewModel(recipesRepository, application)
    }

}