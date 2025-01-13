package ru.paylab.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4

@Entity(tableName = "CategoriesTableFts")
@Fts4(contentEntity = CategoriesEntity::class)
data class CategoriesEntityFts(
    @ColumnInfo(name = "id")
    val id: Int,
    val categoryName: String,
)