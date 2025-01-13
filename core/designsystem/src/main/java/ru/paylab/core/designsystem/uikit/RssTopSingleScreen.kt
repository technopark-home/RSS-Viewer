package ru.paylab.core.designsystem.uikit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.paylab.core.designsystem.utils.RssViewerIcons

@Composable
fun RssTopSingleScreen(
    onSettingsDialog: () -> Unit,
    onBack: () -> Unit,
    title: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(topBar = {
        RssTopAppBar(
            title = title,
            actionIcon = RssViewerIcons.Settings,
            onActionClick = onSettingsDialog,
            navigationIcon = RssViewerIcons.ArrowBack,
            onNavigationClick = onBack,
        )
    }, modifier = Modifier.fillMaxSize(), content = content
    )
}