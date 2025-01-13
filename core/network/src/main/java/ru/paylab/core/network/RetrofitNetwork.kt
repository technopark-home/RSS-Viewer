package ru.paylab.core.network

import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RetrofitNetwork @Inject constructor(
    private val networkApi:ServiceRSS
): NetworkDataSource {
    override suspend fun getFeed(url: String): ArticleFeed {
        val call: Call<ResponseBody?>? = networkApi.getFeed(url)
        val response = call?.execute()
        if( response?.code() == 200 ) {
            val articleFeeds = FeedParser().parse(response.body()?.string() ?: "")
            return articleFeeds
        }
        throw IllegalArgumentException("Unable get feed ${response?.code()}")
    }
}