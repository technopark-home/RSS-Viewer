package ru.paylab.feature.downloaded

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import ru.paylab.core.model.ArticlesRepository
import ru.paylab.core.localcache.LocalCache
import javax.inject.Inject

@HiltViewModel
internal class LocalCacheViewModel @Inject constructor(
    private var articlesRepository: ArticlesRepository,
    private var localRepository: LocalCache,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val selectedArticleId: Int = checkNotNull(savedStateHandle["ArticlesId"])

    private val _article: Flow<ArticlesUiState> = getArticle(selectedArticleId)
    val selectedArticle: StateFlow<ArticlesUiState> = _article.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ArticlesUiState.Loading,
    )

    private fun getArticle(selectedArticleId: Int): Flow<ArticlesUiState> {
        return articlesRepository.getArticle(selectedArticleId).map {
            ArticlesUiState.Success(it.id, it.link, it.imageUrl)
        }.onStart {
            ArticlesUiState.Loading
        }.catch {
            ArticlesUiState.Error
        }
    }

    fun getDocFileName(id: Int):String {
        return localRepository.saveDoc(id)
    }
}

