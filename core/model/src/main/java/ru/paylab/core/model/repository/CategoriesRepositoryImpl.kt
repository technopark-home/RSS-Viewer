package ru.paylab.core.model.repository

import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import kotlinx.coroutines.flow.Flow
import ru.paylab.core.database.dao.CategoryDao
import ru.paylab.core.database.model.CategoryFavorite
import ru.paylab.core.model.utils.toCategoriesList
import ru.paylab.core.model.data.Category
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
) : ru.paylab.core.model.CategoriesRepository {
    override fun getAllCategories(): Flow<List<Category>> =
        categoryDao.getCategoryEntities().map { categories ->
            categories.toCategoriesList()
        }.asFlow()

    override suspend fun updateCategory(categoryId: Int, isFavorite:Boolean) {
        try {
            categoryDao.updateFavorite(CategoryFavorite(categoryId, isFavorite))
        } catch (err: Throwable) {
            //println("Error err: ${err.message}")
        }
    }

    override fun getCategoriesSearch(query: String): Flow<List<Category>> =
        categoryDao.getQueryCategoryEntities(query).map {
            it.toCategoriesList()
        }.asFlow()

    override fun getSelectedCategory(): Flow<List<Category>> =
        categoryDao.getSelectedCategoryEntities().map { categories ->
            categories.toCategoriesList()
        }.asFlow()

    override suspend fun clearAllCategories() {
        categoryDao.deleteAllCategories()
    }

    override fun getCategoriesFavoriteCount(): Flow<Long> =
        categoryDao.getCategoriesFavoriteCount()
}