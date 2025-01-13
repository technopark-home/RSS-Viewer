package ru.paylab.core.model

import kotlinx.coroutines.flow.Flow
import ru.paylab.core.model.data.Article
import ru.paylab.core.model.data.ArticleCategories

interface ArticlesRepository {
    fun getTitleFeed():Flow<String>

    fun getAllArticles(): Flow<List<Article>>

    fun getBookmarkArticles(): Flow<List<Article>>

    fun getUnreadArticles(): Flow<List<Article>>

    fun getArticlesByCategoryAll(): Flow<List<ArticleCategories>>

    fun getAllArticlesCategory(): Flow<List<ArticleCategories>>

    fun getArticlesWithCategory(id: Int): Flow<ArticleCategories?>
    fun getSavedArticles(): Flow<List<Article>>

    fun getUnreadCountArticles(): Flow<Long>
    suspend fun updateArticle( id: Int, isBookmark: Boolean)



    suspend fun updateIsRead(id: Int, isRead: Boolean)
    fun getArticlesCount(): Flow<Long>
    fun getArticlesBookmarkCount(): Flow<Long>

    suspend fun refresh()

    fun getArticle(articleId: Int): Flow<Article>
    suspend fun clearArticle()
    suspend fun clearAll()
    fun prepareSaveDoc(id: Int):String
    fun getSavedDocFileName(id: Int):String
    fun getImageFileName(id: Int):String
    fun getSavedArticle():Flow<Long>
    suspend fun saveImage(id: Int, url: String)
    suspend fun clearSaved(id: Int)
    suspend fun refreshLocalCache()
}