package ru.paylab.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.paylab.core.network.NetworkDataSource
import ru.paylab.core.network.RetrofitNetwork
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkDataSource(): NetworkDataSource = RetrofitNetwork()
}