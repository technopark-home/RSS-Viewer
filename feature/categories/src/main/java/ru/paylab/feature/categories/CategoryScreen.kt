package ru.paylab.feature.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.paylab.core.designsystem.uikit.RssPullRefreshIndicator
import ru.paylab.core.model.data.Category


@Composable
internal fun CategoryScreen(
    modifier: Modifier = Modifier,
    clickedCategoryId: MutableIntState,
    viewModel: CategoryViewModel = hiltViewModel(),
) {
    val category by viewModel.itemsCategory.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyStaggeredGridState()
    LaunchedEffect(clickedCategoryId.intValue) {
        val indexCategory = category.indexOfFirst { categoryItem: Category ->
            categoryItem.id == clickedCategoryId.intValue
        }
        if (indexCategory != -1) {
            coroutineScope.launch {
                listState.animateScrollToItem(
                    index = indexCategory,
                )
            }
        }
    }
    CategoriesListScreen(
        modifier = modifier,
        category = category,
        state = listState,
        isRefreshing = viewModel.isRefreshing,
        onRefresh = {viewModel.refresh()},
        onItemClick = viewModel::markFavoriteCategory,
    )
}

@Composable
fun CategoriesListScreen(
    modifier: Modifier = Modifier,
    category: List<Category>,
    state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onItemClick: (Int,Boolean) -> Unit,
) {
    RssPullRefreshIndicator(modifier = modifier.fillMaxSize(),
        isRefreshing = isRefreshing,
        onRefresh = onRefresh) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
                .semantics { contentDescription = "CategoriesLazyVerticalGrid" },
            state = state,
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                items(items = category, key = { item: Category -> item.id }) { category: Category ->
                    // Item Content
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .clickable(onClick = {
                                onItemClick(category.id, !category.categoryFavorite)
                            })
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .testTag("category:${category.id}"),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(
                                checked = category.categoryFavorite,
                                onCheckedChange = null,
                            )
                            Spacer(Modifier.size(2.dp))
                            Text(
                                text = category.name,
                            )
                        }
                    }
                }
            },
        )
    }
}
