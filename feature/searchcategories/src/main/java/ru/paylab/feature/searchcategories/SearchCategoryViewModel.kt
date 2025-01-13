package ru.paylab.feature.searchcategories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.paylab.core.model.CategoriesRepository
import ru.paylab.core.model.data.Category
import javax.inject.Inject

@HiltViewModel
class SearchCategoryViewModel @Inject constructor(
    private var categoriesRepository: CategoriesRepository,
) : ViewModel() {
    private var _queryCategory = MutableStateFlow("")

    fun setQueryCategory(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _queryCategory.emit(query)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchCategoriesListFlow: StateFlow<SearchCategoryUiState> =
        _queryCategory.flatMapLatest { query ->
            flow { emit(SearchCategoryUiState.Loading) }
            categoriesRepository.getCategoriesSearch(query).map {
                if (it.isEmpty()) SearchCategoryUiState.Empty
                else SearchCategoryUiState.Success( query, it )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchCategoryUiState.Empty,
        )
    fun markFavoriteCategory(id: Int, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            categoriesRepository.updateCategory(
                categoryId = id, isFavorite = isFavorite
            )
        }
    }
}

sealed interface SearchCategoryUiState {
    data object Loading : SearchCategoryUiState
    data class Success(
        val query: String,
        val categories: List<Category>,
    ) : SearchCategoryUiState

    data object Empty : SearchCategoryUiState
}