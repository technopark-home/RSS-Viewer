package ru.paylab.feature.downloadedarticle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.paylab.core.localcache.LocalCache
import javax.inject.Inject

@HiltViewModel
internal class SavedArticleViewModel @Inject constructor(
    private var localRepository: LocalCache,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val selectedArticleId: Int = checkNotNull(savedStateHandle["ArticlesId"])

    fun getDocFileName():String {
        return localRepository.savedDocFileName(selectedArticleId)
    }

    fun getSelectedArticleId():Int {
        return selectedArticleId
    }
}