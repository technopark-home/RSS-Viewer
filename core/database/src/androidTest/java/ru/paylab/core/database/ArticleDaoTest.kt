package ru.paylab.core.database

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.paylab.core.database.dao.ArticleDao
import ru.paylab.core.database.model.ArticleFavoriteBookmark
import ru.paylab.core.database.model.ArticleFavoriteIsRead
import ru.paylab.core.database.model.ArticlesEntity


class ArticleDaoTest {
    private lateinit var database: ArticleDatabase
    private lateinit var articleDao: ArticleDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ArticleDatabase::class.java).build()

        articleDao = database.getArticlesDao()
    }

    @After
    fun tearDown() {
        database.close()
    }
    @Test
    fun check_save_get_data() = runTest {
        articleDao.updatesArticle( articleEntities )

        val articlesDB = articleDao.getAllArticles().asFlow().first()
        val secondArticleDB = articleDao.getArticle(2).first()

        assertEquals(3, articlesDB.size)
        assertEquals(secondArticleDB?.title ?: "", articleEntities[1].title)
        assertEquals(secondArticleDB?.id, articleEntities[1].id)
        assertEquals(false, secondArticleDB?.isRead)
    }

    @Test
    fun check_upsert_get_data() = runTest {
        articleDao.updatesArticle( articleEntities )

        articleDao.updatesArticle( articleTestInsert )

        val articlesDB = articleDao.getAllArticles().asFlow().first()
        val secondArticleDB = articleDao.getArticle(2).first()

        assertEquals(3, articlesDB.size)
        assertEquals(secondArticleDB?.title ?: "", articleEntities[1].title)
        assertEquals(secondArticleDB?.id, articleEntities[1].id)
        assertEquals(false, secondArticleDB?.isRead)
    }

    @Test
    fun check_update_get_data() = runTest {
        articleDao.updatesArticle( articleEntities )

        articleDao.updateBookMark( ArticleFavoriteBookmark( 2, true ))
        articleDao.updateIsRead( ArticleFavoriteIsRead( 2, true ))

        val articlesDB = articleDao.getAllArticles().asFlow().first()
        val secondArticleDB = articleDao.getArticle(2).first()
        val countBookmark = articleDao.getBookmarkCountArticles().first()
        val countUnread = articleDao.getUnreadCountArticles().first()

        assertEquals(3, articlesDB.size)
        assertEquals(1, countBookmark)
        assertEquals(2, countUnread)
        assertEquals(secondArticleDB?.title ?: "", articleEntities[1].title)
        assertEquals(secondArticleDB?.id, articleEntities[1].id)
        assertEquals(true, secondArticleDB?.bookmark)
        assertEquals(true, secondArticleDB?.isRead)
    }

    @Test
    fun check_clear_data() = runTest {
        articleDao.updatesArticle(articleEntities)
        articleDao.clearArticle()
        val articlesDB = articleDao.getAllArticles().asFlow().first()

        assertEquals(0, articlesDB.size)
    }


    private val articleEntities: List<ArticlesEntity> = listOf(
        ArticlesEntity(
            id=1,
            title = "First",
            creator = "",
            link = "",
            isRead = false,
            pubDate = 0,
            description = "First",
            imageUrl = "",
            bookmark = false,
            descriptionHtml = "" ),
        ArticlesEntity(
            id=2,
            title = "Second",
            creator = "",
            link = "",
            isRead = false,
            pubDate = 0,
            description = "Second",
            imageUrl = "",
            bookmark = false,
            descriptionHtml = "" ),
        ArticlesEntity(
            id=3,
            title = "Third",
            creator = "",
            link = "",
            isRead = false,
            pubDate = 0,
            description = "Third",
            imageUrl = "",
            bookmark = false,
            descriptionHtml = "" ),
    )

    private val articleTestInsert: List<ArticlesEntity> = listOf(
        ArticlesEntity(
            id=2,
            title = "Second item ignore",
            creator = "",
            link = "",
            isRead = true,
            pubDate = 12,
            description = "Second item ignore",
            imageUrl = "",
            bookmark = true,
            descriptionHtml = "" ),
    )
}