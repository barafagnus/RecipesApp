package ru.vysokov.recipesapp.ui.recipes.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.vysokov.recipesapp.R
import ru.vysokov.recipesapp.data.repository.RecipesRepository
import ru.vysokov.recipesapp.data.utils.FavoritesManager
import ru.vysokov.recipesapp.model.Recipe

data class FavoritesUiState(
    val favoriteRecipes: List<Recipe> = emptyList(),
    val isLoaded: Boolean = false
)

class FavoritesViewModel(
    private val repository: RecipesRepository,
    application: Application
) : AndroidViewModel(application) {
    private val _uiState = MutableLiveData(FavoritesUiState())
    val uiState: LiveData<FavoritesUiState> get() = _uiState
    private val context = getApplication<Application>()

    private val _errorEvent = MutableLiveData<Int>()
    val errorEvent: LiveData<Int> get() = _errorEvent

    // TODO: load from network
    fun loadFavorites() {
        val recipeIds = getFavoritesRecipeIds()
        repository.getRecipesByIds(recipeIds) { recipes ->
            if (recipes == null) _errorEvent.postValue(R.string.networkError)
            else _uiState.postValue(
                _uiState.value?.copy(
                    favoriteRecipes = recipes,
                    isLoaded = true
                )
            )
        }


    }

    fun getFavoritesRecipeIds(): Set<Int> =
        FavoritesManager.getFavorites(context.applicationContext).map { it.toInt() }.toSet()
}