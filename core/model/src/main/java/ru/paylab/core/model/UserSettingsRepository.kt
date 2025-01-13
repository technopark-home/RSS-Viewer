package ru.paylab.core.model

import kotlinx.coroutines.flow.Flow
import ru.paylab.core.model.data.ColorMode
import ru.paylab.core.model.data.FilterView

interface UserSettingsRepository {
    val url: Flow<String>

    suspend fun saveURL(url: String)

    val useDynamicColor: Flow<Boolean>

    suspend fun saveDynamicColor(useDynamicColor: Boolean)

    val showByCategory: Flow<Boolean>

    suspend fun saveShowByCategory(showByCategory: Boolean)
    val filters: Flow<Set<FilterView>>

    suspend fun saveFilters(filters: Set<FilterView>)

    val deleteDay: Flow<Long>

    suspend fun saveDeleteDay(deleteDay: Long)

    val colorMode: Flow<ColorMode>

    suspend fun saveColorMode(colorMode: ColorMode)

    suspend fun clearSettings()

    fun getURLS(): List<String>

}