package ru.paylab.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity(tableName = "ArticlesTableFts")
@Fts4(contentEntity = ArticlesEntity::class)
data class ArticlesFts(
    @PrimaryKey @ColumnInfo(name = "rowid")
    val id: Int,

    @ColumnInfo(name = "article_title")
    val title: String,

    @ColumnInfo(name = "article_description")
    val description: String,
)