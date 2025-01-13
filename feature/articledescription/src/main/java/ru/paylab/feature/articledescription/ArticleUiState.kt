package ru.paylab.feature.articledescription

import ru.paylab.core.model.data.ArticleCategories

internal sealed interface ArticleUiState {
    data class Success(val selectedArticleCategories: ArticleCategories) : ArticleUiState
    data object Error : ArticleUiState
    data object Loading : ArticleUiState
}