package ru.paylab.core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.paylab.core.model.CategoriesRepository
import ru.paylab.core.model.UserSettingsRepository
import ru.paylab.core.model.data.Category
import ru.paylab.core.model.data.FilterView
import ru.paylab.core.model.data.TitleCategory
import ru.paylab.core.model.data.toTitleCategoryList
import javax.inject.Inject

class FollowedTopicsUseCase @Inject constructor(
    userDataRepository: UserSettingsRepository,
    private val categoriesRepository: CategoriesRepository,
) {
    private val _showByCategory: Flow<Boolean> = userDataRepository.showByCategory

    private val _filters: Flow<Set<FilterView>> = userDataRepository.filters

    private val _selectedCategoryPagingDataFlow: Flow<List<Category>> =
        categoriesRepository.getSelectedCategories()

    val titleCategory: Flow<List<TitleCategory>> = combine(
        _filters, _selectedCategoryPagingDataFlow, _showByCategory
    ) { filter, selectedCategory, isShowCategory ->
        filter.toMutableList().map {
            TitleCategory(
                id = it.ordinal,
                name = it.descriptor,
                type = it,
                categoryFavorite = false,
            )
        }.plus(
            if (isShowCategory) selectedCategory.toTitleCategoryList()
            else emptyList()
        )
    }.distinctUntilChanged()
}