package ru.vysokov.recipesapp.ui.recipes.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.vysokov.recipesapp.R
import ru.vysokov.recipesapp.data.repository.RecipesRepository
import ru.vysokov.recipesapp.model.Recipe
import javax.inject.Inject

data class FavoritesUiState(
    val favoriteRecipes: List<Recipe> = emptyList(),
    val isLoaded: Boolean = false
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: RecipesRepository
) : ViewModel() {
    private val _uiState = MutableLiveData(FavoritesUiState())
    val uiState: LiveData<FavoritesUiState> get() = _uiState

    private val _errorEvent = MutableLiveData<Int>()
    val errorEvent: LiveData<Int> get() = _errorEvent

    fun loadFavorites() {
        viewModelScope.launch {
            try {
                val favoritesFromCache = repository.getFavoritesFromCache()
                updateUi(favoritesFromCache)
            } catch (e: Exception) {
                _errorEvent.postValue(R.string.networkError)
            }
        }
    }

    private fun updateUi(recipes: List<Recipe>) {
        _uiState.postValue(
            _uiState.value?.copy(
                favoriteRecipes = recipes,
                isLoaded = true
            )
        )
    }
}