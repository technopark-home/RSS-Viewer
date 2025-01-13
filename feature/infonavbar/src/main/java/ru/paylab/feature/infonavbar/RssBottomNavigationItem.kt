package ru.paylab.feature.infonavbar

import androidx.compose.ui.graphics.vector.ImageVector

internal data class RssBottomNavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
)