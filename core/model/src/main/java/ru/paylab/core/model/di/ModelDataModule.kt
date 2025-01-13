package ru.paylab.core.model.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.paylab.core.model.ArticlesRepository
import ru.paylab.core.model.repository.ArticlesRepositoryImpl
import ru.paylab.core.database.dao.ArticleDao
import ru.paylab.core.database.dao.ArticleFavoriteFtsDao
import ru.paylab.core.database.dao.CategoryDao
import ru.paylab.core.datastore.UserSettingsPreferences
import ru.paylab.core.localcache.LocalCache
import ru.paylab.core.model.ArticleSearchRepository
import ru.paylab.core.model.CategoriesRepository
import ru.paylab.core.model.UserSettingsRepository
import ru.paylab.core.model.repository.ArticleSearchRepositoryImpl
import ru.paylab.core.model.repository.CategoriesRepositoryImpl
import ru.paylab.core.model.repository.UserSettingsRepositoryImpl
import ru.paylab.core.network.NetworkDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ModelDataModule {

    @Provides
    @Singleton
    fun providesUserSettingsRepository(
        userSettingsPreferences: UserSettingsPreferences,
    ): UserSettingsRepository = UserSettingsRepositoryImpl(
        userSettingsPreferences = userSettingsPreferences,
    )

    @Provides
    @Singleton
    fun providesCategoriesRepository(
        categoryDao: CategoryDao,
    ): CategoriesRepository = CategoriesRepositoryImpl(
        categoryDao = categoryDao,
    )

    @Provides
    @Singleton
    fun providesArticleSearchRepository(
        articleFavoriteFtsDao: ArticleFavoriteFtsDao,
    ): ArticleSearchRepository = ArticleSearchRepositoryImpl(
        articleSearch = articleFavoriteFtsDao,
    )

    @Provides
    @Singleton
    fun providesArticlesRepository(
        rssService: NetworkDataSource,
        articleDao: ArticleDao,
        categoryDao: CategoryDao,
        userSettingsRepository: UserSettingsRepository,
        localCacheRepository: LocalCache,
    ): ArticlesRepository = ArticlesRepositoryImpl(
        rssService = rssService,
        articleDao = articleDao,
        categoryDao = categoryDao,
        settingsRepository = userSettingsRepository,
        localCacheRepository = localCacheRepository,
    )

}