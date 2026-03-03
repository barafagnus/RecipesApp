package ru.vysokov.recipesapp.di

import ru.vysokov.recipesapp.data.repository.RecipesRepository
import ru.vysokov.recipesapp.ui.recipes.recipeslist.RecipesListViewModel

class RecipesListViewModelFactory(
    private val recipesRepository: RecipesRepository
): Factory<RecipesListViewModel> {

    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(recipesRepository)
    }

}