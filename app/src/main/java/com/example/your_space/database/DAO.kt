package com.example.your_space.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AppDao {
    ////////////////Users///////////////////////////////////
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUsers(vararg spaces: UserDB)

    @Query("SELECT * FROM users_table WHERE  userId == :userId")
    fun getUserWithUserId(userId: String): UserDB

    @Query("SELECT * FROM users_table WHERE  userName == :userName")
    fun getUserWithUserName(userName: String): UserDB

    ////////////////Spaces///////////////////////////////////
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllWorkingSpaces(vararg spaces: WorkingSpaceDB)

    @Query("SELECT * FROM workingSpaces_table ORDER BY spaceId")
    fun getAllWorkingSpaces(): LiveData<List<WorkingSpaceDB>>

    @Query("SELECT * FROM workingSpaces_table ORDER BY rating")
    fun getAllWorkingSpacesOrderByRatings(): LiveData<List<WorkingSpaceDB>>

    @Query("SELECT * FROM workingSpaces_table WHERE district Like :district")
    fun getAllWorkingSpacesSearchBy(district: String): LiveData<List<WorkingSpaceDB>>

    @Query("SELECT * FROM workingSpaces_table WHERE spaceId == :spaceId")
    fun getSpaceWithSpaceId(spaceId: String): WorkingSpaceDB

    @Query("DELETE FROM workingSpaces_table")
    fun deleteAllWorkingSpaces()

    @Query("DELETE FROM WORKINGSPACES_TABLE WHERE (spaceId >= :start AND spaceId < :end)")
    fun deleteWorkingSpacesWithLimit(start: Int, end: Int)

    ///////////////////Bookings/////////////////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllBookings(vararg bookings: BookingDB)

    @Query("SELECT * FROM booking_table ORDER BY bookingId")
    fun gelAllBookings(): LiveData<List<BookingDB>>

    @Query("DELETE FROM booking_table")
    fun deleteAllBookings()

    @Query("DELETE FROM booking_table WHERE bookingId = :bookId")
    fun deleteBooking(bookId: String)

    ////////////////////rooms///////////////////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRooms(vararg spaces: SpaceRoomDB)

    @Query("SELECT * FROM spaceRooms_table ORDER BY roomId")
    fun getAllRooms(): LiveData<List<SpaceRoomDB>>

    @Query("SELECT * FROM spaceRooms_table WHERE spaceId = :spaceId ORDER BY roomId")
    fun getAllRoomsWithSpaceId(spaceId: String): LiveData<List<SpaceRoomDB>>

    @Query("DELETE FROM spaceRooms_table")
    fun deleteAllRooms()

    @Query("DELETE FROM spaceRooms_table WHERE spaceId = :spaceId")
    fun deleteAllRoomsWithSpaceId(spaceId: String)

}