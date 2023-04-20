package com.example.your_space.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.your_space.database.AppDao
import com.example.your_space.database.bookingToDomainModel
import com.example.your_space.database.spaceToDomainModel
import com.example.your_space.network.Network
import com.example.your_space.network.networkdatamodel.bookingProertyModelToDatabaseModel
import com.example.your_space.network.networkdatamodel.propertyModelToDatabaseModel
import com.example.your_space.ui.booking.BookItem
import com.example.your_space.ui.ourspaces.SpaceItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val database: AppDao) {

    val workingSpacesRepo: LiveData<List<SpaceItem>> =
        database.gelAllWorkingSpaces().map { it.spaceToDomainModel() }

    val BookingsRepo: LiveData<List<BookItem>> =
        database.gelAllBookings().map { it.bookingToDomainModel() }



     suspend fun refreshAllBookingString(): String {
        return Network.NetworkServices.getAllBookingsAsString()
    }

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
        withContext(Dispatchers.IO) {
            database.deleteBooking(bookItem.bookId)
        }
    }

    suspend fun deleteAllBookings() {
        withContext(Dispatchers.IO) {
            database.deleteAllBookings()
        }
    }

}