package com.example.your_space.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.your_space.database.AppDao
import com.example.your_space.database.AppDatabase
import com.example.your_space.database.bookingToDomainModel
import com.example.your_space.database.spaceToDomainModel
import com.example.your_space.network.Network
import com.example.your_space.network.Network.NetworkServices
import com.example.your_space.network.networkdatamodel.bookingProertyModelToDatabaseModel
import com.example.your_space.network.networkdatamodel.propertyModelToDatabaseModel
import com.example.your_space.ui.booking.BookItem
import com.example.your_space.ui.ourspaces.SpaceItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepository private constructor(private val database: AppDao) {

    val workingSpacesRepo: LiveData<List<SpaceItem>> =
        database.gelAllWorkingSpaces().map { it.spaceToDomainModel() }

    val BookingsRepo: LiveData<List<BookItem>> =
        database.gelAllBookings().map { it.bookingToDomainModel() }


    suspend fun refreshWorkingSpaces() {
        try {
            val workingSpacesList = Network.NetworkServices.getAllWorkingSpaces()
            if (workingSpacesList.isNotEmpty()) {
                withContext(Dispatchers.IO) {
                    database.insertAllWorkingSpaces(*(workingSpacesList.propertyModelToDatabaseModel()))
                }
            }
        } catch (e: Exception) {

        }
    }

    suspend fun loadWorkingSpacesOfPage(pageNumber: Int) {
        try {
            val workingSpacesList = Network.NetworkServices.getWorkingSpacesUsingPaging(pageNumber)
            if (workingSpacesList.isNotEmpty()) {
                withContext(Dispatchers.IO) {
                    database.insertAllWorkingSpaces(*(workingSpacesList.propertyModelToDatabaseModel()))
                }
            }
        } catch (e: Exception) {

        }
    }

    suspend fun refreshBookings() {
        try {
            val bookingsList = Network.NetworkServices.getAllBookings()
            if (bookingsList.isNotEmpty()) {
                withContext(Dispatchers.IO) {
                    database.insertAllBookings(*(bookingsList.bookingProertyModelToDatabaseModel()))
                }
            }
        } catch (e: Exception) {

        }
    }

    suspend fun deleteAllWorkingSpaces() {
        withContext(Dispatchers.IO) {
            database.deleteAllWorkingSpaces()
        }
    }

    suspend fun deleteBookingWithId(bookItem: BookItem) {
            // Do the DELETE request and get response
            val response = NetworkServices.cancelBooking(bookItem.bookId.toInt())
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    database.deleteBooking(bookItem.bookId)
                    refreshBookings()

                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
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