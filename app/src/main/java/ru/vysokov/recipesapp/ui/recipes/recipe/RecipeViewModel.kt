package ru.vysokov.recipesapp.ui.recipes.recipe

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

}