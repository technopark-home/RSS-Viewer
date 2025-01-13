package ru.paylab.feature.articledescription

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.paylab.core.designsystem.utils.RouteNavigation
import ru.paylab.core.designsystem.uikit.RssTopSingleScreen

fun NavController.navigateToArticle(articleId: Int) {
    navigate(route = "${RouteNavigation.ROUTE_FOUND_ARTICLE}/$articleId")
}

fun NavGraphBuilder.foundArticleScreen(
    onSettingsDialog: () -> Unit,
    navigateUp: () -> Unit,
    navigateToSave: (Int) -> Unit,
    title: @Composable () -> Unit,
) {
    composable(
        route = "${RouteNavigation.ROUTE_FOUND_ARTICLE}/{${RouteNavigation.ROUTE_FOUND_ARTICLE_ARG}}",
        arguments = listOf(navArgument(RouteNavigation.ROUTE_FOUND_ARTICLE_ARG) { type = NavType.IntType })
    ) {
        NavigateFoundArticle(
            onSettingsDialog = onSettingsDialog,
            navigateUp = navigateUp,
            navigateToSave = navigateToSave,
            title = title,
        )
    }
}

@Composable
internal fun NavigateFoundArticle(
    onSettingsDialog: () -> Unit,
    navigateUp: () -> Unit,
    navigateToSave: (Int) -> Unit,
    title: @Composable () -> Unit,
) {
    RssTopSingleScreen(
        onSettingsDialog = onSettingsDialog,
        onBack = navigateUp,
        title = title,
    ) { innerPadding ->
        ArticleDescriptionListScreen(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            navigateToSave = navigateToSave,
        )
    }
}