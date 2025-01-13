package ru.paylab.core.datastore.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.paylab.core.datastore.UserSettingsPreferences
import ru.paylab.core.datastore.UserSettingsPreferencesImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Singleton
    @Provides
    fun provideUserUserSettingsPreferences(
        @ApplicationContext context: Context
    ): UserSettingsPreferences = UserSettingsPreferencesImpl(context)
}