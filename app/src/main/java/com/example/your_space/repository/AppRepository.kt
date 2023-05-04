package com.example.your_space.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.your_space.database.*
import com.example.your_space.network.Network.NetworkServices
import com.example.your_space.network.networkdatamodel.BookingPropertyPost
import com.example.your_space.network.networkdatamodel.bookingPropertyModelToDatabaseModel
import com.example.your_space.network.networkdatamodel.propertyModelToDatabaseModel
import com.example.your_space.network.networkdatamodel.roomPropertyModelToDatabaseModel
//import com.example.your_space.ui.booking.BookItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val REPOSITORY_ERROR_STRING = "Error in repository"

class AppRepository private constructor(private val database: AppDao) {

    val workingSpacesRepo: LiveData<List<WorkingSpaceDB>> = database.gelAllWorkingSpaces()

    val bookingsRepo: LiveData<List<BookingDB>> = database.gelAllBookings()

//    val roomsRepo: LiveData<List<SpaceRoomDB>> = database.getAllRooms()


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
                if (initialize) {
                    database.deleteAllWorkingSpaces()
                }
                database.insertAllWorkingSpaces(*(workingSpacesList.propertyModelToDatabaseModel()))
            }
        } catch (e: Exception) {
            Log.e(REPOSITORY_ERROR_STRING, e.stackTraceToString())
        }
    }

    suspend fun refreshBookings() {
        try {
            withContext(Dispatchers.IO) {
                val bookingsList = NetworkServices.getAllBookings()
                database.deleteAllBookings()
                database.insertAllBookings(*(bookingsList.bookingPropertyModelToDatabaseModel()))
            }
        } catch (e: Exception) {
            Log.e(REPOSITORY_ERROR_STRING, e.stackTraceToString())
        }
    }

    suspend fun getRoomsBySpaceIdFromNetwork(spaceId: String) {
        try {
            withContext(Dispatchers.IO) {
                val roomsList = NetworkServices.getRoomsBySpaceId(spaceId)
                database.deleteAllRoomsWithSpaceId(spaceId)
                database.insertAllRooms(*(roomsList.roomPropertyModelToDatabaseModel()))
            }
        } catch (e: Exception) {
            Log.e(REPOSITORY_ERROR_STRING, e.stackTraceToString())
        }
    }

    suspend fun getRoomsBySpaceIdFromDB(spaceId: String): LiveData<List<SpaceRoomDB>> {
        return withContext(Dispatchers.IO) {
            database.getAllRoomsWithSpaceId(spaceId)
        }
    }

    suspend fun deleteAllWorkingSpaces() {
        withContext(Dispatchers.IO) {
            database.deleteAllWorkingSpaces()
        }
    }

    suspend fun addNewBooking(newBooking : BookingPropertyPost){
        val response = NetworkServices.addNewBooking(newBooking)
        if (response.isSuccessful){
            refreshBookings()
        }
        else Log.e("RETROFIT_ERROR", response.code().toString())
    }

    suspend fun deleteBookingWithId(bookItem: BookingDB) {
        try {
            withContext(Dispatchers.IO) {
                // Do the DELETE request and get response
                val response = NetworkServices.cancelBooking(bookItem.bookingId.toInt())
                if (response.isSuccessful) {
                    database.deleteBooking(bookItem.bookingId)

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

    suspend fun deleteAllRooms() {
        withContext(Dispatchers.IO) {
            database.deleteAllRooms()
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