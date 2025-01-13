package ru.paylab.feature.bookmark

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.paylab.core.designsystem.utils.RouteNavigation
import ru.paylab.core.designsystem.uikit.RssPullRefreshIndicator
import ru.paylab.core.designsystem.uikit.RssTopScreen

fun NavGraphBuilder.bookMarkScreen(
    onSettingsDialog: () -> Unit,
    navigateToSave: (Int) -> Unit,
    title: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    composable(route = RouteNavigation.ROUTE_BOOKMARK) {
        BookmarkListScreen(
            onSettingsDialog = onSettingsDialog,
            navigateToSave = navigateToSave,
            title = title,
            bottomBar = bottomBar,
        )
    }
}

@Composable
internal fun BookmarkListScreen(
    onSettingsDialog: () -> Unit,
    navigateToSave: (Int) -> Unit,
    title: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: BookmarkViewModel = hiltViewModel(),
) {
    println("BookmarkListScreen")

    val articles by viewModel.itemsBookmark.collectAsStateWithLifecycle()
    RssTopScreen(
        showSearchAction = false,
        onSettingsDialog = onSettingsDialog,
        title = title,
        bottomBar = bottomBar,
    ) { innerPadding ->
        RssPullRefreshIndicator(
            isRefreshing = viewModel.isRefreshing,
            onRefresh = { viewModel.refresh() }) {
            ArticleInfoList(modifier = Modifier.padding(innerPadding).fillMaxSize(),
                infos = articles,
                onMarkRead = { id, isRead ->
                    viewModel.markIsRead(id, isRead)
                },
                onBookMark = { id, isRead ->
                    viewModel.setBookmark(id, isRead)
                },
                onSave = { id, isSave ->
                    if (isSave) navigateToSave(id)
                    else viewModel.clearSaved(id)
                })
        }
    }
}