package ru.vysokov.recipesapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    application: Application,
    private val repository: RecipesRepository
) : AndroidViewModel(application) {
    private val _uiState = MutableLiveData(CategoriesUiState())
    val uiState: LiveData<CategoriesUiState> get() = _uiState

    private val _errorEvent = MutableLiveData<Int>()
    val errorEvent: LiveData<Int> get() = _errorEvent

    // TODO: load from network
    fun loadCategories() {
        viewModelScope.launch {
            val categoriesFromCache = repository.getCategoriesFromCache()

            if (categoriesFromCache.isNotEmpty()) {
                _uiState.postValue(
                    _uiState.value?.copy(
                        categories = categoriesFromCache,
                        isLoaded = true
                    )
                )
            }

            val networkCategories = repository.getCategories()

            if (networkCategories != null) {
                repository.saveCategoriesToCache(networkCategories)

                _uiState.postValue(
                    _uiState.value?.copy(
                        categories = networkCategories,
                        isLoaded = true
                    )
                )
            } else {
                if (categoriesFromCache.isEmpty()) {
                    _errorEvent.postValue(R.string.networkError)
                }
            }
        }
    }
}