package ru.paylab.feature.articlesviewer

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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.paylab.core.model.ArticlesRepository
import ru.paylab.core.model.CategoriesRepository
import ru.paylab.core.model.UserSettingsRepository
import ru.paylab.core.model.data.ArticleCategories
import ru.paylab.core.model.data.Category
import ru.paylab.core.model.data.FilterView
import ru.paylab.core.model.data.TitleCategory
import ru.paylab.core.model.data.toTitleCategoryList
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    userDataRepository: UserSettingsRepository,
    private val articlesRepository: ArticlesRepository,
    private val categoriesRepository: CategoriesRepository,
) : ViewModel() {
    var isRefreshing by mutableStateOf(false)
        private set

    fun refresh() {
        isRefreshing = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                articlesRepository.refresh()
            } finally {
                isRefreshing = false
            }
        }
    }

    private val _allArticlesPagingDataFlow: Flow<List<ArticleCategories>> =
        articlesRepository.getAllArticlesCategory()
    val allArticles: StateFlow<List<ArticleCategories>> = _allArticlesPagingDataFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )

    fun markAsBookmark(id: Int, isBookmark: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            articlesRepository.updateArticle(id = id, isBookmark = isBookmark)
        }
    }

    fun markIsRead(id: Int, isRead: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            articlesRepository.updateIsRead(id = id, isRead = isRead)
        }
    }

    fun clearSaved(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            articlesRepository.clearSaved(id)
        }
    }

    private val _showByCategory: Flow<Boolean> = userDataRepository.showByCategory

    private val _filters: Flow<Set<FilterView>> = userDataRepository.filters

    private val _selectedCategoryPagingDataFlow: Flow<List<Category>> =
        categoriesRepository.getSelectedCategories()

    val titleCategory: StateFlow<List<TitleCategory>> = combine(
        _filters, _selectedCategoryPagingDataFlow, _showByCategory
    ) { filter, selectedCategory, isShowCategory ->
        filter.toMutableList().map {
            TitleCategory(
                id = it.ordinal,
                name = it.descriptor,
                type = it,
                categoryFavorite = false,
            )
        }.plus(
            if (isShowCategory) selectedCategory.toTitleCategoryList()
            else emptyList()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )
}