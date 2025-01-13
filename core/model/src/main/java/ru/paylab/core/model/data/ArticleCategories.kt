package ru.paylab.core.model.data

data class ArticleCategories(
    val id: Int,
    val title: String,
    val description: String,
    val descriptionHtml: String,
    val link: String,
    val creator: String,
    val pubDate: Long,
    val imageUrl: String,
    val bookmark: Boolean,
    val isRead: Boolean,
    var isSaved: Boolean,
    val categories: List<Category>,
)
