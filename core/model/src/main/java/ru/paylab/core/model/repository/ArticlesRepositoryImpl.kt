package ru.paylab.core.model.repository

import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.paylab.core.database.dao.ArticleDao
import ru.paylab.core.database.dao.CategoryDao
import ru.paylab.core.database.model.ArticleFavoriteBookmark
import ru.paylab.core.database.model.ArticleFavoriteIsRead
import ru.paylab.core.database.model.ArticlesEntity
import ru.paylab.core.database.model.CategoriesEntity
import ru.paylab.core.database.model.CategoryFavorite
import ru.paylab.core.model.utils.toArticleCategories
import ru.paylab.core.database.util.asEntity
import ru.paylab.core.database.util.categoryCrossReferences
import ru.paylab.core.database.util.categoryEntityShells
import ru.paylab.core.model.utils.toArticle
import ru.paylab.core.model.utils.toArticleList
import ru.paylab.core.model.utils.toCategoriesList
import ru.paylab.core.datastore.UserSettingsDataStore
import ru.paylab.core.localcache.LocalCache
import ru.paylab.core.model.data.Article
import ru.paylab.core.model.data.ArticleCategories
import ru.paylab.core.model.data.Category
import ru.paylab.core.network.ArticleFeed
import ru.paylab.core.network.FeedItem
import ru.paylab.core.network.NetworkDataSource
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ArticlesRepositoryImpl @Inject constructor(
    private val rssService: NetworkDataSource,
    private val articleDao: ArticleDao,
    private val categoryDao: CategoryDao,
    private val settingsRepository: UserSettingsDataStore,
    private val localCacheRepository: LocalCache,
) : ru.paylab.core.model.ArticlesRepository {

    private val _url = settingsRepository.url
    private val dateForDelete = settingsRepository.deleteDate
    private val savedIds = localCacheRepository.getSavedIds()

    override fun getAllArticles(): Flow<List<Article>> = combine(
        articleDao.getAllArticles().asFlow(), savedIds
    ) { article, ids ->
        article.toArticleList(ids)
    }

    override fun getBookmarkArticles(): Flow<List<Article>> = combine(
        articleDao.getArticlesBookmark(true).asFlow(), savedIds
    ) { article, ids ->
        article.toArticleList(ids)
    }

    override fun getUnreadArticles(): Flow<List<Article>> = combine(
        articleDao.getArticlesUnread(false).asFlow(), savedIds
    ) { article, ids ->
        article.toArticleList(ids)
    }

    override fun getSelectedCategories(): Flow<List<Category>> =
        categoryDao.getSelectedCategoryEntities().
        map { categories ->
            categories.toCategoriesList()
        }.asFlow()

    override fun getArticlesByCategoryAll(): Flow<List<ArticleCategories>> =
        combine(
            articleDao.getArticlesFavorite(),
            localCacheRepository.getSavedIds()
        ) { article, ids ->
            article.toArticleCategories(ids)
        }

    override fun getAllArticlesCategory(): Flow<List<ArticleCategories>> =
        combine(
            articleDao.getAllArticlesResource(),
            localCacheRepository.getSavedIds()
        ) { article, ids ->
            article.toArticleCategories(ids)
        }

    override fun getArticlesWithCategory(id: Int): Flow<ArticleCategories?> =
        articleDao.getArticleWithCategories(id)
            .map { articlesEntity ->
                articlesEntity?.toArticleCategories(localCacheRepository.checkSavedId(id))
            }

    override fun getUnreadCountArticles(): Flow<Long> = articleDao.getUnreadCountArticles()

    override suspend fun updateArticle( id: Int, isBookmark: Boolean) {
        try {
            articleDao.updateBookMark(ArticleFavoriteBookmark(id, isBookmark))
        } catch (_: Throwable) {
        }
    }

    override suspend fun updateCategory(id: Int, isFavorite: Boolean) {
        try {
            categoryDao.updateFavorite(CategoryFavorite(id, isFavorite))
        } catch (_: Throwable) {
        }
    }

    override suspend fun updateIsRead(id: Int, isRead: Boolean) {
        articleDao.updateIsRead(ArticleFavoriteIsRead(id, isRead))
    }

    override fun getArticlesCount(): Flow<Long> =
        articleDao.getArticlesCount()

    override fun getArticlesBookmarkCount(): Flow<Long> =
        articleDao.getBookmarkCountArticles()

    private val _feedTitle = MutableStateFlow("")
    override fun getTitleFeed():Flow<String> = _feedTitle
    //override val feedTitle: SharedFlow<String> = _feedTitle

    private suspend fun setFeedTitle(feedTitle: String) {
        println("TITLE: $feedTitle")
        _feedTitle.emit(feedTitle)
    }

    override suspend fun refresh() = withContext(Dispatchers.IO) {
        try {
            val useUrl = _url.firstOrNull() ?: ""
            if(useUrl.isEmpty())
                return@withContext

            val articlesFeed = fetchArticlesFeed(useUrl)
            val feedItems = articlesFeed.feedItem

            categoryDao.insertOrIgnoreCategories(
                topicEntities = feedItems.map(FeedItem::categoryEntityShells).flatten()
                    .distinctBy(CategoriesEntity::id),
            )
            updateDatabaseArticles(feedItems.map {
                it.asEntity()
            })
            articleDao.insertOrIgnoreArticleFavoriteCategoriesCrossRef(
                articleCategoryCrossReferences = feedItems.map(FeedItem::categoryCrossReferences)
                    .distinct().flatten(),
            )

            val date = dateForDelete.firstOrNull()?: 30
            val time = Instant.now().epochSecond - date*24*60*60
            val ids = savedIds.firstOrNull()?: emptySet()
            articleDao.deleteOldArticles( time, ids)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun updateDatabaseArticles(articleEntities: List<ArticlesEntity>) =
        coroutineScope {
            articleDao.updatesArticle(articleEntities)
        }

    private suspend fun fetchArticlesFeed( url: String ): ArticleFeed = coroutineScope {
        val articleFeeds = rssService.getFeed(url)

        setFeedTitle(articleFeeds.feedTitle)
        println("****************** FINISH MOD ***********************")
        println("articleFeeds MOD: ${articleFeeds.feedItem[0].categories.size}")
        println("articleFeeds MOD: ${articleFeeds.feedItem.size}")
        println("RSS MOD: ${articleFeeds.feedItem.size}")

        return@coroutineScope articleFeeds
    }

    ////////////////// ****************** Local cache ***********************
    override fun getArticle(articleId: Int): Flow<Article> = articleDao.getArticle(articleId)
        .map { article ->
            article!!.toArticle(localCacheRepository.checkSavedId(articleId))
        }

    override fun getAllCategories(): Flow<List<Category>> = categoryDao.getCategoryEntities()
        .asFlow()
        .map { categories ->
            categories.toCategoriesList()
        }

    override suspend fun clearArticle() {
        articleDao.clearArticle()
    }

    override suspend fun clearSaved(id: Int) =
        with(localCacheRepository) {
            clearSaved(id)
            refresh()
        }

    override suspend fun clearAll() {
        localCacheRepository.clearLocalCache()
        localCacheRepository.refresh()
        categoryDao.deleteAllCategories()
        clearArticle()
        refresh()
    }
}