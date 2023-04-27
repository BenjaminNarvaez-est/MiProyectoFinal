

package com.example.android.miproyectofinal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [Word::class], version = 1)
abstract class RepoDatabase : RoomDatabase() {

    abstract fun wordDao(): RepoDao

    companion object {
        @Volatile
        private var INSTANCE: RepoDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): RepoDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RepoDatabase::class.java,
                    "word_database"
                )

                    .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance

                instance
            }
        }

        private class WordDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.wordDao())
                    }
                }
            }
        }


        suspend fun populateDatabase(repoDao: RepoDao) {

            repoDao.deleteAll()

            var word = Word("Hello")
            repoDao.insert(word)
            word = Word("World!")
            repoDao.insert(word)
        }
    }
}
