package ru.vysokov.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.vysokov.recipesapp.data.repository.STUB
import ru.vysokov.recipesapp.model.Category

data class CategoriesUiState(
    val categories: List<Category> = emptyList(),
    val isLoaded: Boolean = false,
)

class CategoriesListViewModel() : ViewModel() {
    private val _uiState = MutableLiveData(CategoriesUiState())
    val uiState: LiveData<CategoriesUiState> get() = _uiState

    // TODO: load from network
    fun loadCategories() {
        val categories = STUB.getCategories()

        _uiState.value = _uiState.value?.copy(
            categories = categories,
            isLoaded = true
        )
    }
}