package com.example.your_space.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AppDao {
    ////////////////Spaces///////////////////////////////////
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllWorkingSpaces(vararg spaces: WorkingSpaceDB)

    @Query("SELECT * FROM workingSpaces_table ORDER BY spaceId")
    fun gelAllWorkingSpaces(): LiveData<List<WorkingSpaceDB>>

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

    ////////////////////ratings///////////////////////////

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertAllRatings(vararg ratings: RatingDB)
//
//    @Query("SELECT * FROM rating_table ORDER BY ratingId")
//    fun getAllRatings(): LiveData<List<RatingDB>>
//
//    @Query("SELECT * FROM rating_table WHERE spaceId = :ratingId ORDER BY ratingId")
//    fun getAllRatingsWithSpaceId(ratingId: String): LiveData<List<RatingDB>>
//
//    @Query("DELETE FROM rating_table")
//    fun deleteAllRatings()
//
//    @Query("DELETE FROM rating_table WHERE ratingId = :ratingId")
//    fun deleteAllRatingsWithSpaceId(ratingId: String)

}