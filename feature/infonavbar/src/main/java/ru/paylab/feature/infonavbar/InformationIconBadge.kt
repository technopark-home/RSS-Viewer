package ru.paylab.feature.infonavbar

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
internal fun InformationIconBadge(
    value: Long,
    description: String,
    icon: ImageVector,
) {
    if (value > 0) {
        RssNavIconBadge(
            valueBadge = if (value > 999) "999+" else value.toString(),
            description = description,
            icon = icon
        )
    } else if (value == 0.toLong()) {
        RssNavIconBadge(
            valueBadge = "", description = description, icon = icon
        )
    } else {
        Icon(imageVector = icon, contentDescription = description)
    }

}