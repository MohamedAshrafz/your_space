package com.example.your_space.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.liveData
import com.example.your_space.database.AppDao
import com.example.your_space.database.WorkingSpaceDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val database: AppDao) {

    val workingSpacesRepo: LiveData<List<WorkingSpaceDB>> = getWorkingSpaces()

    private fun getWorkingSpaces(): LiveData<List<WorkingSpaceDB>> {
        return database.gelAllWorkingSpaces()
    }

    suspend fun refreshWorkingSpaces(list: List<WorkingSpaceDB>) {
        withContext(Dispatchers.IO) {
            database.insertAll(*(list.toTypedArray()))
        }
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            database.deleteAllWorkingSpaces()
        }
    }
}