package ru.paylab.core.database

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.paylab.core.database.dao.CategoryDao
import ru.paylab.core.database.model.CategoriesEntity
import ru.paylab.core.database.model.CategoryFavorite

class CategoryDaoTest {
    private lateinit var database: ArticleDatabase
    private lateinit var categoryDao: CategoryDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ArticleDatabase::class.java).build()

        categoryDao = database.getCategoriesDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    private val categoriesTestUpsert: List<CategoriesEntity> = listOf(
        CategoriesEntity( 1, "First", false),
        CategoriesEntity( 2, "Second", false),
        CategoriesEntity( 3, "Third", true),
    )
    private val categoriesTestInsert: List<CategoriesEntity> = listOf(
        CategoriesEntity( 2, "Second item ignore", true),
    )

    @Test
    fun check_save_get_data() = runTest {
        categoryDao.upsertCategories(categoriesTestUpsert)

        val categories = categoryDao.getCategoryEntities().asFlow().first()
        val category = categoryDao.getCategoryEntity(2).first()

        assertEquals(3, categories.size)
        assertEquals(category.categoryName, categoriesTestUpsert[1].categoryName)
        assertEquals(category.id, categoriesTestUpsert[1].id)
        assertEquals(false, category.categoryFavorite)
    }

    @Test
    fun check_update_select_data() = runTest {
        categoryDao.upsertCategories(categoriesTestUpsert)
        categoryDao.insertOrIgnoreCategories(categoriesTestInsert)
        val categories = categoryDao.getCategoryEntities().asFlow().first()
        val category = categoryDao.getCategoryEntities(setOf(2)).first()[0]

        assertEquals(3, categories.size)
        assertEquals(category.categoryName, categoriesTestUpsert[1].categoryName)
        assertEquals(category.id, categoriesTestUpsert[1].id)
        assertEquals(false, category.categoryFavorite)
    }

    @Test
    fun check_make_favorite_select_data() = runTest {
        categoryDao.upsertCategories(categoriesTestUpsert)
        categoryDao.updateFavorite( CategoryFavorite( 2, true) )
        val categories = categoryDao.getCategoryEntities().asFlow().first()
        val category = categoryDao.getCategoryEntity(2).first()

        assertEquals(3, categories.size)
        assertEquals(category.categoryName, categoriesTestUpsert[1].categoryName)
        assertEquals(category.id, categoriesTestUpsert[1].id)
        assertEquals(true, category.categoryFavorite)
    }

    @Test
    fun check_clear_data() = runTest {
        categoryDao.upsertCategories(categoriesTestUpsert)
        categoryDao.deleteAllCategories()
        val categories = categoryDao.getCategoryEntities().asFlow().first()

        assertEquals(0, categories.size)
    }
}