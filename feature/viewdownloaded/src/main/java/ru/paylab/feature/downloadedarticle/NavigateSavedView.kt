package ru.paylab.feature.downloadedarticle

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.paylab.core.designsystem.utils.RouteNavigation
import ru.paylab.core.designsystem.uikit.RssTopSingleScreen

fun NavController.navigateToSavedArticle(articleId: Int) {
    navigate("${RouteNavigation.ROUTE_SAVED_ARTICLE}/$articleId")
}

fun NavGraphBuilder.savedArticleScreen(
    onSettingsDialog: () -> Unit,
    clearSavedDoc: (Int) -> Unit,
    navigateUp: () -> Unit,
    title: @Composable () -> Unit,
){
    composable(
        route = "${RouteNavigation.ROUTE_SAVED_ARTICLE}/{${RouteNavigation.ROUTE_SAVED_ARTICLE_ARG}}",
        arguments = listOf(navArgument(RouteNavigation.ROUTE_SAVED_ARTICLE_ARG) { type = NavType.IntType })
    ) {
        SavedArticleScreen(
            onSettingsDialog = onSettingsDialog,
            clearSavedDoc = clearSavedDoc,
            navigateUp = navigateUp,
            title= title,
        )
    }
}

@Composable
internal fun SavedArticleScreen(
    onSettingsDialog: () -> Unit,
    navigateUp: () -> Unit,
    clearSavedDoc: (Int) -> Unit,
    title: @Composable () -> Unit,
    viewModel: SavedArticleViewModel = hiltViewModel()
) = RssTopSingleScreen (
    onSettingsDialog = onSettingsDialog,
    onBack = navigateUp,
    title= title,
) { innerPadding ->
    SavedViewScreen(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        onNavigateUp = navigateUp,
        clearFiles = {clearSavedDoc( viewModel.getSelectedArticleId() )},
        fileName = viewModel.getDocFileName()
    )
}