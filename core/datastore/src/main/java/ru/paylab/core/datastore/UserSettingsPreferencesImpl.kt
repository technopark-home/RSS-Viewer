package ru.paylab.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class UserSettingsPreferencesImpl @Inject constructor(
    private val context: Context,
) : UserSettingsPreferences {

    override suspend fun clearSettings() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override fun getFieldFlow(nameString: String): Flow<String> =
        context.dataStore.data.map {  preferences ->
            preferences[stringPreferencesKey(nameString)] ?: ""
        }

    override suspend fun saveField(nameString: String, value: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(nameString)] = value
        }
    }

    companion object {
        private const val DATA_STORE_FILE_NAME: String = "userSettingsPref"
        private val Context.dataStore by preferencesDataStore(name = DATA_STORE_FILE_NAME)
    }
}

