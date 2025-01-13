package ru.paylab.core.model.utils

import ru.paylab.core.database.model.ArticlesFts
import ru.paylab.core.model.data.ArticleSearch

fun ArticlesFts.toArticleFts(): ArticleSearch {
    return ArticleSearch(
        id = id,
        title = title,
        description = description,
    )
}

fun List<ArticlesFts>.toArticleListFts(): List<ArticleSearch> {
    return map { articleEntity ->
        articleEntity.toArticleFts()
    }
}