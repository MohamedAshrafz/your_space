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

    var mySession: String? = null

    fun getSession(): String? {
        return mySession
    }

    val workingSpacesRepo: LiveData<List<WorkingSpaceDB>> =
        database.getAllWorkingSpaces()

    val workingSpacesRepoOrderByRatings: LiveData<List<WorkingSpaceDB>> =
        database.getAllWorkingSpacesOrderByRatings()

    fun workingSpacesRepoSearchBy(searchQuery: String): LiveData<List<WorkingSpaceDB>> =
        database.getAllWorkingSpacesSearchBy(searchQuery)

    val bookingsRepo: LiveData<List<BookingDB>> = database.gelAllBookings("upcoming")

    val historyBookingsRepo: LiveData<List<BookingDB>> = database.gelAllBookings("past")

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
                val dbUser = database.getUserWithUserId(userId)
                currentUser = loginAndGetTokenForUserWith(dbUser.userName, dbUser.password)
            }
        } catch (e: Exception) {
            Log.e(REPOSITORY_ERROR_STRING, e.stackTraceToString())
        }
        return currentUser
    }

    suspend fun updateUserData(newUserData: UserPropertyUpdate, userId: String): Boolean {
        var isSucceeded = false
        try {
            withContext(Dispatchers.IO) {
                val response = mySession?.let {
                    NetworkServices.updateUser(
                        newUserData,
                        it, userId
                    )
                }

                response?.let {
                    if (response.isSuccessful) {
                        updateTokenForUserWithUserId(userId)
                        isSucceeded = true
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(REPOSITORY_ERROR_STRING, e.stackTraceToString())
        }
        return isSucceeded
    }

    suspend fun loginAndGetTokenForUserWith(userName: String, password: String): UserDB? {
        var currentUser: UserDB? = null
        try {
            withContext(Dispatchers.IO) {
                val response = NetworkServices.loginWithUsernameAndPassword(userName, password)

                if (response.isSuccessful) {

//                    Log.e(REPOSITORY_ERROR_STRING, response.headers().toString())
                    mySession = response.headers().get("Set-Cookie")!!.split(";")[0]
                    Log.e(REPOSITORY_ERROR_STRING, mySession!!)

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
                } else {
                    currentUser = UserDB(
                        userId = "-1",
                    )
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
                    mySession?.let { NetworkServices.getWorkingSpacesUsingPaging(pageNumber, it) }
                if (initialize) {
                    database.deleteAllWorkingSpaces()
                }
                workingSpacesList?.let {
                    database.insertAllWorkingSpaces(*(workingSpacesList.workingSpacesPropertyModelToDatabaseModel()))
                }
            }
        } catch (e: Exception) {
            Log.e(REPOSITORY_ERROR_STRING, e.stackTraceToString())
        }
    }

    suspend fun refreshBookings(userId: String) {
        try {
            withContext(Dispatchers.IO) {
                val bookingsList = mySession?.let { NetworkServices.getAllBookings(userId, it) }
                val historyBookingsList =
                    mySession?.let { NetworkServices.getAllHistoryBookings(userId, it) }
                database.deleteAllBookings()
                bookingsList?.let {
                    database.insertAllBookings(*(bookingsList.bookingPropertyModelToDatabaseModel()))
                }
                historyBookingsList?.let {
                    database.insertAllBookings(*(historyBookingsList.bookingPropertyModelToDatabaseModel()))
                }
            }
        } catch (e: Exception) {
            Log.e(REPOSITORY_ERROR_STRING, e.stackTraceToString())
        }
    }

    suspend fun getRoomsBySpaceIdFromNetwork(spaceId: String) {
        try {
            withContext(Dispatchers.IO) {
                val roomsList = mySession?.let { NetworkServices.getRoomsBySpaceId(spaceId, it) }
                database.deleteAllRoomsWithSpaceId(spaceId)
                roomsList?.let {
                    database.insertAllRooms(*(roomsList.roomPropertyModelToDatabaseModel()))
                }
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
        var isPosted: Boolean = false
        val response = mySession?.let { NetworkServices.addNewBooking(newBooking, it) }

        if (response != null) {
            if (response.isSuccessful) {
                isPosted = true
                refreshBookings(newBooking.userId)
            } else {
                Log.e("RETROFIT_ERROR", response.code().toString())
            }
        }
        return isPosted
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
                val response =
                    mySession?.let { NetworkServices.cancelBooking(bookItem.bookingId.toInt(), it) }

                response?.let {
                    if (response.isSuccessful) {
                        database.deleteBooking(bookItem.bookingId)

                    } else {
                        Log.e("RETROFIT_ERROR", response.code().toString())
                    }
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

    suspend fun sendMessage(message: String, userId: String): Boolean {
        var isSucceeded = false
        try {
            withContext(Dispatchers.IO) {
                val response = mySession?.let { NetworkServices.postMessage(userId, message, it) }
                response?.let {
                    if (response.isSuccessful) {
                        isSucceeded = true
                    } else {
                        Log.e("RETROFIT_ERROR", response.code().toString())
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(REPOSITORY_ERROR_STRING, e.stackTraceToString())
        }
        return isSucceeded
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