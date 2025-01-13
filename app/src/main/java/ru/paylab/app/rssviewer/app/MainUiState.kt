package ru.paylab.app.rssviewer.app

import ru.paylab.core.datastore.ColorMode

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(val useDynamicColor: Boolean, val colorMode: ColorMode) : MainUiState
}