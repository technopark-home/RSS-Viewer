package ru.paylab.core.model

import kotlinx.coroutines.flow.Flow
import ru.paylab.core.model.data.Article
import ru.paylab.core.model.data.ArticleCategories
import ru.paylab.core.model.data.Category

interface ArticlesRepository {
    fun getTitleFeed():Flow<String>

    fun getAllArticles(): Flow<List<Article>>

    fun getBookmarkArticles(): Flow<List<Article>>

    fun getUnreadArticles(): Flow<List<Article>>


    fun getSelectedCategories(): Flow<List<Category>>

    fun getArticlesByCategoryAll(): Flow<List<ArticleCategories>>

    fun getAllArticlesCategory(): Flow<List<ArticleCategories>>

    fun getArticlesWithCategory(id: Int): Flow<ArticleCategories?>

    fun getUnreadCountArticles(): Flow<Long>
    suspend fun updateArticle( id: Int, isBookmark: Boolean)

    suspend fun updateCategory(id: Int, isFavorite: Boolean)

    suspend fun updateIsRead(id: Int, isRead: Boolean)
    fun getArticlesCount(): Flow<Long>
    fun getArticlesBookmarkCount(): Flow<Long>
    fun getAllCategories(): Flow<List<Category>>
    suspend fun clearSaved(id: Int)

    suspend fun refresh()

    fun getArticle(articleId: Int): Flow<Article>

    suspend fun clearArticle()
    suspend fun clearAll()
}