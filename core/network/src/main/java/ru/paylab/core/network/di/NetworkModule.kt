package ru.paylab.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import ru.paylab.core.network.BuildConfig
import ru.paylab.core.network.NetworkDataSource
import ru.paylab.core.network.RetrofitNetwork
import ru.paylab.core.network.ServiceRSS
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkDataSource(
        serviceRSS: ServiceRSS
    ): NetworkDataSource = RetrofitNetwork(serviceRSS)

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(Settings.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Settings.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Settings.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Settings.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit):  ServiceRSS {
        return retrofit.create( ServiceRSS::class.java)
    }

    internal object Settings {
        const val CONNECT_TIMEOUT = 10L
        const val READ_TIMEOUT = 30L
        const val WRITE_TIMEOUT = 10L
        const val BASE_URL = "https://habr.com/ru/rss/articles/"
    }
}