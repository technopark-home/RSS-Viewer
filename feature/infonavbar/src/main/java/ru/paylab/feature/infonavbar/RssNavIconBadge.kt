package ru.paylab.feature.infonavbar

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
internal fun RssNavIconBadge(valueBadge: String, description: String, icon: ImageVector) {
    BadgedBox(badge = {
        if(valueBadge.isEmpty()) {
            Badge()
        } else {
            Badge(containerColor = MaterialTheme.colorScheme.secondary) {
                Text(valueBadge)
            }
        }
    }) { Icon(icon, contentDescription = description) }
}