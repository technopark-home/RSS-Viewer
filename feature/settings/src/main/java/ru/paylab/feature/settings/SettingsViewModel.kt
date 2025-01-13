package ru.paylab.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.paylab.core.model.ArticlesRepository
import ru.paylab.core.model.UserSettingsRepository
import ru.paylab.core.model.data.ColorMode
import ru.paylab.core.model.data.FilterView
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserSettingsRepository,
    private val articlesRepository: ArticlesRepository,
) : ViewModel() {

    private val _refresh = MutableStateFlow(false)

    val uiState: StateFlow<SettingsUiState> = combine(
        combine(
            userDataRepository.colorMode,
            userDataRepository.useDynamicColor,
            userDataRepository.showByCategory,
            ::Triple,
        ),
        combine(
            userDataRepository.deleteDay,
            userDataRepository.url,
            userDataRepository.filters,
            ::Triple,
        ),
        _refresh
    ) { first, second , isLoad ->
        if(isLoad)
            SettingsUiState.Loading
        else {
            SettingsUiState.Success(
                colorMode = first.first,
                useDynamicColor = first.second,
                showByCategory = first.third,
                filterSet = second.third,
                url = second.second,
                deleteOldData = second.first
            )
        }
    }.catch { _ ->
            SettingsUiState.Error
        }.stateIn(
            scope = viewModelScope,
            initialValue = SettingsUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    fun getURLs(): List<String> {
        return userDataRepository.getURLS()
    }

    fun getIndexURL(value: String): Int {
        val currentIndex = userDataRepository.getURLS().indexOf(value)
        return if (currentIndex == -1) 0 else currentIndex
    }

    fun setURLs(url: String) {
        _refresh.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try{
                userDataRepository.saveURL(url)
                articlesRepository.clearAll()
            } finally {
                _refresh.value = false
            }
        }
    }

    fun saveFilterSet( filterSet: Set<FilterView> ) {
        viewModelScope.launch(Dispatchers.IO) {
            userDataRepository.saveFilters(filterSet)
        }
    }
    fun saveShowByCategory(showByCategory: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userDataRepository.saveShowByCategory(showByCategory)
        }
    }

    fun saveColorMode(colorMode: ColorMode) {
        viewModelScope.launch(Dispatchers.IO) {
            userDataRepository.saveColorMode(colorMode)
        }
    }

    fun  saveDynamicColor(useDynamicColor: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userDataRepository.saveDynamicColor(useDynamicColor)
        }
    }

    fun  saveDeleteDate(deleteDate: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            userDataRepository.saveDeleteDay(deleteDate)
        }
    }
}