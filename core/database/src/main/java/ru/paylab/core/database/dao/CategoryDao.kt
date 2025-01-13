package ru.paylab.core.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.paylab.core.database.model.CategoriesEntity
import ru.paylab.core.database.model.CategoryFavorite

@Dao
interface CategoryDao {
    @Query(
        value = """
        SELECT * FROM CategoriesTable
        WHERE id = :topicId
    """,
    )
    fun getCategoryEntity(topicId: Int): Flow<CategoriesEntity>

    @Query(value = "SELECT * FROM CategoriesTable")
    fun getCategoryEntities(): LiveData<List<CategoriesEntity>>

    @Query(value = "SELECT * FROM CategoriesTable WHERE categoryFavorite=1")
    fun getSelectedCategoryEntities(): LiveData<List<CategoriesEntity>>

    @Query(value = "SELECT COUNT(*) FROM CategoriesTable WHERE categoryFavorite=1")
    fun getCategoriesFavoriteCount(): Flow<Long>

    @Query(value = "SELECT * FROM CategoriesTable WHERE categoryName LIKE '%' || :query || '%'")
    fun getQueryCategoryEntities(query: String): LiveData<List<CategoriesEntity>>

    @Query(
        value = """
        SELECT * FROM CategoriesTable
        WHERE id IN (:ids)
    """,
    )
    fun getCategoryEntities(ids: Set<Int>): Flow<List<CategoriesEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreCategories(topicEntities: List<CategoriesEntity>): List<Long>

    @Upsert
    suspend fun upsertCategories(entities: List<CategoriesEntity>)

    @Query(
        value = """
            DELETE FROM CategoriesTable
            WHERE id in (:ids)
        """,
    )
    suspend fun deleteCategories(ids: List<String>)

    @Query(value = "DELETE FROM CategoriesTable")
    suspend fun deleteAllCategories()

    @Update(entity = CategoriesEntity::class)
    suspend fun updateFavorite(bookMark: CategoryFavorite)
}