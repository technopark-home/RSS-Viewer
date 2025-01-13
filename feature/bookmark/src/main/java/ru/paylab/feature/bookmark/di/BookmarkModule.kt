package ru.paylab.feature.bookmark.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import ru.paylab.core.model.ArticlesRepository
import ru.paylab.feature.bookmark.BookmarkViewModel
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookmarkModule {
    @Provides
    @Singleton
    fun provideBookmarkViewModel(
        articlesRepository: ArticlesRepository,
    ): BookmarkViewModel = BookmarkViewModel(
        articlesRepository = articlesRepository,
        coroutineDispatcher = Dispatchers.IO
    )
}