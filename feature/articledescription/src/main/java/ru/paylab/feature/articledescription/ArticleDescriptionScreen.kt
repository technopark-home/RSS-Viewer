package ru.paylab.feature.articledescription

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.paylab.core.designsystem.uikit.RssErrorScreen
import ru.paylab.core.designsystem.uikit.RssLoadingScreen

@Composable
internal fun ArticleDescriptionListScreen(
    modifier: Modifier = Modifier,
    navigateToSave: (Int) -> Unit,
    viewModel: ArticleDescriptionViewModel = hiltViewModel<ArticleDescriptionViewModel>(),
) {
    println("ArticleDescriptionListScreen")
    val article: ArticleUiState by viewModel.selectedArticle.collectAsStateWithLifecycle()
    when (val currentArticle = article) {
        is ArticleUiState.Error -> RssErrorScreen()
        is ArticleUiState.Loading -> RssLoadingScreen(modifier)
        is ArticleUiState.Success -> {
            ArticleDescriptionCardScreen(
                modifier = modifier,
                onSave = { idArticle, isSave ->
                    if( isSave )
                        navigateToSave(idArticle)
                    else
                        viewModel.clearSaved(idArticle)
                },
                onSavedView = {},
                onBookMarkToggle = { idArticle, bookMark ->
                    viewModel.articleBookMark(idArticle, bookMark)
                },
                onFavoriteToggle = { idCategory, isFavorite ->
                    viewModel.categoryFavorite( idCategory, isFavorite )
                },
                currentArticle.selectedArticleCategories,
            )
        }
    }
}
