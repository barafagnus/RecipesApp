package ru.vysokov.recipesapp.ui.recipes.recipeslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vysokov.recipesapp.R
import ru.vysokov.recipesapp.data.repository.RecipesRepository
import ru.vysokov.recipesapp.model.Recipe

data class RecipesListUiState(
    val recipe: List<Recipe> = emptyList(),
    val isLoaded: Boolean = false,
)

class RecipesListViewModel(
    private val repository: RecipesRepository
) : ViewModel() {
    private val _uiState = MutableLiveData(RecipesListUiState())
    val uiState: LiveData<RecipesListUiState> get() = _uiState

    private val _errorEvent = MutableLiveData<Int>()
    val errorEvent: LiveData<Int> get() = _errorEvent

    fun loadRecipes(categoryId: Int?) {
        viewModelScope.launch {
            val recipesFromCache = repository.getRecipesFromCacheByCategory(categoryId)
            updateUi(recipesFromCache)

            val networkRecipes = repository.getRecipesByCategory(categoryId)

            if (networkRecipes != null) {
                repository.saveRecipesToCache(networkRecipes, categoryId)
                updateUi(networkRecipes)
            } else {
                if (recipesFromCache.isEmpty()) {
                    _errorEvent.postValue(R.string.networkError)
                }
            }
        }
    }

    private fun updateUi(recipe: List<Recipe>) {
        _uiState.postValue(
            _uiState.value?.copy(
                recipe = recipe,
                isLoaded = true
            )
        )
    }
}