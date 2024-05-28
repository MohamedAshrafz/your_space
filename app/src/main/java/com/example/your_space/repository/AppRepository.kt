package com.example.your_space.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.your_space.database.AppDao
import com.example.your_space.database.AppDatabase
import com.example.your_space.database.BookingDB
import com.example.your_space.database.RatingsDB
import com.example.your_space.database.SpaceRoomDB
import com.example.your_space.database.UserDB
import com.example.your_space.database.WorkingSpaceDB
import com.example.your_space.network.Network.NetworkServices
import com.example.your_space.network.networkdatamodel.BookingPropertyPost
import com.example.your_space.network.networkdatamodel.UserPropertyUpdate
import com.example.your_space.network.networkdatamodel.bookingPropertyModelToDatabaseModel
import com.example.your_space.network.networkdatamodel.ratingPropertyModelToDatabaseModel
import com.example.your_space.network.networkdatamodel.roomPropertyModelToDatabaseModel
import com.example.your_space.network.networkdatamodel.userPropertyModelToDatabaseModel
import com.example.your_space.network.networkdatamodel.workingSpacesPropertyModelToDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class AppRepository private constructor(private val database: AppDao) {

    private val TAG = AppRepository::class.java.simpleName
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
                    Timber.tag(TAG).e(currentUser.toString())
                }
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e.stackTraceToString())
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
            Timber.tag(TAG).e(e.stackTraceToString())
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
            Timber.tag(TAG).e(e.stackTraceToString())
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
                    Timber.tag(TAG).e(mySession!!)

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
                        Timber.tag(TAG).e(currentUser.toString())
                    }
                } else {
                    currentUser = UserDB(
                        userId = "-1",
                    )
                }
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e.stackTraceToString())
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
            Timber.tag(TAG).e(e.stackTraceToString())
        }
    }

    suspend fun loadWorkingSpacesOfPage(pageNumber: Int, initialize: Boolean) {
//        val start = pageNumber * PAGE_SIZE
//        val end = start + PAGE_SIZE
        try {
            withContext(Dispatchers.IO) {
                val workingSpacesList =
                    mySession?.let { NetworkServices.getWorkingSpacesUsingPaging(pageNumber, it) }

                if (initialize && workingSpacesList?.isNotEmpty() == true) {
                    database.deleteAllWorkingSpaces()
                    workingSpacesList.let {
                        database.insertAllWorkingSpaces(*(workingSpacesList.workingSpacesPropertyModelToDatabaseModel()))
                    }
                } else if (workingSpacesList?.isNotEmpty() == true) {
                    workingSpacesList.let {
                        database.insertAllWorkingSpaces(*(workingSpacesList.workingSpacesPropertyModelToDatabaseModel()))
                    }
                }
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e.stackTraceToString())
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
            Timber.tag(TAG).e(e.stackTraceToString())
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
            Timber.tag(TAG).e(e.stackTraceToString())
        }
    }

    suspend fun getRoomsBySpaceIdFromDB(spaceId: String): LiveData<List<SpaceRoomDB>> {
        return withContext(Dispatchers.IO) {
            database.getAllRoomsWithSpaceId(spaceId)
        }
    }

    suspend fun getRatingsBySpaceIdFromNetwork(spaceId: String) {
        try {
            withContext(Dispatchers.IO) {
                val ratingsList =
                    mySession?.let { NetworkServices.getRatingsOfSpaceId(spaceId, it) }
                database.deleteAllRatingsWithSpaceId(spaceId)
                ratingsList?.let {
                    database.insertAllRatings(*(ratingsList.ratingPropertyModelToDatabaseModel()))
                }
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e.stackTraceToString())
        }
    }

    suspend fun getRatingsBySpaceIdFromDB(spaceId: String): LiveData<List<RatingsDB>> {
        return withContext(Dispatchers.IO) {
            database.getAllRatingsWithSpaceId(spaceId)
        }
    }

    suspend fun deleteAllWorkingSpaces() {
        withContext(Dispatchers.IO) {
            database.deleteAllWorkingSpaces()
        }
    }

    suspend fun addNewBooking(newBooking: BookingPropertyPost): Boolean {
        var isPosted = false
        val response = mySession?.let { NetworkServices.addNewBooking(newBooking, it) }

        if (response != null) {
            if (response.isSuccessful) {
                isPosted = true
                refreshBookings(newBooking.userId)
            } else {
                Timber.tag("RETROFIT_ERROR").e(response.code().toString())
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

    suspend fun getUserWithIdLD(userId: String): LiveData<UserDB> {
        return withContext(Dispatchers.IO) {
            database.getUserWithUserIdLD(userId)
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
                        Timber.tag("RETROFIT_ERROR").e(response.code().toString())
                    }
                }
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e.stackTraceToString())
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
                        Timber.tag(TAG).e(response.code().toString())
                    }
                }
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e.stackTraceToString())
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