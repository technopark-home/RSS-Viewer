package ru.paylab.app.rssviewer.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.paylab.core.model.ArticlesRepository
import ru.paylab.core.datastore.UserSettingsDataStore
import ru.paylab.core.localcache.LocalCache
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userSettingsDataStore: UserSettingsDataStore,
    private val articlesRepository: ArticlesRepository,
    private val localRepository: LocalCache,
): ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            articlesRepository.refresh()
            localRepository.refresh()
        }
    }

    val uiState: StateFlow<MainUiState> =
        combine(userSettingsDataStore.useDynamicColor, userSettingsDataStore.colorMode) {
            useDynamicColor, colorMode ->
        MainUiState.Success(useDynamicColor,colorMode )
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    private val _screenTitle: Flow<String> = articlesRepository.getTitleFeed()
    val screenTitle: StateFlow<String> = _screenTitle.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "",
    )

    fun saveImage(id: Int, url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.checkDocFile(id)
            if (url.isNotEmpty()) localRepository.saveImage(id, url)
            localRepository.refresh()
        }
    }

    fun clearSaved(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.clearSaved(id)
            localRepository.refresh()
        }
    }
}