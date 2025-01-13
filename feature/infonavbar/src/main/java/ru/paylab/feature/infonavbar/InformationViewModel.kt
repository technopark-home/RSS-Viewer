package ru.paylab.feature.infonavbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.paylab.core.model.ArticlesRepository
import ru.paylab.core.model.CategoriesRepository
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(
    articlesRepository: ArticlesRepository,
    categoriesRepository: CategoriesRepository,
) : ViewModel() {

    private val _savedArticles:Flow<Long> = articlesRepository.getSavedArticle()
    val savedArticles: StateFlow<Long> = _savedArticles.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0,
    )
    private val _articleCount: Flow<Long> =
        articlesRepository.getArticlesCount()

    val articleCount: StateFlow<Long> = _articleCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0,
    )

    private val _articleBookmarkCount: Flow<Long> =
        articlesRepository.getArticlesBookmarkCount()

    val articleBookmarkCount: StateFlow<Long> = _articleBookmarkCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0,
    )

    private val _categoriesFavoriteCount: Flow<Long> =
        categoriesRepository.getCategoriesFavoriteCount()

    val categoriesFavoriteCount: StateFlow<Long> = _categoriesFavoriteCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0,
    )
}