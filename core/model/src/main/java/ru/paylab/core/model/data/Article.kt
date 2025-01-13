package ru.paylab.core.model.data

data class Article(
    val id: Int,
    val title: String,
    val link: String,
    val creator: String,
    val pubDate: Long,
    val description: String,
    val descriptionHtml: String,
    val imageUrl: String,
    val bookmark: Boolean,
    val isRead: Boolean,
    val isSaved: Boolean,
)