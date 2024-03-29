package com.example.your_space.network.networkdatamodel

import android.os.Parcelable
import com.example.your_space.database.BookingDB
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*


@JsonClass(generateAdapter = true)
@Parcelize
data class BookingProperty(
    @Json(name = "id")
    val bookingId: String,
    @Json(name = "startTime")
    val startTime: String = "",
    @Json(name = "endTime")
    val endTime: String = "",
    @Json(name = "date")
    val date: String = "",
    @Json(name = "roomId")
    val roomId: String,
    @Json(name = "userId")
    val userId: String,
    @Json(name = "spaceName")
    val spaceName: String,
    @Json(name = "qrScan")
    val qrScan: Boolean,
    @Json(name = "paymentMethod")
    val paymentMethod: String,
    @Json(name = "price")
    val price: Double,
    @Json(name = "bookingStatus")
    val bookingStatus: String
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class BookingPropertyPost(
    @Json(name = "startTime")
    val startTime: String = "",
    @Json(name = "endTime")
    val endTime: String = "",
    @Json(name = "date")
    val date: String = "",
    @Json(name = "roomId")
    val roomId: String,
    @Json(name = "userId")
    val userId: String,
    @Json(name = "spaceName")
    val spaceName: String,
    @Json(name = "paymentMethod")
    val paymentMethod: String = "lol"
) : Parcelable

fun List<BookingProperty>.bookingPropertyModelToDatabaseModel(): Array<BookingDB> {
    return map {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        // Date type
        val parsedDate = dateFormat.parse(it.date)

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        // Date type
        val parsedTime = timeFormat.parse(it.startTime)
        BookingDB(
            bookingId = it.bookingId,
            date = it.date,
            startTime = it.startTime,
            endTime = it.endTime,
            roomId = it.roomId,
            spaceId = "",
            bookingStatus = it.bookingStatus,
            spaceName = it.spaceName
        )
    }.toTypedArray()
}