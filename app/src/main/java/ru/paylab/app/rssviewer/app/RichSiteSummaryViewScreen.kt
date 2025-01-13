package ru.paylab.app.rssviewer.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import ru.paylab.feature.settings.SettingsDialog

@Composable
internal fun RichSiteSummaryViewScreen() {
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

    if (showSettingsDialog) {
        SettingsDialog { showSettingsDialog = false }
    }
    val navController = rememberNavController()

    AppRssNavHost(
        onSettingsDialog = { showSettingsDialog = true },
        navController = navController,
    )
}