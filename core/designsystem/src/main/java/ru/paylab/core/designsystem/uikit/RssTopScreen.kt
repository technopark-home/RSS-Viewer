package ru.paylab.core.designsystem.uikit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import ru.paylab.core.designsystem.utils.RssViewerIcons

@Composable
fun RssTopScreen(
    showSearchAction: Boolean = true,
    onSearchDialog: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    isUnreadOnly: State<Boolean> = mutableStateOf(value = false),
    showFilterAction: Boolean = false,
    onSettingsDialog: () -> Unit,
    title: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    var secondActionIcon: ImageVector? = null
    if( showFilterAction ) {
        secondActionIcon = if (isUnreadOnly.value) RssViewerIcons.VisibilityOff else RssViewerIcons.Visibility
    }
    var navigationIcon: ImageVector? = null
    if( showSearchAction ) {
        navigationIcon = RssViewerIcons.Search
    }
    Scaffold(topBar = {
            RssTopAppBar(
                title = title,
                actionIcon = RssViewerIcons.Settings,
                onActionClick = onSettingsDialog,
                secondActionIcon = secondActionIcon,
                onSecondActionClick = onFilterClick,
                navigationIcon = navigationIcon,
                onNavigationClick = onSearchDialog,
            )
        },
        bottomBar = bottomBar,
        modifier = Modifier.fillMaxSize(), content = content
    )
}
