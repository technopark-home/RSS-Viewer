package ru.paylab.core.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ArticleResource(
    @Embedded
    val article: ArticlesEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ArticlesCategoriesCrossRef::class,
            parentColumn = "articleId",
            entityColumn = "categoryId",
        ),
    )
    val category: List<CategoriesEntity>,
)