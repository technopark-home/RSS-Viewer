package ru.paylab.feature.categories

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.paylab.core.designsystem.utils.RouteNavigation
import ru.paylab.core.designsystem.uikit.RssTopScreen
import ru.paylab.feature.searchcategories.SearchCategoryBar

fun NavGraphBuilder.categoriesScreen(
    showSearchDialog: MutableState<Boolean>,
    onSearchDialog: () -> Unit,
    onSettingsDialog: () -> Unit,
    title: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    composable(route = RouteNavigation.ROUTE_CATEGORIES) {
        CategoriesScreen(
            showSearchDialog = showSearchDialog,
            onSearchDialog = onSearchDialog,
            onSettingsDialog = onSettingsDialog,
            title = title,
            bottomBar = bottomBar,
        )
    }
}

@Composable
internal fun CategoriesScreen(
    showSearchDialog: MutableState<Boolean>,
    onSearchDialog: () -> Unit,
    onSettingsDialog: () -> Unit,
    title: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
) = RssTopScreen(
    onSettingsDialog = onSettingsDialog,
    onSearchDialog = onSearchDialog,
    title = title,
    bottomBar = bottomBar,
) { innerPadding ->
    val clickedCategoryId = remember { mutableIntStateOf(Int.MIN_VALUE) }
    if (showSearchDialog.value) {
        SearchCategoryBar(
            expanded = showSearchDialog,
            onExpanded = { showSearchDialog.value = it },
            onNavigateToCategoryView = { selectedId ->
                clickedCategoryId.intValue = selectedId
            },
        )
    } else {
        CategoryScreen(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            clickedCategoryId = clickedCategoryId,
        )
    }
}