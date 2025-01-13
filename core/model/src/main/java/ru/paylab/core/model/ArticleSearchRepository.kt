package ru.paylab.core.model

import ru.paylab.core.model.data.ArticleSearch

interface ArticleSearchRepository {
    fun getArticlesSearch(query: String): List<ArticleSearch>
}