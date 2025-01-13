package ru.paylab.core.model.utils

import ru.paylab.core.database.model.CategoriesEntity
import ru.paylab.core.model.data.Category

fun CategoriesEntity.asExternalModel() = Category(
    id = id,
    name = categoryName,
    categoryFavorite = categoryFavorite,
)

fun List<CategoriesEntity>.toCategoriesList(): List<Category> {
    return map { categoryEntity ->
        categoryEntity.asExternalModel()
    }
}