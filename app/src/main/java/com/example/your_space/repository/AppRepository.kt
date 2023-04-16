package com.example.your_space.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.your_space.database.AppDao
import com.example.your_space.database.toDomainModel
import com.example.your_space.network.Network
import com.example.your_space.network.networkdatamodel.propertyModelToDatabaseModel
import com.example.your_space.ui.ourspaces.SpaceItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val database: AppDao) {

    val workingSpacesRepo: LiveData<List<SpaceItem>> = getWorkingSpaces()

    private fun getWorkingSpaces(): LiveData<List<SpaceItem>> {
        return database.gelAllWorkingSpaces().map { it.toDomainModel() }
    }

    suspend fun refreshWorkingSpaces() {
        try {
            val workingSpacesList = Network.NetworkServices.getAllWorkingSpaces()
            if (workingSpacesList.isNotEmpty()) {
                withContext(Dispatchers.IO) {
                    database.insertAll(*(workingSpacesList.propertyModelToDatabaseModel()))
                }
            }
        } catch (e: Exception) {

        }
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            database.deleteAllWorkingSpaces()
        }
    }
}