package ru.paylab.core.database.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.paylab.core.database.dao.ArticleDao
import ru.paylab.core.database.ArticleDatabase
import ru.paylab.core.database.dao.ArticleFavoriteFtsDao
import ru.paylab.core.database.dao.CategoryDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun providesArticleDatabase(
        @ApplicationContext context: Context,
    ): ArticleDatabase = ArticleDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun providesArticleDao(
        database: ArticleDatabase,
    ): ArticleDao = database.getArticlesDao()

    @Provides
    @Singleton
    fun providesCategoriesDao(
        database: ArticleDatabase,
    ): CategoryDao = database.getCategoriesDao()

    @Provides
    @Singleton
    fun providesArticleFavoriteFtsDao(
        database: ArticleDatabase,
    ): ArticleFavoriteFtsDao = database.getArticleFavoriteFtsDao()
}