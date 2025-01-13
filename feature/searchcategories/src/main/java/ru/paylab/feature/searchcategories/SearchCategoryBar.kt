package ru.paylab.feature.searchcategories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.paylab.core.designsystem.uikit.RssSearchTopBar
import ru.paylab.core.designsystem.utils.BuildAnnotatedShortString

@Composable
fun SearchCategoryBar(
    viewModel: SearchCategoryViewModel = hiltViewModel(),
    expanded: MutableState<Boolean>,
    onNavigateToCategoryView: (Int) -> Unit,
    onExpanded: (Boolean) -> Unit,
) {
    val categoriesSearch by viewModel.searchCategoriesListFlow.collectAsStateWithLifecycle()
    RssSearchTopBar(
        expanded, modifier = Modifier.fillMaxWidth(), onSearchQuery = {
            viewModel.setQueryCategory(it)
        }, onExpanded = onExpanded
    ) {
        when (val categories = categoriesSearch) {
            SearchCategoryUiState.Loading -> Text("Loading...", modifier = Modifier.fillMaxWidth())
            SearchCategoryUiState.Empty -> Text(
                "Empty query...", modifier = Modifier.fillMaxWidth()
            )

            is SearchCategoryUiState.Success -> {
                LazyColumn {
                    items(categories.categories) {
                        ListItem(
                            headlineContent = {
                                val stringShow =
                                    BuildAnnotatedShortString(it.name, categories.query, false)
                                Text( stringShow )
                            },
                            modifier = Modifier.clickable {
                                viewModel.markFavoriteCategory(it.id, !it.categoryFavorite)
                                onExpanded(false)
                                println("Be scroll to ${it.id}")
                                onNavigateToCategoryView(it.id)
                            },
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}