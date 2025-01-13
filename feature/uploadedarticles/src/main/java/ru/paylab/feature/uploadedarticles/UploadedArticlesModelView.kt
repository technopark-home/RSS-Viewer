package ru.paylab.feature.uploadedarticles

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
import ru.paylab.core.localcache.LocalCache
import ru.paylab.core.model.data.Article
import javax.inject.Inject

@HiltViewModel
class UploadedArticlesModelView @Inject constructor(
    private var articlesRepository: ArticlesRepository,
    private var localCache: LocalCache,
) : ViewModel() {
    private val _articlesSavedPagingFlow: Flow<List<Article>> =
        combine(articlesRepository.getAllArticles(), localCache.getSavedIds()) { article, ids ->
            article.filter {
                ids.contains(it.id)
            }
        }
    val articlesSaved: StateFlow<List<Article>> = _articlesSavedPagingFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )

    fun bookMark( id: Int, isBookmark: Boolean ) {
        viewModelScope.launch(Dispatchers.IO) {
            articlesRepository.updateArticle(id = id, isBookmark = isBookmark)
        }
    }

    fun clearSaved( id:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            localCache.clearSaved(id)
            localCache.refresh()
        }
    }

    fun getSavedImage( id: Int ): String {
        return localCache.savedImageFileName(id = id)
    }
}