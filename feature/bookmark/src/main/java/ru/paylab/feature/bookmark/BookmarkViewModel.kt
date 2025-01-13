package ru.paylab.feature.bookmark

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.paylab.core.model.ArticlesRepository
import ru.paylab.core.model.data.Article
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val articlesRepository: ArticlesRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    private val articlePagingBookmark: Flow<List<Article>> =
        articlesRepository.getBookmarkArticles()

    val itemsBookmark: StateFlow<List<Article>> = articlePagingBookmark.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )

    var isRefreshing by mutableStateOf(false)
    fun refresh() {
        isRefreshing = true
        viewModelScope.launch(coroutineDispatcher) {
            try {
                articlesRepository.refresh()
            } finally {
                isRefreshing = false
            }
        }
    }

    fun setBookmark(id: Int, isBookmark: Boolean) {
        viewModelScope.launch(coroutineDispatcher) {
            articlesRepository.updateArticle(
                id = id, isBookmark = isBookmark
            )
        }
    }

    fun markIsRead(id: Int, isRead: Boolean) {
        viewModelScope.launch(coroutineDispatcher) {
            articlesRepository.updateIsRead(id = id, isRead = isRead)
        }
    }

    fun clearSaved(id: Int) {
        viewModelScope.launch(coroutineDispatcher) {
            articlesRepository.clearSaved(id)
        }
    }
}