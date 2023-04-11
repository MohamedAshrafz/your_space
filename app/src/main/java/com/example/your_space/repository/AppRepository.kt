package com.example.your_space.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.your_space.database.AppDao
import com.example.your_space.database.WorkingSpaceDB
import com.example.your_space.database.toDomainModel
import com.example.your_space.ui.ourspaces.SpaceItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val database: AppDao) {

    val workingSpacesRepo: LiveData<List<SpaceItem>> = getWorkingSpaces()

    private fun getWorkingSpaces(): LiveData<List<SpaceItem>> {
        return database.gelAllWorkingSpaces().map { it.toDomainModel() }
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