package ru.paylab.feature.uploadedarticles

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.paylab.core.designsystem.uikit.RssTopScreen
import ru.paylab.core.model.data.Article

@Composable
internal fun SavedArticlesScreen(
    modifier: Modifier = Modifier,
    onSettingsDialog: () -> Unit,
    onSavedView: (Int) -> Unit,
    title: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    RssTopScreen(
        showSearchAction = false,
        onSettingsDialog = onSettingsDialog,
        title = title,
        bottomBar = bottomBar,
    ) { innerPadding ->
        SavedListScreen(
            modifier = modifier.padding(innerPadding),
            onSavedView = onSavedView,
        )
    }
}

@Composable
internal fun SavedListScreen(
    modifier: Modifier = Modifier,
    onSavedView: (Int) -> Unit,
    viewModel: UploadedArticlesModelView = hiltViewModel<UploadedArticlesModelView>(),
) {
    val articles by viewModel.articlesSaved.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
    ) {
        items(articles, key = { info -> info.id }) { it: Article ->
            ExtendedArticleCard(
                article = it,
                onBookMark = viewModel::bookMark,
                onClearSaved = viewModel::clearSaved,
                onSavedView = onSavedView,
                imageFile = viewModel.getSavedImage(it.id),
            )
        }
    }
}