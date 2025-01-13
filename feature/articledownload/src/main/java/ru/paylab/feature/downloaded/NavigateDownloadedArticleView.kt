package ru.paylab.feature.downloaded

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

fun NavController.navigateToSaveArticle(articleId: Int) {
    navigate("${RouteNavigation.ROUTE_SAVE_ARTICLE}/$articleId")
}

fun NavGraphBuilder.saveArticleScreen(
    onSettingsDialog: () -> Unit,
    navigateUp: () -> Unit,
    onSaveImg: (Int,String) -> Unit,
    title: @Composable () -> Unit,
){
    composable(
        route = "${RouteNavigation.ROUTE_SAVE_ARTICLE}/{${RouteNavigation.ROUTE_SAVE_ARTICLE_ARG}}",
        arguments = listOf(navArgument(RouteNavigation.ROUTE_SAVE_ARTICLE_ARG) { type = NavType.IntType })
    ) {
        SaveArticleScreen(
            onSettingsDialog = onSettingsDialog,
            navigateUp = navigateUp,
            onSaveImg = onSaveImg,
            title= title,
        )
    }
}

@Composable
internal fun SaveArticleScreen(
    onSettingsDialog: () -> Unit,
    navigateUp: () -> Unit,
    onSaveImg: (Int,String) -> Unit,
    title: @Composable () -> Unit,
) {
    RssTopSingleScreen(
        onSettingsDialog = onSettingsDialog,
        onBack = navigateUp,
        title= title,
    ) { innerPadding ->
        WebViewSave(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            onNavigateUp = navigateUp,
            onSaveImg = onSaveImg,
        )
    }
}