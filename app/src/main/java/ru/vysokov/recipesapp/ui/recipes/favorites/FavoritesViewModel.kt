package ru.vysokov.recipesapp.ui.recipes.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.vysokov.recipesapp.data.repository.STUB
import ru.vysokov.recipesapp.data.utils.FavoritesManager
import ru.vysokov.recipesapp.model.Recipe

data class FavoritesUiState(
    val favoriteRecipes: List<Recipe> = emptyList(),
    val isLoaded: Boolean = false
)

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableLiveData(FavoritesUiState())
    val uiState: LiveData<FavoritesUiState> get() = _uiState
    private val context = getApplication<Application>()

    // TODO: load from network
    fun loadFavorites() {
        val recipeIds = getFavoritesRecipeIds()
        val recipes = STUB.getRecipesById(recipeIds)

        _uiState.value = _uiState.value?.copy(
            favoriteRecipes = recipes,
            isLoaded = true
        )
    }

    fun getFavoritesRecipeIds(): Set<Int> =
        FavoritesManager.getFavorites(context.applicationContext).map { it.toInt() }.toSet()
}