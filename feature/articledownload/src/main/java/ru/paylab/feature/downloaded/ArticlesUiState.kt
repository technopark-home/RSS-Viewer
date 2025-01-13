package ru.paylab.feature.downloaded

internal sealed interface ArticlesUiState {
    data class Success(val articleId: Int, val link: String, val imageUrl:String ) : ArticlesUiState
    data object Error : ArticlesUiState
    data object Loading : ArticlesUiState
}