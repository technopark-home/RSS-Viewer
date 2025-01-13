package ru.paylab.core.designsystem.uikit

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun BadgeTextCounter(counter: String) {
    Spacer(Modifier.width(8.dp))
    Badge(
        containerColor = MaterialTheme.colorScheme.secondary, ) {
        Text( text = counter, textAlign =  TextAlign.Center, )
    }
}

@Composable
fun RssTextWithBadge(
    counter: Int,
    text: String,
) {
    Row( verticalAlignment = Alignment.CenterVertically,) {
        Text(text)
        if (counter > 99) {
            BadgeTextCounter("99++")
        } else if (counter > 0) {
            BadgeTextCounter(counter.toString())
        }
    }
}