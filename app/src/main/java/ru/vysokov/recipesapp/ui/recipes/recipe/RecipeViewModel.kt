package ru.vysokov.recipesapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.vysokov.recipesapp.model.Ingredient

data class RecipeUiState(
    val title: String = "",
    val imageUrl: String? = null,
    val isFavorite: Boolean? = null,
    val ingredients: List<Ingredient> = emptyList(),
    val method: List<String> = emptyList(),
    val numberOfPortions: Int = 1,
)

class RecipeViewModel: ViewModel() {
    private val _uiState = MutableLiveData(RecipeUiState())
    val uiState: LiveData<RecipeUiState> get() = _uiState

    init {
        Log.i("!!!", "ViewModel")
        Log.i("!!!", _uiState.value?.isFavorite.toString())
        _uiState.value = _uiState.value?.copy(
            isFavorite = true
        )
    }

}