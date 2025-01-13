package ru.paylab.core.datastore

import kotlinx.coroutines.flow.Flow

interface UserSettingsPreferences {
    suspend fun clearSettings(): Unit

    fun getFieldFlow(nameString: String): Flow<String>

    suspend fun saveField(nameString: String, value: String)
}