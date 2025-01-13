package ru.paylab.feature.infonavbar.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.paylab.core.model.ArticlesRepository
import ru.paylab.core.model.CategoriesRepository
import ru.paylab.feature.infonavbar.InformationViewModel
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InformationBarModule {
    @Provides
    @Singleton
    fun provideInformationViewModel(
        articlesRepository: ArticlesRepository,
        categoriesRepository: CategoriesRepository,
    ): InformationViewModel = InformationViewModel(
        articlesRepository = articlesRepository,
        categoriesRepository = categoriesRepository,
    )
}