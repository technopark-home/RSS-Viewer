package ru.paylab.app.rssviewer.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.paylab.core.designsystem.utils.RouteNavigation
import ru.paylab.core.designsystem.uikit.RssTitleText
import ru.paylab.feature.articledescription.foundArticleScreen
import ru.paylab.feature.articledescription.navigateToArticle
import ru.paylab.feature.articlesviewer.articlesScreen
import ru.paylab.feature.bookmark.bookMarkScreen
import ru.paylab.feature.categories.categoriesScreen
import ru.paylab.feature.downloaded.navigateToSaveArticle
import ru.paylab.feature.downloaded.saveArticleScreen
import ru.paylab.feature.downloadedarticle.navigateToSavedArticle
import ru.paylab.feature.downloadedarticle.savedArticleScreen
import ru.paylab.feature.infonavbar.InformationNavigationBar
import ru.paylab.feature.uploadedarticles.savedScreen

@Composable
internal fun AppRssNavHost(
    navController: NavHostController,
    onSettingsDialog: () -> Unit,
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val screenTitle = mainViewModel.screenTitle.collectAsStateWithLifecycle()
    val title =  @Composable{ RssTitleText(screenTitle.value) }
    val bottomBar = @Composable{ InformationNavigationBar(navController) }
    val isFilterUnread = rememberSaveable { mutableStateOf(false) }

    val showSearchDialog = rememberSaveable { mutableStateOf(false) }
    val onSearchDialog: () -> Unit = { showSearchDialog.value = true }
    // Navigate
    val navigateToSave: (Int) -> Unit = navController::navigateToSaveArticle
    val navigateToSavedView: (Int) -> Unit = navController::navigateToSavedArticle
    val navigateToArticleView: (Int) -> Unit = navController::navigateToArticle
    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }
    NavHost(
        navController = navController,
        startDestination = RouteNavigation.ROUTE_ARTICLE,
    ) {
        foundArticleScreen(
            onSettingsDialog = onSettingsDialog,
            navigateUp = navigateUp,
            navigateToSave = navigateToSave,
            title = title,
        )
        savedArticleScreen(
            onSettingsDialog = onSettingsDialog,
            clearSavedDoc = mainViewModel::clearSaved,
            navigateUp = navigateUp,
            title = title,
        )
        saveArticleScreen(
            onSettingsDialog = onSettingsDialog,
            navigateUp = navigateUp,
            onSaveImg = mainViewModel::saveImage,
            title = title,
        )
        savedScreen(
            onSettingsDialog = onSettingsDialog,
            navigateToSavedView = navigateToSavedView,
            title = title,
            bottomBar = bottomBar,
        )
        articlesScreen(
            showSearchDialog = showSearchDialog,
            onSearchDialog = onSearchDialog,
            isUnreadOnly = isFilterUnread,
            onFilterClick = { isFilterUnread.value = !isFilterUnread.value },
            onSettingsDialog = onSettingsDialog,
            onNavigateToArticleView = navigateToArticleView,
            navigateToSave = navigateToSave,
            title = title,
            bottomBar = bottomBar,
        )
        categoriesScreen(
            onSettingsDialog = onSettingsDialog,
            showSearchDialog = showSearchDialog,
            onSearchDialog = onSearchDialog,
            title = title,
            bottomBar = bottomBar,
        )
        bookMarkScreen(
            onSettingsDialog = onSettingsDialog,
            navigateToSave = navigateToSave,
            title = title,
            bottomBar = bottomBar,
        )
    }
}