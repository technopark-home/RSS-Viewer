package ru.paylab.feature.articledescription

import androidx.lifecycle.SavedStateHandle
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
import ru.paylab.core.designsystem.utils.RouteNavigation
import ru.paylab.core.model.CategoriesRepository
import javax.inject.Inject

@HiltViewModel
internal class ArticleDescriptionViewModel @Inject constructor(
    private val articlesRepository: ArticlesRepository,
    private val categoriesRepository: CategoriesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val selectedArticleId = savedStateHandle.getStateFlow(
        (RouteNavigation.ROUTE_FOUND_ARTICLE_ARG),
        0
    )
    private val _article: Flow<ArticleUiState> = combine(
        articlesRepository.getArticlesWithCategory(selectedArticleId.value),
        selectedArticleId,
    ) { article, selectedId ->
        article?.let {
            if(it.id == selectedId)
                ArticleUiState.Success(it)
            else ArticleUiState.Error
        }?: ArticleUiState.Error
    }

    val selectedArticle: StateFlow<ArticleUiState> = _article.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ArticleUiState.Loading,
    )

    fun articleBookMark(id: Int, isBookmark: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            articlesRepository.updateArticle(id = id, isBookmark = isBookmark)
        }
    }

    fun categoryFavorite(id: Int, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            categoriesRepository.updateCategory(categoryId = id, isFavorite = isFavorite)
        }
    }
    fun clearSaved(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            articlesRepository.clearSaved(id)
        }
    }
}

