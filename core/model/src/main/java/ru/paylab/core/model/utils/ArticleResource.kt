package ru.paylab.core.model.utils

import ru.paylab.core.database.model.ArticleResource
import ru.paylab.core.database.model.CategoriesEntity
import ru.paylab.core.model.data.ArticleCategories

fun List<ArticleResource>.toArticleCategories(localSavedIds: Set<Int>): List<ArticleCategories> {
    return map { articleResource ->
        articleResource.toArticleCategories(localSavedIds.contains(articleResource.article.id))
    }
}

fun ArticleResource.toArticleCategories(isSaved: Boolean) = ArticleCategories(
    id = article.id,
    title = article.title,
    description = article.description,
    link = article.link,
    descriptionHtml = article.descriptionHtml,
    creator = article.creator,
    pubDate = article.pubDate,
    bookmark = article.bookmark,
    isRead = article.isRead,
    isSaved = isSaved,
    imageUrl = article.imageUrl,
    categories = category.map(CategoriesEntity::asExternalModel),
)