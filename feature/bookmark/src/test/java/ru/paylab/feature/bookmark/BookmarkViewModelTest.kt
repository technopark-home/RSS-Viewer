package ru.paylab.feature.bookmark

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import ru.paylab.core.model.ArticlesRepository
import ru.paylab.core.model.data.Article
import ru.paylab.core.model.data.ArticleCategories

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherBookmarkRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {

    override fun starting(description: Description) = Dispatchers.setMain(testDispatcher)

    override fun finished(description: Description) = Dispatchers.resetMain()
}

class BookmarkViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherBookmarkRule()

    class FakeArticlesRepositoryImpl : ArticlesRepository {
        private val flowArticles = MutableSharedFlow<List<Article>>()
        override fun getTitleFeed(): Flow<String> {
            TODO("Not yet implemented")
        }

        override fun getAllArticles(): Flow<List<Article>> {
            TODO("Not yet implemented")
        }

        override fun getBookmarkArticles(): Flow<List<Article>> = flowArticles.map {
                articles ->
                articles.filter { article ->
                    article.bookmark
                }
            }

        override fun getUnreadArticles(): Flow<List<Article>> {
            TODO("Not yet implemented")
        }

        override suspend fun updateArticle(id: Int, isBookmark: Boolean) {
            _articles.replaceAll { article ->
                if( article.id == id) {
                    article.copy(
                        bookmark = isBookmark,
                    )
                } else
                    article
            }
            flowArticles.emit( _articles )
        }

        override suspend fun updateIsRead(id: Int, isRead: Boolean) {
            _articles.replaceAll { article ->
                if( article.id == id) {
                    article.copy(
                        isRead = isRead,
                    )
                } else
                    article
            }
            flowArticles.emit( _articles )
        }

        override suspend fun clearSaved(id: Int) {
            TODO("Not yet implemented")
        }

        override suspend fun refreshLocalCache() {
            TODO("Not yet implemented")
        }

        override fun getAllArticlesCategory(): Flow<List<ArticleCategories>> {
            TODO("Not yet implemented")
        }

        override fun getArticlesWithCategory(id: Int): Flow<ArticleCategories?> {
            TODO("Not yet implemented")
        }

        override fun getSavedArticles(): Flow<List<Article>> {
            TODO("Not yet implemented")
        }

        override fun getUnreadCountArticles(): Flow<Long> {
            TODO("Not yet implemented")
        }

        override fun getArticlesByCategoryAll(): Flow<List<ArticleCategories>> {
            TODO("Not yet implemented")
        }

        override suspend fun clearAll() {
            TODO("Not yet implemented")
        }

        override fun prepareSaveDoc(id: Int): String {
            TODO("Not yet implemented")
        }

        override fun getSavedDocFileName(id: Int): String {
            TODO("Not yet implemented")
        }

        override fun getImageFileName(id: Int): String {
            TODO("Not yet implemented")
        }

        override fun getSavedArticle(): Flow<Long> {
            TODO("Not yet implemented")
        }

        override suspend fun saveImage(id: Int, url: String) {
            TODO("Not yet implemented")
        }

        override fun getArticlesCount(): Flow<Long> {
            TODO("Not yet implemented")
        }

        override fun getArticlesBookmarkCount(): Flow<Long> {
            TODO("Not yet implemented")
        }

        override suspend fun refresh() {
            flowArticles.emit( _articles.toList() )
        }

        override fun getArticle(articleId: Int): Flow<Article> {
            TODO("Not yet implemented")
        }

        override suspend fun clearArticle() {
            TODO("Not yet implemented")
        }

        private var _articles: MutableList<Article> = mutableListOf()
        fun setArticles( articles: MutableList<Article>) {
            _articles.clear()
            _articles.addAll(articles)
        }
    }

    private val articlesRepository:FakeArticlesRepositoryImpl = FakeArticlesRepositoryImpl()
    private lateinit var viewModel: BookmarkViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        viewModel = BookmarkViewModel(
            articlesRepository = articlesRepository,
            coroutineDispatcher = UnconfinedTestDispatcher()
        )
    }

    @After
    fun tearDown() {
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getItemsBookMark() = runTest {
        articlesRepository.setArticles(sampleArticles)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.itemsBookmark.collect{}
        }
        assertEquals(viewModel.itemsBookmark.value, emptyList<List<Article>>())
        viewModel.refresh()
        assertEquals(viewModel.itemsBookmark.value.size, 2)
        viewModel.setBookmark( sampleArticles[0].id, false)
        assertEquals(viewModel.itemsBookmark.value.size, 1)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun refresh() = runTest {
        articlesRepository.setArticles(sampleArticles)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.itemsBookmark.collect{}
        }
        assertEquals(viewModel.itemsBookmark.value, emptyList<List<Article>>())
        viewModel.refresh()
        assertEquals(viewModel.itemsBookmark.value.size, 2)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun markAsBookMark() = runTest {
        articlesRepository.setArticles(sampleArticles)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.itemsBookmark.collect{}
        }
        assertEquals(viewModel.itemsBookmark.value, emptyList<List<Article>>())
        viewModel.refresh()
        assertEquals(viewModel.itemsBookmark.value.size, 2)
        viewModel.setBookmark( sampleArticles[0].id, false)
        assertEquals(viewModel.itemsBookmark.value.size, 1)
        viewModel.setBookmark( sampleArticles[1].id, false)
        assertEquals(viewModel.itemsBookmark.value.size, 0)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun markIsRead() = runTest {
        articlesRepository.setArticles(sampleArticles)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.itemsBookmark.collect{}
        }
        assertEquals(viewModel.itemsBookmark.value, emptyList<List<Article>>())
        viewModel.refresh()
        assertEquals(viewModel.itemsBookmark.value.size, 2)
        viewModel.markIsRead( sampleArticles[0].id, true)
        assertEquals(viewModel.itemsBookmark.first()[0].isRead, true)
        viewModel.markIsRead( sampleArticles[1].id, false)
        assertEquals(viewModel.itemsBookmark.first()[1].isRead, false)
    }
}

private val sampleArticles = mutableListOf(
    Article(
        id = -1,
        title = "Item 1",
        link = "",
        creator = "creator",
        pubDate = 1736548,
        description = "Description text 1",
        descriptionHtml = "Description text HTML 1",
        imageUrl = "",
        bookmark = true,
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
        isRead = true,
        isSaved = false,
    )
)
