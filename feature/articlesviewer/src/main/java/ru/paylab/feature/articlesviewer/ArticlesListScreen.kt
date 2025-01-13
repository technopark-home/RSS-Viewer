package ru.paylab.feature.articlesviewer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ru.paylab.core.designsystem.uikit.RssPullRefreshIndicator
import ru.paylab.core.designsystem.uikit.ArticleCard
import ru.paylab.core.model.data.Article

@Composable
internal fun ArticlesRefreshListScreen(
    articles: List<Article>,
    onMarkRead: (Int, Boolean) -> Unit,
    onBookMark: (Int, Boolean) -> Unit,
    onSave: (Int, Boolean) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    RssPullRefreshIndicator(modifier = modifier.fillMaxSize(),
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        ArticlesListScreen(
            //modifier = modifier,
            infos = articles,
            onMarkRead = onMarkRead,
            onBookMark = onBookMark,
            onSave = onSave
        )
    }
}

@Composable
internal fun ArticlesListScreen(
    modifier: Modifier = Modifier,
    infos: List<Article>,
    onMarkRead: (Int, Boolean) -> Unit,
    onBookMark: (Int, Boolean) -> Unit,
    onSave: (Int, Boolean) -> Unit,
) {
    println("ArticleInfo ${infos.size}")
    var clickedItemId by remember { mutableIntStateOf(Int.MIN_VALUE) }

    LazyColumn(modifier.fillMaxSize()) {
        items(infos) { it: Article ->
            ArticleCard(
                article = it,
                onItemClick = { id ->
                    if(clickedItemId != Int.MIN_VALUE) {
                        onMarkRead(clickedItemId, true)
                    }
                    clickedItemId = if (clickedItemId == id) Int.MIN_VALUE
                    else id
                },
                expandedItemId = clickedItemId,
                onBookMark = onBookMark,
                onSave = onSave,
                onSavedView = {},
            )
        }
    }
}