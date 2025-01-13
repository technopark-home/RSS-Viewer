package ru.paylab.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ArticlesTable")
data class ArticlesEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "article_title")
    val title: String,

    @ColumnInfo(name = "article_creator")
    val creator: String,

    @ColumnInfo(name = "article_pubDate")
    val pubDate: Long,

    @ColumnInfo(name = "article_description")
    val description: String,

    @ColumnInfo(name = "article_descriptionHtml")
    val descriptionHtml: String,

    @ColumnInfo(name = "article_link")
    val link: String,

    @ColumnInfo(name = "isBookmark")
    val bookmark: Boolean,

    @ColumnInfo(name = "isRead")
    val isRead: Boolean,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String,
)