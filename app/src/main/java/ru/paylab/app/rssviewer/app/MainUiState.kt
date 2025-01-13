package ru.paylab.app.rssviewer.app

import ru.paylab.core.model.data.ColorMode

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(val useDynamicColor: Boolean, val colorMode: ColorMode) : MainUiState
}