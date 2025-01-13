package ru.paylab.core.model.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.paylab.core.datastore.UserSettingsPreferences
import ru.paylab.core.model.UserSettingsRepository
import ru.paylab.core.model.data.ColorMode
import ru.paylab.core.model.data.FilterView
import javax.inject.Inject

class UserSettingsRepositoryImpl @Inject constructor(
    private val userSettingsPreferences: UserSettingsPreferences,
) : UserSettingsRepository {
    override val url:Flow<String> = userSettingsPreferences.getFieldFlow(TUNING_URL).map {
            it.ifEmpty { RSS_URLS[0] }
        }.distinctUntilChanged()
        .catch { emit(RSS_URLS[0]) }

    override suspend fun saveURL(url: String) =
        userSettingsPreferences.saveField(TUNING_URL, url)

    override val useDynamicColor: Flow<Boolean> = userSettingsPreferences.getFieldFlow(TUNING_DYNAMIC_COLOR).map {
        it.toBoolean()
    }.distinctUntilChanged()

    override suspend fun saveDynamicColor(useDynamicColor: Boolean) =
        userSettingsPreferences.saveField(TUNING_DYNAMIC_COLOR, useDynamicColor.toString())

    override val showByCategory: Flow<Boolean> = userSettingsPreferences.getFieldFlow(FILTER_SHOW_CATEGORY).map {
        it.toBoolean()
    }.distinctUntilChanged()

    override suspend fun saveShowByCategory(showByCategory: Boolean) =
        userSettingsPreferences.saveField(FILTER_SHOW_CATEGORY, showByCategory.toString())

    private val itemTypeFilterView = object : TypeToken<Set<FilterView>>() {}.type
    override val filters: Flow<Set<FilterView>> = userSettingsPreferences.getFieldFlow(FILTER_SET)
        .map{ filters ->
            val filtersItem = Gson().fromJson<Set<FilterView>>(filters, itemTypeFilterView)
            filtersItem?: setOf(FilterView.ALL, FilterView.FOR_YOU)
        }.catch { emit(setOf(FilterView.ALL, FilterView.FOR_YOU)) }.distinctUntilChanged()

    override suspend fun saveFilters(filters: Set<FilterView>) {
        val jsonData = Gson().toJson(filters).toString()
        userSettingsPreferences.saveField( FILTER_SET, jsonData)
    }

    override val deleteDay: Flow<Long> = userSettingsPreferences.getFieldFlow(TUNING_DELETE).map {
        it.toLong()
    }.catch { emit(30.toLong()) }.
    distinctUntilChanged()

    override suspend fun saveDeleteDay(deleteDay: Long) =
        userSettingsPreferences.saveField(FILTER_SHOW_CATEGORY, deleteDay.toString())

    override val colorMode: Flow<ColorMode> = userSettingsPreferences.getFieldFlow(SETTING_COLOR_MODE).map {
            ColorMode.getEnum(it)
        }.distinctUntilChanged()
        .catch { emit(ColorMode.SYSTEM) }

    override suspend fun saveColorMode(colorMode: ColorMode) =
        userSettingsPreferences.saveField(SETTING_COLOR_MODE, colorMode.descriptor)

    override suspend fun clearSettings() { userSettingsPreferences.clearSettings() }

    override fun getURLS(): List<String> = RSS_URLS

    companion object {
        private const val TUNING_URL = "tuning_url"
        private const val TUNING_DYNAMIC_COLOR = "tuning_dynamic_color"
        private const val SETTING_COLOR_MODE = "setting_color_mode"
        private const val FILTER_SET = "filter_set"
        private const val TUNING_DELETE = "tuning_delete"
        private const val FILTER_SHOW_CATEGORY = "filter_show_category"

        private val RSS_URLS = listOf(
            "https://habr.com/ru/rss/articles/",
            "https://rssexport.rbc.ru/rbcnews/news/30/full.rss",
            "https://meteoinfo.ru/rss/forecasts/index.php?s=27612"
        )
    }
}