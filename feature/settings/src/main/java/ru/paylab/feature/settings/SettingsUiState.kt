package ru.paylab.feature.settings

import ru.paylab.core.model.data.ColorMode
import ru.paylab.core.model.data.FilterView

internal sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data object Error : SettingsUiState
    data class Success(
        val colorMode: ColorMode,
        val useDynamicColor: Boolean,
        val showByCategory: Boolean,
        val filterSet: Set<FilterView>,
        val url: String,
        val deleteOldData: Long,) : SettingsUiState
}