package ru.paylab.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.paylab.core.database.dao.ArticleDao
import ru.paylab.core.database.dao.ArticleFavoriteFtsDao
import ru.paylab.core.database.dao.CategoryDao
import ru.paylab.core.database.model.ArticlesCategoriesCrossRef
import ru.paylab.core.database.model.ArticlesEntity
import ru.paylab.core.database.model.ArticlesFts
import ru.paylab.core.database.model.CategoriesEntity
import ru.paylab.core.database.model.CategoriesEntityFts
import ru.paylab.core.database.model.InstantConverter
import javax.inject.Singleton

@Singleton
@Database(
    entities = [
        ArticlesEntity::class,
        ArticlesCategoriesCrossRef::class,
        CategoriesEntity::class,
        CategoriesEntityFts::class,
        ArticlesFts::class
               ],
    version = 13,
    exportSchema = false
)
@TypeConverters(
    InstantConverter::class,
)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticlesDao(): ArticleDao
    abstract fun getCategoriesDao(): CategoryDao
    abstract fun getArticleFavoriteFtsDao(): ArticleFavoriteFtsDao

    companion object {
        @Volatile
        private var INSTANCE: ArticleDatabase? = null

        private const val DB_NAME = "article_database.db"

        fun getDatabase(context: Context): ArticleDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context/*, scope*/).also {
                    INSTANCE = it
                }
            }
        }

        private fun buildDatabase(context: Context): ArticleDatabase {
            println("buildDatabase")
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, ArticleDatabase::class.java, DB_NAME)
                    .fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }

            //return Room.databaseBuilder(context, ArticleDatabase::class.java, DB_NAME)
//                .addCallback(object : Callback()
//                {
//                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        super.onCreate(db)
//                        println("Creade")
//                        scope.launch {
//                            INSTANCE?.let {
//                                for (article: ArticleFavorite in DataGenerator.getArticles()) {
//                                    it.getArticlesDao().insertArticle(
//                                        ArticleFavorite(
//                                            null,
//                                            article.title,
//                                            article.creator,
//                                            article.description,
//                                            article.pubDate,
//                                            article.link,
//                                        ) )
//                                }}}}})
            //.build()
    }
}