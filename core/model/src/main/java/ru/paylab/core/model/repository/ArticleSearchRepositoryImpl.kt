package ru.paylab.core.model.repository

import ru.paylab.core.database.dao.ArticleFavoriteFtsDao
import ru.paylab.core.model.utils.toArticleListFts
import ru.paylab.core.model.ArticleSearchRepository
import ru.paylab.core.model.data.ArticleSearch
import javax.inject.Inject

internal class ArticleSearchRepositoryImpl @Inject constructor(
    private val articleSearch: ArticleFavoriteFtsDao,
) : ArticleSearchRepository {
    override fun getArticlesSearch(query: String): List<ArticleSearch> =
        articleSearch.getArticlesSearchFlow(query).toArticleListFts()
}