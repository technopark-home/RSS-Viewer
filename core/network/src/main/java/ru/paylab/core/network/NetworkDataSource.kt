package ru.paylab.core.network

interface NetworkDataSource {
    suspend fun getFeed( url: String ): ArticleFeed
}