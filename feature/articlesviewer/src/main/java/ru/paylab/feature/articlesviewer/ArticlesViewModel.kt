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
import ru.paylab.core.domain.FollowedTopicsUseCase
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
    private val articlesRepository: ArticlesRepository,
    private val followedTopicsUseCase: FollowedTopicsUseCase
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

    val titleCategory: StateFlow<List<TitleCategory>> = followedTopicsUseCase
        .titleCategory
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )
}