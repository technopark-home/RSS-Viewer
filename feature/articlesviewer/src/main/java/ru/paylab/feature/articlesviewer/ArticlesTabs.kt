package ru.paylab.feature.articlesviewer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastAny
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.paylab.core.designsystem.uikit.RssTextWithBadge
import ru.paylab.core.model.data.ArticleCategories
import ru.paylab.core.model.data.FilterView
import ru.paylab.core.model.data.toArticle

@Composable
fun ArticlesTabs(
    modifier: Modifier = Modifier,
    isUnreadOnly: State<Boolean>,
    navigateToSave: (Int) -> Unit,
    articlesViewModel: ArticlesViewModel = hiltViewModel(),
) {
    val selectedCategories by articlesViewModel.titleCategory.collectAsStateWithLifecycle()
    val allArticles by articlesViewModel.allArticles.collectAsStateWithLifecycle()
    val onBookMark = articlesViewModel::markAsBookmark
    val onMarkRead = articlesViewModel::markIsRead
    val onSave: (Int, Boolean) -> Unit = { id, isSave ->
        if (isSave)
            navigateToSave(id)
        else
            articlesViewModel.clearSaved(id)
    }

    if (selectedCategories.isNotEmpty()) {
        val onFilterUnreadData: (ArticleCategories) -> Boolean = { article ->
            if (isUnreadOnly.value) !article.isRead else true
        }
        val onFilterCategoriesData: (ArticleCategories, Set<Int>) -> Boolean = { article, ids ->
            ((if (isUnreadOnly.value) !article.isRead else true)
            and (article.categories.map{it.id}.any { ids.contains(it) }))
        }
        val onFilterFavoriteCategories: (ArticleCategories) -> Boolean = { article ->
            ((if (isUnreadOnly.value) !article.isRead else true)
                    and (article.categories.fastAny{it.categoryFavorite}))
        }

        Column(modifier = modifier.fillMaxSize()) {
            val pagerState = key(selectedCategories.size) {
                rememberPagerState(initialPage = 0) { selectedCategories.size }
            }
            val animationScope = rememberCoroutineScope()
            ScrollableTabRow(selectedTabIndex = pagerState.currentPage) {
                //println("SET: ${selectedCategories.map{it.id}.toSet()}")
                selectedCategories.forEachIndexed { index, category ->
                    Tab(selected = pagerState.currentPage == (index), onClick = {
                        animationScope.launch { pagerState.animateScrollToPage(index) }
                    }, text = {
                        when (selectedCategories[index].type) {
                            FilterView.ALL -> RssTextWithBadge(
                                counter = allArticles.count { onFilterUnreadData(it) },
                                text = category.name
                            )

                            FilterView.FOR_YOU -> RssTextWithBadge(
                                counter = allArticles.count {
                                        article ->
                                    onFilterFavoriteCategories(article)
                                },
                                text = category.name
                            )
                            FilterView.CATEGORIES ->
                                RssTextWithBadge(
                                    counter = allArticles.count { article ->
                                        onFilterCategoriesData(article, setOf(selectedCategories[index].id))
                                    },
                                    text = category.name
                                )
                        }
                    })
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                when (selectedCategories[page].type) {
                    FilterView.ALL -> {
                        ArticlesRefreshListScreen(
                            articles = allArticles
                                .filter { onFilterUnreadData(it) }
                                .map{ it.toArticle(it.isSaved)},
                            isRefreshing = articlesViewModel.isRefreshing,
                            onRefresh = articlesViewModel::refresh,
                            onMarkRead = onMarkRead,
                            onBookMark = onBookMark,
                            onSave = onSave,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    FilterView.FOR_YOU -> {
                        ArticlesRefreshListScreen(
                            articles = allArticles
                                .filter { article ->
                                    onFilterFavoriteCategories(article) }
                                .map{ it.toArticle(it.isSaved)},
                            isRefreshing = articlesViewModel.isRefreshing,
                            onRefresh = articlesViewModel::refresh,
                            onMarkRead = onMarkRead,
                            onBookMark = onBookMark,
                            onSave = onSave,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    FilterView.CATEGORIES -> {
                        ArticlesRefreshListScreen(
                            articles = allArticles
                                .filter { article ->
                                    onFilterCategoriesData(article, setOf(selectedCategories[page].id)) }
                                .map{ it.toArticle(it.isSaved)},
                            isRefreshing = articlesViewModel.isRefreshing,
                            onRefresh = articlesViewModel::refresh,
                            onMarkRead = onMarkRead,
                            onBookMark = onBookMark,
                            onSave = onSave,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}