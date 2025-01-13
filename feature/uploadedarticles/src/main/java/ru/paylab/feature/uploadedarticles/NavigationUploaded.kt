package ru.paylab.feature.uploadedarticles

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.paylab.core.designsystem.utils.RouteNavigation

fun NavGraphBuilder.savedScreen(
    onSettingsDialog: () -> Unit,
    navigateToSavedView: (Int) -> Unit,
    title: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    composable(route = RouteNavigation.ROUTE_SAVED) {
        SavedArticlesScreen(
            onSettingsDialog = onSettingsDialog,
            onSavedView = navigateToSavedView,
            title = title,
            bottomBar = bottomBar,
        )
    }
}