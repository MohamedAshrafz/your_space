package com.example.your_space.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.your_space.ui.booking.BookItem
import com.example.your_space.ui.ourspaces.SpaceItem
import java.sql.Time
import java.sql.Date
import java.util.*

@Entity(tableName = "users_table")
data class UserDB(
    @PrimaryKey
    val userId: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val mobileNo: String,
    val address: String,
    val bio: String,
    val points: Int,
    val picture: String
)

@Entity(tableName = "requests_table")
data class RequestDB(
    @PrimaryKey
    val requestId: Int,
    val name: String,
    val status: String,
    val address: String,
    val noOfRooms: Int
)

@Entity(tableName = "spaceRooms_table")
data class SpaceRoomDB(
    @PrimaryKey
    val roomId: Int,
    val number: Int,
    val name: String,
    val activity: String,
    val type: String,
    val price: Float,
    val image: String
)

@Entity(tableName = "workingSpaces_table")
data class WorkingSpaceDB(
    @PrimaryKey
    val spaceId: String = UUID.randomUUID().toString(),
    val address: String,
    val district: String = "",
    val rating: Double,
    val images: String = "",
    val roomNumbers: Int = 0,
    val description: String,
    val name: String,
    val contactNumber: String,
    val minPrice: Double,
    val maxPrice: Double = 100.0,
    val drinks: Boolean = false,
    val owner: String = "",
    val outdoors: Boolean = false,

    val time: Time = Time(50L),
    val date: Date = Date(50L)
)

@Entity(tableName = "booking_table")
data class BookingDB(
    @PrimaryKey
    val bookingId: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val roomId: String = "",
    val time: Time,
    val date: Date
)

fun List<SpaceItem>.spaceToDatabaseModel(): Array<WorkingSpaceDB> {
    return map {
        WorkingSpaceDB(
            spaceId = it.id,
            name = it.spaceName,
            address = it.location,
            contactNumber = it.mobile,
            rating = it.rating.toDouble(),
            minPrice = it.price.toDouble(),
            description = it.description,
            images = it.img
        )
    }.toTypedArray()
}

fun List<WorkingSpaceDB>.spaceToDomainModel(): List<SpaceItem> {
    return map {
        SpaceItem(
            id = it.spaceId,
            spaceName = it.name,
            location = it.address,
            mobile = it.contactNumber,
            rating = it.rating.toString(),
            price = it.minPrice.toString(),
            description = it.description,
            img = it.images
        )
    }
}

fun List<BookItem>.bookingToDatabaseModel(): Array<BookingDB> {
    return map {


        BookingDB(
            bookingId = it.bookId,
            roomId = it.bookName,
            date = Date(31 / 4 / 2023),
            time = Time(20, 29, 29)
        )
    }.toTypedArray()
}

fun List<BookingDB>.bookingToDomainModel(): List<BookItem> {
    return map {
        BookItem(
            bookId = it.bookingId,
            bookName = "Book " + it.bookingId,
            date = it.date.toString(),
            time = it.time.toString()
        )
    }
}



