package ru.vysokov.recipesapp.di

import ru.vysokov.recipesapp.data.repository.RecipesRepository
import ru.vysokov.recipesapp.ui.categories.CategoriesListViewModel

class CategoriesListViewModelFactory(
    private val recipesRepository: RecipesRepository
) : Factory<CategoriesListViewModel> {

    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(recipesRepository)
    }

}