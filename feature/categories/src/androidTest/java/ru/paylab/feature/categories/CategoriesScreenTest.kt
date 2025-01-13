package ru.paylab.feature.categories

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import ru.paylab.core.model.data.Category

class ArticleListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun showsArticleInfoList() {
        composeTestRule.setContent {
            CategoriesListScreen(
                category = category,
                isRefreshing = false,
                onRefresh = {},
                onItemClick = { _, _ ->}
            )
        }
        composeTestRule
            .onNodeWithContentDescription(
                "CategoriesLazyVerticalGrid"
            ).assertExists()
    }

    @Test
    fun setItemFavorite() {
        var isSetItemClickCalled = false
        composeTestRule.setContent {
            CategoriesListScreen(
                category = category,
                isRefreshing = false,
                onItemClick = { id, _ ->
                    assertEquals(category[1].id, id)
                    isSetItemClickCalled = true
                },
                onRefresh = {},
            )
        }
        composeTestRule.apply {
            onNodeWithTag("category:${category[1].id}")
                .performClick()
        }
        composeTestRule.waitUntil(2_000) { true }
        composeTestRule.apply {
            onNodeWithTag("category:${category[1].id}")
                .performClick()
        }
        assertTrue(isSetItemClickCalled)
    }


    private val category: List<Category> = listOf(
        Category (
            id = 1,
            name = "Category 1",
            categoryFavorite = false,
        ),
        Category (
            id = 2,
            name = "Category 2",
            categoryFavorite = true,
        )
    )
}