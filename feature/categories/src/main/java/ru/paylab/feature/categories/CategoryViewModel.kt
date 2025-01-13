package ru.paylab.feature.categories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.paylab.core.model.ArticlesRepository
import ru.paylab.core.model.CategoriesRepository
import ru.paylab.core.model.data.Category
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private var articlesRepository: ArticlesRepository,
    private var categoriesRepository: CategoriesRepository,
) : ViewModel() {
    private val categoryPagingDataFlow: Flow<List<Category>> = categoriesRepository.getAllCategories()
    val itemsCategory: StateFlow<List<Category>> = categoryPagingDataFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )
    var isRefreshing by mutableStateOf(false)

    fun markFavoriteCategory(id: Int, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            categoriesRepository.updateCategory(
                categoryId = id, isFavorite = isFavorite
            )
        }
    }

    fun refresh() {
        isRefreshing = true
        viewModelScope.launch(Dispatchers.IO) {
            try{
                articlesRepository.refresh()
            } finally {
                isRefreshing = false
            }
        }
    }
}