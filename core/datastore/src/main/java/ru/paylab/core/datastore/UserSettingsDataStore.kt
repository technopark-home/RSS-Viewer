package ru.paylab.core.datastore

import kotlinx.coroutines.flow.Flow

interface UserSettingsDataStore {
    val url: Flow<String>

    suspend fun saveURL(url: String)

    val useDynamicColor: Flow<Boolean>

    suspend fun saveDynamicColor(useDynamicColor: Boolean)

    val showByCategory: Flow<Boolean>

    suspend fun saveShowByCategory(showByCategory: Boolean)

    val filterSet: Flow<Set<FilterView>>

    suspend fun saveFilterSet(filterSet: Set<FilterView>)

    val deleteDate: Flow<Long>

    suspend fun saveDeleteDate(deleteDate: Long)

    val colorMode: Flow<ColorMode>

    suspend fun saveColorMode(colorMode: ColorMode)

    suspend fun clear()

    fun getURLS(): List<String>
}