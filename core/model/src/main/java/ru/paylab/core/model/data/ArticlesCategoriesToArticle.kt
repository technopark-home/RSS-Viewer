package ru.paylab.core.model.data

fun ArticleCategories.toArticle(isSaved: Boolean): Article {
    return Article(
        id = link.hashCode(),
        title = title,
        link = link,
        creator = creator,
        pubDate = pubDate,
        descriptionHtml = descriptionHtml,
        description = description,
        isRead = isRead,
        bookmark = bookmark,
        imageUrl = imageUrl,
        isSaved = isSaved,
    )
}