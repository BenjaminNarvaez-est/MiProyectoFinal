
package com.example.android.miproyectofinal

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow


class RepoRepository(private val repoDao: RepoDao) {


    val allWords: Flow<List<Word>> = repoDao.getAlphabetizedWords()


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Word) {
        repoDao.insert(word)
    }
}
