package ru.vysokov.recipesapp.ui.recipes.recipeslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.vysokov.recipesapp.data.repository.STUB
import ru.vysokov.recipesapp.model.Recipe

data class RecipesListUiState(
    val recipe: List<Recipe> = emptyList(),
    val isLoaded: Boolean = false,
)

class RecipesListViewModel(): ViewModel() {
    private val _uiState = MutableLiveData(RecipesListUiState())
    val uiState: LiveData<RecipesListUiState> get() = _uiState

    fun loadRecipes(categoryId: Int?) {
        val dataset = STUB.getRecipesByCategoryId(categoryId)

        _uiState.value = _uiState.value?.copy(
            recipe = dataset,
            isLoaded = true
        )
    }
}