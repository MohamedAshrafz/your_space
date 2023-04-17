package com.example.your_space.network.networkdatamodel

import android.os.Parcelable
import com.example.your_space.database.BookingDB
import com.example.your_space.database.DBConverters
import com.example.your_space.database.WorkingSpaceDB
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.sql.Date
import java.sql.Time



@JsonClass(generateAdapter = true)
@Parcelize
data class BookingProperty (
//    val id : Int,
//    val startTime : Time,
//    val endTime : Time,
//    val room : WorkingSpaceRoomProperty,
//    val user : UserProperty,
    @Json(name = "id")
    val bookingId: Int,
    @Json(name = "startTime")
    val startTime: String = "",
    @Json(name = "endTime")
    val endTime: String = "",
    @Json(name = "date")
    val date: String = "",
    @Json(name = "room")
    val room: WorkingSpaceRoomProperty ,
    @Json(name = "user")
    val user: UserProperty
        ) :Parcelable

fun List<BookingProperty>.bookingProertyModelToDatabaseModel(): Array<BookingDB> {
    return map {
        BookingDB(
            roomId = it.room.name,
            date = Date(17/3/2222),
            time = Time(20,20,22)
        )
    }.toTypedArray()
}