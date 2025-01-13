package ru.paylab.core.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RetrofitNetwork @Inject constructor(): NetworkDataSource {
    private val networkApi = Retrofit.Builder().baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ServiceRSS::class.java)

    override suspend fun getFeed(url: String): ArticleFeed {
        val call: Call<ResponseBody?>? = networkApi.getFeed(url)
        val response = call?.execute()
        if( response?.code() == 200 ) {
            val articleFeeds = FeedParser().parse(response.body()?.string() ?: "")
            /*println("****************** FINISH NETWORK ***********************")
            println("articleFeeds2 3: ${articleFeeds.feedItem[0].categories.size}")
            println("articleFeeds2 3: ${articleFeeds.feedItem.size}")
            println("RSS: ${articleFeeds.feedItem.size}")*/
            return articleFeeds
        }
        throw IllegalArgumentException("Unable get feed ${response?.code()}")
    }
    companion object {
        private const val BASE_URL = "https://habr.com/ru/rss/articles/"
    }
}