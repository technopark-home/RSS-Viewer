package ru.paylab.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CategoriesTable")
data class CategoriesEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    val categoryName: String,
    val categoryFavorite: Boolean,
)