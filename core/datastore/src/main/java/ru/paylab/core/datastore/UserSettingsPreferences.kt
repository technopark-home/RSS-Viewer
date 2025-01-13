package ru.paylab.core.datastore

import android.content.Context
import androidx.annotation.Keep
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class UserSettingsPreferences @Inject constructor(
    private val context: Context,
) : UserSettingsDataStore {
    override val url: Flow<String>
        get() = context.dataStore.data.map { preferences ->
                preferences[TUNING_URL] ?: RSS_URLS[0]
            }.distinctUntilChanged()
            .catch {
                RSS_URLS[0]
            }

    override suspend fun saveURL(url: String) {
        context.dataStore.edit { preferences ->
            preferences[TUNING_URL] = url
        }
    }

    override val useDynamicColor: Flow<Boolean>
        get() = context.dataStore.data.map { preferences ->
            preferences[TUNING_DYNAMIC_COLOR].toBoolean()
        }.distinctUntilChanged()

    override suspend fun saveDynamicColor(useDynamicColor: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[TUNING_DYNAMIC_COLOR] = useDynamicColor.toString()
        }
    }

    override val showByCategory: Flow<Boolean>
        get() = context.dataStore.data.map { preferences ->
            preferences[FILTER_SHOW_CATEGORY].toBoolean()
        }.distinctUntilChanged()

    override suspend fun saveShowByCategory(showByCategory: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[FILTER_SHOW_CATEGORY] = showByCategory.toString()
        }
    }

    override val filterSet: Flow<Set<FilterView>>
        get() = context.dataStore.data.map { preferences ->
            val jsonData = preferences[FILTER_SET] ?: ""
            if (jsonData.isNotEmpty()) {
                Gson().fromJson(jsonData, SettingFilterData::class.java).filterViews
            } else {
                mutableSetOf(FilterView.ALL, FilterView.FOR_YOU)
            }
        }.distinctUntilChanged()

    override suspend fun saveFilterSet(filterSet: Set<FilterView>) {
        context.dataStore.edit { preferences ->
            preferences[FILTER_SET] = Gson().toJson(SettingFilterData(filterSet)).toString()
        }
    }

    override val deleteDate: Flow<Long>
        get() = context.dataStore.data.map { preferences ->
            preferences[TUNING_DELETE]?.toLong() ?: 30
        }.distinctUntilChanged()

    override suspend fun saveDeleteDate(deleteDate: Long) {
        context.dataStore.edit { preferences ->
            preferences[TUNING_DELETE] = deleteDate.toString()
        }
    }

    override val colorMode:  Flow<ColorMode>
        get() = context.dataStore.data.map { preferences ->
            val jsonData = preferences[SETTING_COLOR_MODE] ?: ""
            if (jsonData.isNotEmpty()) {
                Gson().fromJson(jsonData, ColorModeData::class.java).colorMode
            } else {
                ColorMode.SYSTEM
            }
        }.distinctUntilChanged()

    override suspend fun saveColorMode(colorMode: ColorMode) {
        context.dataStore.edit { preferences ->
            preferences[SETTING_COLOR_MODE] = Gson().toJson(ColorModeData(colorMode)).toString()
        }
    }

    override suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override fun getURLS(): List<String> {
        return RSS_URLS
    }

    companion object {
        private const val DATA_STORE_FILE_NAME: String = "userSettingsPref"
        private val Context.dataStore by preferencesDataStore(name = DATA_STORE_FILE_NAME)

        private val TUNING_URL = stringPreferencesKey("tuning_url")
        private val TUNING_DYNAMIC_COLOR = stringPreferencesKey("tuning_dynamic_color")
        private val SETTING_COLOR_MODE = stringPreferencesKey("setting_color_mode")
        private val FILTER_SET = stringPreferencesKey("filter_set")
        private val TUNING_DELETE = stringPreferencesKey("tuning_delete")
        private val FILTER_SHOW_CATEGORY = stringPreferencesKey("filter_show_category")
        private val RSS_URLS = listOf(
            "https://habr.com/ru/rss/articles/",
            "https://rssexport.rbc.ru/rbcnews/news/30/full.rss",
            "https://meteoinfo.ru/rss/forecasts/index.php?s=27612")
    //https://informer.gismeteo.ru/rss/27612.xml
        //https://meteoinfo.ru/rss/forecasts/index.php?s=27612
    }
}

enum class ColorMode(val descriptor: String) {
    SYSTEM("System"),
    DARK("Dark"),
    LIGHT("Light"),;

    override fun toString(): String {
        return descriptor
    }

    companion object {
        fun getEnum(value: String): ColorMode {
            return entries.first { it.descriptor == value }
        }
    }
}

internal class ColorModeData(
    @Keep
    @SerializedName("colorMode")
    var colorMode: ColorMode,
)

internal class SettingFilterData(
    @Keep
    @SerializedName("filterViews")
    var filterViews: Set<FilterView>,
)