package ru.paylab.core.model.utils

import ru.paylab.core.database.model.ArticlesEntity
import ru.paylab.core.model.data.Article


fun ArticlesEntity.toArticle(isSaved: Boolean): Article {
    return Article(
        id = link.hashCode(),
        title = title,
        link = link,
        creator = creator,
        pubDate = pubDate,
        descriptionHtml = descriptionHtml,
        description = description,
        isSaved = isSaved,
        isRead = isRead,
        bookmark = bookmark,
        imageUrl = imageUrl,
    )
}

fun List<ArticlesEntity>.toArticleList( ids: Set<Int>): List<Article> {
    return map { articleEntity ->
        articleEntity.toArticle(ids.contains(articleEntity.id))
    }
}