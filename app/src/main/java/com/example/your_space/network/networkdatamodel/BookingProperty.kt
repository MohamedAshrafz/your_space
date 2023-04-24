package com.example.your_space.network.networkdatamodel

import android.os.Parcelable
import com.example.your_space.database.BookingDB
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*


@JsonClass(generateAdapter = true)
@Parcelize
data class BookingProperty(
//    val id : Int,
//    val startTime : Time,
//    val endTime : Time,
//    val room : WorkingSpaceRoomProperty,
//    val user : UserProperty,
    @Json(name = "id")
    val bookingId: String,
    @Json(name = "startTime")
    val startTime: String = "",
    @Json(name = "endTime")
    val endTime: String = "",
    @Json(name = "date")
    val date: String = "",
    @Json(name = "room")
    val room: WorkingSpaceRoomProperty,
    @Json(name = "user")
    val user: UserProperty
) : Parcelable

fun List<BookingProperty>.bookingProertyModelToDatabaseModel(): Array<BookingDB> {
    return map {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        // Date type
        val parsedDate = dateFormat.parse(it.date)

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        // Date type
        val parsedTime = timeFormat.parse(it.startTime)
        BookingDB(
            roomId = it.room.name,
            date = Date(parsedDate.time),
            time = Time(parsedTime.time),
            bookingId = it.bookingId
        )
    }.toTypedArray()
}