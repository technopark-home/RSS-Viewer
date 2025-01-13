package ru.paylab.core.localcache.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.paylab.core.localcache.LocalCache
import ru.paylab.core.localcache.LocalCacheImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalCacheModule {

    @Singleton
    @Provides
    fun provideLocalCache(
        @ApplicationContext context: Context
    ): LocalCache = LocalCacheImpl(context)
}