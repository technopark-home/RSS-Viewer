package ru.paylab.core.designsystem.uikit

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RssTopAppBar(
    title: @Composable () -> Unit,
    navigationIcon: ImageVector? = null,
    navigationIconContentDescription: String = "",
    actionIcon: ImageVector? = null,
    actionIconContentDescription: String = "",
    secondActionIcon: ImageVector? = null,
    secondActionIconContentDescription: String = "",
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
    onSecondActionClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = title,
        navigationIcon = {
            navigationIcon?.let{
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = navigationIconContentDescription,
                    )
                }
            }
        },
        actions = {
            secondActionIcon?.let {
                IconButton(onClick = onSecondActionClick) {
                    Icon(
                        imageVector = secondActionIcon,
                        contentDescription = secondActionIconContentDescription,
                    )
                }
            }
            actionIcon?.let {
                IconButton(onClick = onActionClick) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = actionIconContentDescription,
                    )
                }
            }
        },
    )
}