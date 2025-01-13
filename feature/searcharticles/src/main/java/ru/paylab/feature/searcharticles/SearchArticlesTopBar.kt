package ru.paylab.feature.searcharticles

import android.graphics.Typeface
import android.text.Html
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.paylab.core.designsystem.uikit.RssSearchTopBar

@Composable
fun SearchBarArticles(
    expanded: MutableState<Boolean>,
    onNavigateToArticleView: (Int) -> Unit,
    onExpanded: (Boolean) -> Unit,
) {
    val viewModel: ArticleSearchViewModel = hiltViewModel()
    val articlesSearch by viewModel.eventFlow.collectAsStateWithLifecycle()
    RssSearchTopBar(
        expanded, modifier = Modifier.fillMaxWidth(), onSearchQuery = {
            viewModel.onSearch(it)
        }, onExpanded = {
            if(!it)
                viewModel.onSearch("")
            onExpanded(it)
        }
    ) {
        when (val articles = articlesSearch) {
            SearchArticleUiState.Loading -> Text("Loading...", modifier = Modifier.fillMaxWidth())
            SearchArticleUiState.Empty -> Text("Empty query...", modifier = Modifier.fillMaxWidth())
            is SearchArticleUiState.Success -> {
                LazyColumn {
                    items(articles.feed) {
                        ListItem(headlineContent = {
                            Text(
                                Html.fromHtml(it.title, Html.FROM_HTML_MODE_COMPACT)
                                    .toAnnotatedString()
                            )
                        }, modifier = Modifier.clickable {
                            onNavigateToArticleView(it.id)
                        }, supportingContent = {
                            Text(
                                Html.fromHtml(it.description, Html.FROM_HTML_MODE_COMPACT)
                                    .toAnnotatedString()
                            )
                        })
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
internal fun Spanned.toAnnotatedString(): AnnotatedString = buildAnnotatedString {
    val spanned = this@toAnnotatedString
    val spanColor = SpanStyle(
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        //fontWeight = FontWeight.SemiBold,
        background = MaterialTheme.colorScheme.primaryContainer
    )
    append(spanned.toString())
    getSpans(0, spanned.length, Any::class.java).forEach { span ->
        val start = getSpanStart(span)
        val end = getSpanEnd(span)
        when (span) {
            is StyleSpan -> when (span.style) {
                Typeface.BOLD -> addStyle(
                    SpanStyle(fontWeight = FontWeight.Bold).plus(spanColor), start, end
                )

                Typeface.ITALIC -> addStyle(
                    SpanStyle(fontStyle = FontStyle.Italic).plus(spanColor), start, end
                )

                Typeface.BOLD_ITALIC -> addStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic
                    ).plus(spanColor), start, end
                )
            }

            is UnderlineSpan -> addStyle(
                SpanStyle(textDecoration = TextDecoration.Underline), start, end
            )

            is ForegroundColorSpan -> addStyle(
                SpanStyle(color = Color(span.foregroundColor)), start, end
            )
        }
    }
}