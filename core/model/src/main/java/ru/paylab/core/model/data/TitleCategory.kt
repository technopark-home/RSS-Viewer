package ru.paylab.core.model.data

fun Category.toTitleCategory(): TitleCategory {
    return TitleCategory(
        id = id,
        name = name,
        type = FilterView.CATEGORIES,
        categoryFavorite = categoryFavorite,
    )
}

fun List<Category>.toTitleCategoryList(): List<TitleCategory> {
    return map { category ->
        category.toTitleCategory()
    }
}

data class TitleCategory(
    val id: Int,
    val name: String,
    val type: FilterView,
    val categoryFavorite: Boolean,
)