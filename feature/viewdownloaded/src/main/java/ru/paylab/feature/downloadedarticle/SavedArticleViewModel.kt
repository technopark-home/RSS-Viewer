package ru.paylab.feature.downloadedarticle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.paylab.core.model.ArticlesRepository
import javax.inject.Inject

@HiltViewModel
internal class SavedArticleViewModel @Inject constructor(
    private var articlesRepository: ArticlesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val selectedArticleId: Int = checkNotNull(savedStateHandle["ArticlesId"])

    fun getDocFileName():String {
        return articlesRepository.getSavedDocFileName(selectedArticleId)
    }

    fun getSelectedArticleId():Int {
        return selectedArticleId
    }
}