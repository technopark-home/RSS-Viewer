package ru.paylab.feature.searcharticles

import ru.paylab.core.model.data.ArticleSearch

internal sealed interface SearchArticleUiState {
    data object Loading : SearchArticleUiState
    data class Success(
        val query: String,
        val feed: List<ArticleSearch>,
    ) : SearchArticleUiState

    data object Empty : SearchArticleUiState
}