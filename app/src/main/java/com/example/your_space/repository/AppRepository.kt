package com.example.your_space.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.your_space.database.*
import com.example.your_space.network.Network.NetworkServices
import com.example.your_space.network.networkdatamodel.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val REPOSITORY_ERROR_STRING = "Error in repository"


class AppRepository private constructor(private val database: AppDao) {

    private lateinit var session: String

    val workingSpacesRepo: LiveData<List<WorkingSpaceDB>> = database.gelAllWorkingSpaces()

    val bookingsRepo: LiveData<List<BookingDB>> = database.gelAllBookings("Current")

    val historyBookingsRepo: LiveData<List<BookingDB>> = database.gelAllBookings("Past")

//    val roomsRepo: LiveData<List<SpaceRoomDB>> = database.getAllRooms()

    suspend fun getUserWithUserName(userName: String): UserDB? {
        var currentUser: UserDB? = null
        try {
            withContext(Dispatchers.IO) {
                val users = NetworkServices.getAllUsers()
                if (users.isNotEmpty()) {
                    database.insertAllUsers(*(users.userPropertyModelToDatabaseModel()))
                    currentUser = database.getUserWithUserName(userName)
                    Log.e(REPOSITORY_ERROR_STRING, currentUser.toString())
                }
            }
        } catch (e: Exception) {
            Log.e(REPOSITORY_ERROR_STRING, e.stackTraceToString())
        }
        return currentUser
    }

    suspend fun updateTokenForUserWithUserId(userId: String): UserDB? {
        var currentUser: UserDB? = null
        try {
            withContext(Dispatchers.IO) {
                currentUser = database.getUserWithUserId(userId)
                if (currentUser != null) {
                    loginAndGetTokenForUserWith(
                        (currentUser as UserDB).userName,
                        (currentUser as UserDB).password
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(REPOSITORY_ERROR_STRING, e.stackTraceToString())
        }
        return currentUser
    }

    suspend fun loginAndGetTokenForUserWith(userName: String, password: String): UserDB? {
        var currentUser: UserDB? = null
        try {
            withContext(Dispatchers.IO) {
                val response = NetworkServices.loginWithUsernameAndPassword(userName, password)


                if (response.isSuccessful) {

//                    Log.e(REPOSITORY_ERROR_STRING, response.headers().toString())
                    session = response.headers().get("Set-Cookie")!!.split(";")[0]
                    Log.e(REPOSITORY_ERROR_STRING, session)

                    response.body()?.let {
                        val responseUser = it.userPropertyModelToDatabaseModel()

                        currentUser = UserDB(
                            responseUser.userId,
                            responseUser.email,
                            responseUser.firstName,
                            responseUser.lastName,
                            responseUser.mobileNo,
                            responseUser.address,
                            responseUser.birthDate,
                            responseUser.bio,
                            responseUser.points,
                            responseUser.userName,
                            password,
                        )

                        database.insertAllUsers(currentUser as UserDB)
                        Log.e(REPOSITORY_ERROR_STRING, currentUser.toString())
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(REPOSITORY_ERROR_STRING, e.stackTraceToString())
        }
        return currentUser
    }

    suspend fun refreshWorkingSpaces() {
        try {
            withContext(Dispatchers.IO) {
                val workingSpacesList = NetworkServices.getAllWorkingSpaces()
                if (workingSpacesList.isNotEmpty()) {
                    database.insertAllWorkingSpaces(*(workingSpacesList.workingSpacesPropertyModelToDatabaseModel()))
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
                val workingSpacesList =
                    NetworkServices.getWorkingSpacesUsingPaging(pageNumber, session)
                if (initialize) {
                    database.deleteAllWorkingSpaces()
                }
                database.insertAllWorkingSpaces(*(workingSpacesList.workingSpacesPropertyModelToDatabaseModel()))
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
                val roomsList = NetworkServices.getRoomsBySpaceId(spaceId, session)
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

    suspend fun addNewBooking(newBooking: BookingPropertyPost): Boolean {
        val isPosted: Boolean
        val response = NetworkServices.addNewBooking(newBooking, session)
        return if (response.isSuccessful) {
            isPosted = true
            refreshBookings()
            isPosted
        } else {
            isPosted = false
            Log.e("RETROFIT_ERROR", response.code().toString())
            isPosted
        }
    }

    suspend fun getSpaceWithSpaceId(spaceId: String): WorkingSpaceDB {
        return withContext(Dispatchers.IO) {
            database.getSpaceWithSpaceId(spaceId)
        }
    }

    suspend fun getUserWithId(userId: String): UserDB {
        return withContext(Dispatchers.IO) {
            database.getUserWithUserId(userId)
        }
    }

    suspend fun deleteBookingWithId(bookItem: BookingDB) {
        try {
            withContext(Dispatchers.IO) {
                // Do the DELETE request and get response
                val response = NetworkServices.cancelBooking(bookItem.bookingId.toInt(), session)
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