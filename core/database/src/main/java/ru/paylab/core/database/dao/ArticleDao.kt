package ru.paylab.core.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.paylab.core.database.model.ArticleFavoriteBookmark
import ru.paylab.core.database.model.ArticleFavoriteIsRead
import ru.paylab.core.database.model.ArticleResource
import ru.paylab.core.database.model.ArticlesCategoriesCrossRef
import ru.paylab.core.database.model.ArticlesEntity

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticle(article : ArticlesEntity)

    @Delete
    suspend fun deleteArticle(article: ArticlesEntity)

    @Update
    suspend fun updateArticle(article: ArticlesEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updatesArticle(articles: List<ArticlesEntity>)

    @Query(value = "SELECT * FROM ArticlesTable ORDER BY article_pubDate DESC")
    fun getAllArticles(): LiveData<List<ArticlesEntity>>

    @Query("SELECT * FROM ArticlesTable WHERE isBookmark = :articleBookmark ORDER BY article_pubDate DESC")
    fun getArticlesBookmark(articleBookmark: Boolean): LiveData<List<ArticlesEntity>>

    @Query("SELECT * FROM ArticlesTable WHERE isRead = :articleIsRead ORDER BY article_pubDate DESC")
    fun getArticlesUnread(articleIsRead: Boolean): LiveData<List<ArticlesEntity>>

    @Query(value = "SELECT * FROM ArticlesTable WHERE id = :articleId LIMIT 1")
    fun getArticle(articleId: Int): Flow<ArticlesEntity?>

    @Transaction
    @Query(value = "SELECT * FROM ArticlesTable WHERE id = :articleId LIMIT 1")
    fun getArticleWithCategories(articleId: Int): Flow<ArticleResource?>

    @Query("DELETE FROM ArticlesTable")
    suspend fun clearArticle()

    @Query("DELETE FROM ArticlesTable WHERE id = :articleId")
    suspend fun deleteArticleById(articleId: Int)

    @Query("DELETE FROM ArticlesTable WHERE article_pubDate < :dateOld AND id NOT IN (:filterArticleIds)")
    suspend fun deleteOldArticles( dateOld: Long, filterArticleIds: Set<Int> = emptySet(),)

    @Update(entity = ArticlesEntity::class)
    suspend fun updateBookMark(bookmark: ArticleFavoriteBookmark)

    @Update(entity = ArticlesEntity::class)
    suspend fun updateIsRead(isRead: ArticleFavoriteIsRead)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreArticleFavoriteCategoriesCrossRef(
        articleCategoryCrossReferences: List<ArticlesCategoriesCrossRef>,
    )

    @Query("SELECT count(*) FROM ArticlesTable WHERE isRead = 0")
    fun getUnreadCountArticles(): Flow<Long>

    @Query("SELECT count(*) FROM ArticlesTable WHERE isBookmark = 1")
    fun getBookmarkCountArticles(): Flow<Long>

    @Query("SELECT count(*) FROM ArticlesTable")
    fun getArticlesCount(): Flow<Long>

    @Transaction
    @Query(
        value = """
            SELECT * FROM ArticlesTable
            WHERE  ArticlesTable.id IN
                        (
                            SELECT articleId FROM ArticleCategoryLink
                            WHERE ArticleCategoryLink.categoryId IN (
                            SELECT CategoriesTable.id FROM CategoriesTable WHERE CategoriesTable.categoryFavorite = 1
                            )
                        )
            ORDER BY article_pubDate DESC
    """,
    )
    fun getArticlesFavorite(): Flow<List<ArticleResource>>

    @Transaction
    @Query(
        value = """
            SELECT * FROM ArticlesTable
            ORDER BY article_pubDate DESC
    """,
    )
    fun getAllArticlesResource(): Flow<List<ArticleResource>>

    @Transaction
    @Query(
        value = """
            SELECT * FROM ArticlesTable
            WHERE 
                CASE WHEN :useFilterArticleIds
                    THEN ArticlesTable.id IN (:filterArticleIds)
                    ELSE 1
                END
             AND
                CASE WHEN :useFilterCategoryIds
                    THEN ArticlesTable.id IN
                        (
                            SELECT articleId FROM ArticleCategoryLink
                            WHERE categoryId IN (:filterCategoryIds)
                        )
                    ELSE 1
                END
            ORDER BY article_pubDate DESC
    """,
    )
    fun getArticleResource(
        useFilterCategoryIds: Boolean = false,
        filterCategoryIds: Set<Int> = emptySet(),
        useFilterArticleIds: Boolean = false,
        filterArticleIds: Set<Int> = emptySet(),
    ): LiveData<List<ArticleResource>>
}