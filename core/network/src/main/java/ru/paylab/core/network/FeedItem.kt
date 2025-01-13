package ru.paylab.core.network

data class FeedItem(
    val title: String,
    val link: String,
    val creator: String,
    val pubDate: String,
    val image: String,
    var description: String = "",
    var categories: List<String> = mutableListOf(),
)