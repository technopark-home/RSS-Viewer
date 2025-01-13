package ru.paylab.core.model

import kotlinx.coroutines.flow.Flow
import ru.paylab.core.model.data.Category

interface CategoriesRepository {
    fun getAllCategories(): Flow<List<Category>>

    suspend fun updateCategory(categoryId: Int, isFavorite:Boolean)

    fun getCategoriesSearch(query: String): Flow<List<Category>>

    fun getSelectedCategories(): Flow<List<Category>>

    suspend fun clearAllCategories()

    fun getCategoriesFavoriteCount(): Flow<Long>
}