package com.example.your_space.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.your_space.ui.booking.BookItem

@Dao
interface AppDao {
    ////////////////Spaces///////////////////////////////////
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllWorkingSpaces(vararg spaces: WorkingSpaceDB)

    @Query("SELECT * FROM workingSpaces_table")
    fun gelAllWorkingSpaces(): LiveData<List<WorkingSpaceDB>>

    @Query("DELETE FROM workingSpaces_table")
    fun deleteAllWorkingSpaces()

    ///////////////////Bookings/////////////////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllBookings(vararg bookings : BookingDB)

    @Query("SELECT * FROM booking_table")
    fun gelAllBookings(): LiveData<List<BookingDB>>

//    @Query("DELETE FROM booking_table WHERE bookingId = :bookId")
//    fun deleteBooking(bookId: String)
}