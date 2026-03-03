package ru.vysokov.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
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

    fun loadCategories() {
        viewModelScope.launch {
            val categoriesFromCache = repository.getCategoriesFromCache()
            updateUi(categoriesFromCache)

            val networkCategories = repository.getCategories()

            if (networkCategories != null) {
                repository.saveCategoriesToCache(networkCategories)
                updateUi(networkCategories)
            } else {
                if (categoriesFromCache.isEmpty()) {
                    _errorEvent.postValue(R.string.networkError)
                }
            }
        }
    }

    private fun updateUi(categories: List<Category>) {
        _uiState.postValue(
            _uiState.value?.copy(
                categories = categories,
                isLoaded = true
            )
        )
    }
}