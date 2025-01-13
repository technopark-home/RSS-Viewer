package ru.paylab.core.network

data class ArticleFeed(
    val feedItem: List<FeedItem>,
    val feedTitle: String,
)