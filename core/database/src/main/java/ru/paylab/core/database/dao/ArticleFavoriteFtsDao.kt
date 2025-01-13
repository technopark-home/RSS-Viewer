package ru.paylab.core.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import ru.paylab.core.database.model.ArticlesEntity
import ru.paylab.core.database.model.ArticlesFts

@Dao
interface ArticleFavoriteFtsDao {
    @Query( value = """
        SELECT * FROM ArticlesTable
            JOIN ArticlesTableFts
                ON ( ArticlesTable.id == ArticlesTableFts.rowid )
                    WHERE ArticlesTableFts MATCH :text
                        GROUP BY ArticlesTable.id"""
    )
    fun getArticlesSearch(text: String):  LiveData<List<ArticlesEntity>>

    @Query( value = """
        SELECT ArticlesTableFts.rowid as rowid,
            snippet(ArticlesTableFts, '<b>', '</b>', '...', 0, 15) as article_title,
            snippet(ArticlesTableFts, '<b>', '</b>', '...', 1, 15) as article_description
                FROM ArticlesTableFts
                LEFT JOIN ArticlesTable
                ON ArticlesTableFts.rowid = ArticlesTable.id
                    WHERE ArticlesTableFts MATCH :text
                    ORDER BY ArticlesTable.article_pubDate DESC
        """
    )
    fun getArticlesSearchFlow(text: String): List<ArticlesFts>
}