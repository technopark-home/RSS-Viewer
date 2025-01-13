package ru.paylab.feature.searcharticles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.paylab.core.model.ArticleSearchRepository
import javax.inject.Inject

@HiltViewModel
internal class ArticleSearchViewModel @Inject constructor(
    private var articleSearchRepository: ArticleSearchRepository,
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<SearchArticleUiState>()
    val eventFlow: StateFlow<SearchArticleUiState> = _eventFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchArticleUiState.Empty,
    )

    private var searchJob: Job? = null

    fun onSearch(query: String) {
        //_searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            if (query.isEmpty()) _eventFlow.emit(SearchArticleUiState.Empty)
            else {
                _eventFlow.emit(SearchArticleUiState.Loading)
                val words = query.replace("\"(\\[\"]|.*)?\"".toRegex(), " ")
                    .split("[^\\p{Alnum}]+".toRegex()).filter { it.isNotBlank() }
                    //.map(Porter::stem)
                    //.filter { it.length > 2 }
                    .joinToString(separator = " OR ", transform = { "$it*" })
                println("Words $words")/*val res0 = articlesRepository.getArticlesSearch("'$query'")
                println("QUERY: RES: $query count: ${res0.size}")**/
                val res = articleSearchRepository.getArticlesSearch("'$words'")
                println("QUERY: RES: $words count: ${res.size}")
                _eventFlow.emit(SearchArticleUiState.Success(query, res))
            }
        }
    }
}