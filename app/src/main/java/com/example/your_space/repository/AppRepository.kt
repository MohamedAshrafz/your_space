package com.example.your_space.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.your_space.database.AppDao
import com.example.your_space.database.AppDatabase
import com.example.your_space.database.WorkingSpaceDB
import com.example.your_space.database.bookingToDomainModel
import com.example.your_space.network.Network.NetworkServices
import com.example.your_space.network.networkdatamodel.bookingProertyModelToDatabaseModel
import com.example.your_space.network.networkdatamodel.propertyModelToDatabaseModel
import com.example.your_space.ui.booking.BookItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val REPOSITORY_ERROR_STRING = "Error in repository"

class AppRepository private constructor(private val database: AppDao) {

    val workingSpacesRepo: LiveData<List<WorkingSpaceDB>> =
        database.gelAllWorkingSpaces()

    val BookingsRepo: LiveData<List<BookItem>> =
        database.gelAllBookings().map { it.bookingToDomainModel() }


    suspend fun refreshWorkingSpaces() {
        try {
            withContext(Dispatchers.IO) {
                val workingSpacesList = NetworkServices.getAllWorkingSpaces()
                if (workingSpacesList.isNotEmpty()) {
                    database.insertAllWorkingSpaces(*(workingSpacesList.propertyModelToDatabaseModel()))
                }
            }
        } catch (e: Exception) {
            Log.e(REPOSITORY_ERROR_STRING, e.stackTraceToString())
        }
    }

    suspend fun loadWorkingSpacesOfPage(pageNumber: Int, initialize: Boolean) {
//        val start = pageNumber * PAGE_SIZE
//        val end = start + PAGE_SIZE
        try {
            withContext(Dispatchers.IO) {
                val workingSpacesList = NetworkServices.getWorkingSpacesUsingPaging(pageNumber)
                if (workingSpacesList.isNotEmpty()) {
                    if (initialize){
                        database.deleteAllWorkingSpaces()
                    }
                    database.insertAllWorkingSpaces(*(workingSpacesList.propertyModelToDatabaseModel()))
                }
            }
        } catch (e: Exception) {
            Log.e(REPOSITORY_ERROR_STRING, e.stackTraceToString())
        }
    }

    suspend fun refreshBookings() {
        try {
                withContext(Dispatchers.IO) {
                    val bookingsList = NetworkServices.getAllBookings()
                    if (bookingsList.isNotEmpty()) {
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
            withContext(Dispatchers.IO) {
                // Do the DELETE request and get response
                val response = NetworkServices.cancelBooking(bookItem.bookId.toInt())
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