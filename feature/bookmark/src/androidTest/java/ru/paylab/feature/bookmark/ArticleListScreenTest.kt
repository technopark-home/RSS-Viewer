package ru.paylab.feature.bookmark

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import ru.paylab.core.model.data.Article

class ArticleListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun showsArticleInfoList() {
        composeTestRule.setContent {
            ArticleInfoList(
                infos = infos,
                onMarkRead = { id, isRead ->},
                onBookMark = { _,_ ->},
                onSave = { _,_ ->},
            )
        }
        composeTestRule
            .onNodeWithContentDescription(
                "ArticleLazyList"
            ).assertExists()
    }

    @Test
    fun setItemRead() {
        var isSetReadCalled = false
        composeTestRule.setContent {
            ArticleInfoList(
                infos = infos,
                onMarkRead = { id, isRead ->
                    assertEquals(infos[1].id, id)
                    isSetReadCalled = true
                             },
                onBookMark = { _,_ ->},
                onSave = { _,_ ->},
            )
        }
        composeTestRule.apply {
            onNodeWithTag("article:${infos[1].id}")
                .performClick()
        }
        composeTestRule.waitUntil(2_000) { true }
        composeTestRule.apply {
            onNodeWithTag("article:${infos[1].id}")
                .performClick()
        }
        assertTrue(isSetReadCalled)
    }

    @Test
    fun setItemBookmark() {
        var bookmarkCalled = false
        composeTestRule.setContent {
            ArticleInfoList(
                infos = infos,
                onMarkRead = { _,_ ->
                },
                onBookMark = { id, _ ->
                    assertEquals(infos[1].id, id)
                    bookmarkCalled = true
                             },
                onSave = { _, _ -> },
            )
        }
        composeTestRule
            .onAllNodesWithTag("ArticleCard")
            .filterToOne(
                hasAnyDescendant(hasText(infos[1].title))
                        and
                    hasAnyDescendant(hasTestTag("Bookmark"))
            )
            .onChildren()
            .filterToOne(hasTestTag("Bookmark"))
            .performClick()

        assertTrue(bookmarkCalled)
    }

    private val infos: List<Article> = listOf(
        Article(
            id = -1,
            title = "Item 1",
            link = "",
            creator = "creator",
            pubDate = 1736548,
            description = "Description text 1",
            descriptionHtml = "Description text HTML 1",
            imageUrl = "",
            bookmark = false,
            isRead = false,
            isSaved = false,
        ),
        Article(
            id = 1,
            title = "Item 2",
            link = "",
            creator = "creator",
            pubDate = 1736543,
            description = "Description text 2",
            descriptionHtml = "Description text HTML 2",
            imageUrl = "",
            bookmark = true,
            isRead = false,
            isSaved = false,
        )
    )
}