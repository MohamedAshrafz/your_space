package com.example.your_space.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.your_space.database.AppDao
import com.example.your_space.database.AppDatabase
import com.example.your_space.database.bookingToDomainModel
import com.example.your_space.database.spaceToDomainModel
import com.example.your_space.network.Network.NetworkServices
import com.example.your_space.network.networkdatamodel.bookingProertyModelToDatabaseModel
import com.example.your_space.network.networkdatamodel.propertyModelToDatabaseModel
import com.example.your_space.ui.booking.BookItem
import com.example.your_space.ui.ourspaces.SpaceItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val REPOSITORY_ERROR_STRING = "Error in repository"

class AppRepository private constructor(private val database: AppDao) {

    val workingSpacesRepo: LiveData<List<SpaceItem>> =
        database.gelAllWorkingSpaces().map { it.spaceToDomainModel() }

    val BookingsRepo: LiveData<List<BookItem>> =
        database.gelAllBookings().map { it.bookingToDomainModel() }


    suspend fun refreshWorkingSpaces() {
        try {
            val workingSpacesList = NetworkServices.getAllWorkingSpaces()
            if (workingSpacesList.isNotEmpty()) {
                withContext(Dispatchers.IO) {
                    database.insertAllWorkingSpaces(*(workingSpacesList.propertyModelToDatabaseModel()))
                }
            }
        } catch (e: Exception) {
            Log.e(REPOSITORY_ERROR_STRING, e.stackTraceToString())
        }
    }

    suspend fun loadWorkingSpacesOfPage(pageNumber: Int) {
        try {
            val workingSpacesList = NetworkServices.getWorkingSpacesUsingPaging(pageNumber)
            if (workingSpacesList.isNotEmpty()) {
                withContext(Dispatchers.IO) {
                    database.insertAllWorkingSpaces(*(workingSpacesList.propertyModelToDatabaseModel()))
                }
            }
        } catch (e: Exception) {
            Log.e(REPOSITORY_ERROR_STRING, e.stackTraceToString())
        }
    }

    suspend fun refreshBookings() {
        try {
            val bookingsList = NetworkServices.getAllBookings()
            if (bookingsList.isNotEmpty()) {
                withContext(Dispatchers.IO) {
                    database.insertAllBookings(*(bookingsList.bookingProertyModelToDatabaseModel()))
                }
            }
        } catch (e: Exception) {
            Log.e(REPOSITORY_ERROR_STRING, e.stackTraceToString())
        }
    }

    suspend fun deleteAllWorkingSpaces() {
        withContext(Dispatchers.IO) {
            database.deleteAllWorkingSpaces()
        }
    }

    suspend fun deleteBookingWithId(bookItem: BookItem) {
        try {
            // Do the DELETE request and get response
            val response = NetworkServices.cancelBooking(bookItem.bookId.toInt())
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    database.deleteBooking(bookItem.bookId)

                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        } catch (e: Exception) {
            Log.e(REPOSITORY_ERROR_STRING, e.stackTraceToString())
        }

    }

    suspend fun deleteAllBookings() {
        withContext(Dispatchers.IO) {
            database.deleteAllBookings()
        }
    }

    companion object {
        @Volatile
        private var repositoryINSTANCE: AppRepository? = null

        fun getInstance(context: Context): AppRepository {
            synchronized(this) {
                var localInstance = repositoryINSTANCE

                if (localInstance == null) {
                    val appDao = AppDatabase.getInstance(context).dao
                    localInstance = AppRepository(appDao)

                    repositoryINSTANCE = localInstance
                }
                return localInstance
            }
        }
    }
}