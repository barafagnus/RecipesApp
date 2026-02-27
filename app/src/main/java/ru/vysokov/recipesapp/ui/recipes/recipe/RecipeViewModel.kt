package ru.vysokov.recipesapp.ui.recipes.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.vysokov.recipesapp.R
import ru.vysokov.recipesapp.data.network.NetworkClient
import ru.vysokov.recipesapp.data.repository.RecipesRepository
import ru.vysokov.recipesapp.data.utils.FavoritesManager
import ru.vysokov.recipesapp.model.Ingredient

data class RecipeUiState(
    val title: String = "",
    val recipeImageUrl: String = "",
    val isFavorite: Boolean = false,
    val ingredients: List<Ingredient> = emptyList(),
    val method: List<String> = emptyList(),
    val portionsCount: Int = 1,
    val isLoaded: Boolean = false
)

class RecipeViewModel(
    private val repository: RecipesRepository,
    application: Application
) : AndroidViewModel(application) {
    private val _uiState = MutableLiveData(RecipeUiState())
    val uiState: LiveData<RecipeUiState> get() = _uiState
    private val context = getApplication<Application>()

    private val _errorEvent = MutableLiveData<Int>()
    val errorEvent: LiveData<Int> get() = _errorEvent

    // TODO: load from network
    fun loadRecipe(recipeId: Int) {
        repository.getRecipeById(recipeId) { recipe ->
            if (recipe == null) _errorEvent.postValue(R.string.networkError)
            else _uiState.postValue(
                _uiState.value?.copy(
                    title = recipe.title,
                    recipeImageUrl = "${NetworkClient.URL_IMAGES}${recipe.imageUrl}",
                    isFavorite = recipeId.toString() in getFavorites(),
                    ingredients = recipe.ingredients,
                    method = recipe.method,
                    portionsCount = _uiState.value?.portionsCount ?: 1,
                    isLoaded = true
                )
            )
        }
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

    fun updatePortion(portionsCount: Int) {
        _uiState.value = _uiState.value?.copy(
            portionsCount = portionsCount
        )
    }

    private fun getFavorites(): MutableSet<String> {
        return FavoritesManager.getFavorites(context.applicationContext)
    }

    private fun saveFavorites(favorites: Set<String>) {
        FavoritesManager.saveFavorites(context.applicationContext, favorites)
    }
}