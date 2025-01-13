package ru.paylab.core.designsystem.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle


@Composable
fun BuildAnnotatedShortString(text: String, query: String, firstFound: Boolean): AnnotatedString {
    val span = SpanStyle(
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        fontWeight = FontWeight.SemiBold,
        background = MaterialTheme.colorScheme.primaryContainer
    )
    val searchText = if (firstFound) {
        val pos = 20
        val queryPosition = text.indexOf(query, 0, ignoreCase = true)
        val startText = if ((queryPosition - pos) > 0) (queryPosition - pos) else 0
        val endText = if ((queryPosition + pos + query.length) > text.length) text.length
        else (queryPosition + pos + query.length)
        if (startText != 0) "..." else "" + text.substring(
            startText, endText
        ) + if (endText != text.length) "..." else ""
    } else {
        text
    }
    return buildAnnotatedString {
        var start = 0
        while (searchText.indexOf(query, start, ignoreCase = true) != -1 && query.isNotBlank()) {
            val firstIndex = searchText.indexOf(query, start, true)
            val end = firstIndex + query.length
            append(searchText.substring(start, firstIndex))
            withStyle(style = span) {
                append(searchText.substring(firstIndex, end))
            }
            start = end
        }
        append(searchText.substring(start, searchText.length))
        toAnnotatedString()
    }
}