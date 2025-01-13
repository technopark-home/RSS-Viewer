package ru.paylab.core.designsystem.uikit

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import ru.paylab.core.designsystem.R

@Composable
fun RssTitleText( title: String )
{
    if (title.isEmpty()) {
        Text(
            text = stringResource(R.string.rss_view),
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
        )
    } else {
        Text(
            text = title,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
        )
    }
}