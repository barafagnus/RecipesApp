package ru.vysokov.recipesapp.ui.recipes.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.vysokov.recipesapp.data.utils.FavoritesManager
import ru.vysokov.recipesapp.model.Ingredient

data class RecipeUiState(
    val title: String = "",
    val imageUrl: String? = null,
    val isFavorite: Boolean = false,
    val ingredients: List<Ingredient> = emptyList(),
    val method: List<String> = emptyList(),
    val portionsCount: Int = 1,
)

class RecipeViewModel(
    application: Application
): AndroidViewModel(application) {
    private val _uiState = MutableLiveData(RecipeUiState())
    val uiState: LiveData<RecipeUiState> get() = _uiState
    private val context = getApplication<Application>()

    // TODO: load from network
    fun loadRecipe(recipeId: Int?) {
        _uiState.value = _uiState.value?.copy(
            isFavorite = recipeId?.toString() in getFavorites(),
            //portionsCount = _uiState.value?.portionsCount ?: return
        )
    }

    fun onFavoritesClicked(recipeId: Int?) {
        val favorites = getFavorites().toMutableSet()

        val isFavorite = if (recipeId?.toString() in favorites) {
            favorites.remove(recipeId.toString())
            false
        } else {
            favorites.add(recipeId.toString())
            true
        }

        saveFavorites(favorites)

        _uiState.value = _uiState.value?.copy(
            isFavorite = isFavorite
        )
    }

    private fun getFavorites(): MutableSet<String> {
        return FavoritesManager.getFavorites(context.applicationContext)
    }

    private fun saveFavorites(favorites: Set<String>) {
        FavoritesManager.saveFavorites(context.applicationContext, favorites)
    }

}