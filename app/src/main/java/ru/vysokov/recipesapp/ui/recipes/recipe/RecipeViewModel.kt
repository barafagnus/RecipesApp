package ru.vysokov.recipesapp.ui.recipes.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.vysokov.recipesapp.R
import ru.vysokov.recipesapp.core.Network
import ru.vysokov.recipesapp.data.repository.RecipesRepository
import ru.vysokov.recipesapp.model.Ingredient
import ru.vysokov.recipesapp.model.Recipe
import javax.inject.Inject

data class RecipeUiState(
    val title: String = "",
    val recipeImageUrl: String = "",
    val isFavorite: Boolean = false,
    val ingredients: List<Ingredient> = emptyList(),
    val method: List<String> = emptyList(),
    val portionsCount: Int = 1,
    val isLoaded: Boolean = false
)

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipesRepository,
    application: Application
) : AndroidViewModel(application) {
    private val _uiState = MutableLiveData(RecipeUiState())
    val uiState: LiveData<RecipeUiState> get() = _uiState

    private val _errorEvent = MutableLiveData<Int>()
    val errorEvent: LiveData<Int> get() = _errorEvent

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val recipeFromCache = repository.getRecipeFromCache(recipeId)

            recipeFromCache?.let {
                updateUi(recipeFromCache, repository.isFavorite(recipeId) ?: false)
            }

            val networkRecipe = repository.getRecipeById(recipeId)

            if (networkRecipe != null) {
                repository.saveRecipeToCache(
                    networkRecipe, null
                )
                updateUi(networkRecipe, repository.isFavorite(recipeId) ?: false)
            } else {
                if (recipeFromCache == null) {
                    _errorEvent.postValue(R.string.networkError)
                }
            }
        }
    }

    private fun updateUi(recipe: Recipe, isFavorite: Boolean) {
        _uiState.postValue(
            _uiState.value?.copy(
                title = recipe.title,
                recipeImageUrl = "${Network.URL_IMAGES}${recipe.imageUrl}",
                ingredients = recipe.ingredients,
                isFavorite = isFavorite,
                method = recipe.method,
                portionsCount = _uiState.value?.portionsCount ?: 1,
                isLoaded = true
            )
        )
    }

    fun onFavoritesClicked(recipeId: Int?) {
        viewModelScope.launch {
            val currentStatus = repository.isFavorite(recipeId) ?: false
            val newStatus = !currentStatus

            repository.updateFavoriteInCache(recipeId, isFavorite = newStatus)

            _uiState.value = _uiState.value?.copy(
                isFavorite = newStatus
            )
        }
    }

    fun updatePortion(portionsCount: Int) {
        _uiState.value = _uiState.value?.copy(
            portionsCount = portionsCount
        )
    }

}