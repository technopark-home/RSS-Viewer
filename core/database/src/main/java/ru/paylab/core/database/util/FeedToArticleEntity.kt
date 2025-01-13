package ru.paylab.core.database.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import ru.paylab.core.database.model.ArticlesCategoriesCrossRef
import ru.paylab.core.database.model.ArticlesEntity
import ru.paylab.core.database.model.CategoriesEntity
import ru.paylab.core.network.FeedItem

fun FeedItem.categoryCrossReferences(): List<ArticlesCategoriesCrossRef> =
    categories.map { category ->
        ArticlesCategoriesCrossRef(
            articleId = link.hashCode(),
            categoryId = category.hashCode(),
        )
    }

fun FeedItem.categoryEntityShells() = categories.map { category ->
    CategoriesEntity(
        id = category.hashCode(),
        categoryName = category,
        categoryFavorite = false,
    )
}

fun FeedItem.asEntity() = ArticlesEntity(
    id = link.hashCode(),
    title = title,
    descriptionHtml = description,
    description = AnnotatedString.fromHtml(
        description.replace(
            "<img.+/(img)*>".toRegex(),
            ""
        )
    ).text,
    link = link,
    imageUrl = image,
    pubDate = UtilsDateTime.parsePubDateStringToLong(pubDate),
    creator = creator,
    bookmark = false,
    isRead = false,
)