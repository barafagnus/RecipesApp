package ru.vysokov.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.vysokov.recipesapp.R
import ru.vysokov.recipesapp.data.repository.RecipesRepository
import ru.vysokov.recipesapp.model.Category

data class CategoriesUiState(
    val categories: List<Category> = emptyList(),
    val isLoaded: Boolean = false,
)

class CategoriesListViewModel(
    private val repository: RecipesRepository
) : ViewModel() {
    private val _uiState = MutableLiveData(CategoriesUiState())
    val uiState: LiveData<CategoriesUiState> get() = _uiState

    private val _errorEvent = MutableLiveData<Int>()
    val errorEvent: LiveData<Int> get() = _errorEvent

    // TODO: load from network
    fun loadCategories() {
        repository.getCategories { categories ->
            if (categories == null) _errorEvent.postValue(R.string.networkError)
            else _uiState.postValue(
                _uiState.value?.copy(
                    categories = categories,
                    isLoaded = true
                )
            )

        }
    }
}