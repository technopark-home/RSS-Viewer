package ru.paylab.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "ArticleCategoryLink",
    primaryKeys = ["articleId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = ArticlesEntity::class,
            parentColumns = ["id"],
            childColumns = ["articleId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = CategoriesEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["articleId"]),
        Index(value = ["categoryId"]),
    ],
)
data class ArticlesCategoriesCrossRef(
    @ColumnInfo(name = "articleId")
    val articleId: Int,
    @ColumnInfo(name = "categoryId")
    val categoryId: Int
)