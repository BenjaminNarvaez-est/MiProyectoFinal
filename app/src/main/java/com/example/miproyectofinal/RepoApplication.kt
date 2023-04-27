

package com.example.android.miproyectofinal

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RepoApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())


    val database by lazy { RepoDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { RepoRepository(database.wordDao()) }
}
