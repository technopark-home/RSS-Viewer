package ru.paylab.feature.articlesviewer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.paylab.core.designsystem.utils.RouteNavigation
import ru.paylab.core.designsystem.uikit.RssTopScreen
import ru.paylab.feature.searcharticles.SearchBarArticles

fun NavGraphBuilder.articlesScreen(
    showSearchDialog: MutableState<Boolean>,
    onSearchDialog: () -> Unit,
    onFilterClick: () -> Unit = {},
    isUnreadOnly: State<Boolean>,
    onSettingsDialog: () -> Unit,
    navigateToSave: (Int) -> Unit,
    onNavigateToArticleView: (Int) -> Unit,
    title: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    composable(route = RouteNavigation.ROUTE_ARTICLE) {
        ArticlesScreen(
            showSearchDialog = showSearchDialog,
            onFilterClick = onFilterClick,
            isFilterUnread = isUnreadOnly,
            onSearchDialog = onSearchDialog,
            onSettingsDialog = onSettingsDialog,
            navigateToSave = navigateToSave,
            onNavigateToArticleView = onNavigateToArticleView,
            title = title,
            bottomBar = bottomBar,
        )
    }
}

@Composable
internal fun ArticlesScreen(
    showSearchDialog: MutableState<Boolean>,
    onFilterClick: () -> Unit = {},
    isFilterUnread: State<Boolean>,
    onSearchDialog: () -> Unit,
    onSettingsDialog: () -> Unit,
    navigateToSave: (Int) -> Unit,
    onNavigateToArticleView: (Int) -> Unit,
    title: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    RssTopScreen(
        onSearchDialog = onSearchDialog,
        isUnreadOnly = isFilterUnread,
        showFilterAction = true,
        onFilterClick = onFilterClick,
        onSettingsDialog = onSettingsDialog,
        title = title,
        bottomBar = bottomBar,
    ) { innerPadding ->
        if (showSearchDialog.value) {
            SearchBarArticles(
                expanded = showSearchDialog,
                onExpanded = { showSearchDialog.value = it },
                onNavigateToArticleView = onNavigateToArticleView,
            )
        } else {
            ArticlesTabs(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                isUnreadOnly = isFilterUnread,
                navigateToSave = navigateToSave
            )
        }
    }
}