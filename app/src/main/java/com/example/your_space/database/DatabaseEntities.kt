package com.example.your_space.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.sql.Date
import java.sql.Time
import java.util.*

@Entity(tableName = "users_table")
@Parcelize
data class UserDB(
    @PrimaryKey
    val userId: String,
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val mobileNo: String = "",
    val address: String = "",
    val birthDate: String = "",
    val bio: String = "",
    val points: Int = 0,
    val userName: String = "",
    val password: String = ""
) : Parcelable

@Entity(tableName = "requests_table")
@Parcelize
data class RequestDB(
    @PrimaryKey
    val requestId: Int,
    val name: String,
    val status: String,
    val address: String,
    val noOfRooms: Int
) : Parcelable

@Entity(tableName = "spaceRooms_table")
@Parcelize
data class SpaceRoomDB(
    @PrimaryKey
    val roomId: Int,
    val number: Int,
    val name: String,
    val activity: String,
    val type: String,
    val price: Float,
    val image: String = "",
    val spaceId: String,
) : Parcelable {
    fun typeFunction(): String {
        return "Type: $type"
    }

    fun priceFunction(): String {
        return "Price: $price"
    }
}

@Entity(tableName = "workingSpaces_table")
@Parcelize
data class WorkingSpaceDB(
    @PrimaryKey
    val spaceId: String,
    val address: String,
    val district: String,
    val rating: String,
    val images: String = "",
    val roomNumbers: Int = 0,
    val description: String,
    val name: String,
    val contactNumber: String,
    val minPrice: Double,
    val maxPrice: Double,
    val drinks: Boolean,
    val owner: String = "",
    val outdoors: Boolean = false,
    val startTime: String,
    val endTime: String,

    val time: Time = Time(50L),
    val date: Date = Date(50L)
) : Parcelable {
    fun locationFunction(): String {
        return "$address, $district"
    }

    fun pricingFunction(): String {
        return "$minPrice - $maxPrice"
    }

    fun freeWiFiFunction(): String {
        return if (drinks) "Offers free Wi-Fi" else "No offer free Wi-Fi"
    }

    fun openNewFunction(): String {
        return "$startTime - $endTime"
    }

    fun drinksFunction(): String {
        return if (drinks) "Drinks: Yes" else "Drinks: No"
    }
}

@Entity(tableName = "booking_table")
@Parcelize
data class BookingDB(
    @PrimaryKey
    val bookingId: String = UUID.randomUUID().toString(),
    val startTime: String = "",
    val endTime: String = "",
    val date: String = "",
    val roomId: String = "",
    val spaceId: String = "",
    val bookingStatus: String = "",
    val spaceName:String = ""
) : Parcelable {
    fun namingFunction(): String {
        return "Co-working space: $spaceName\nRoom: $roomId"
    }

    fun timingFromTo(): String {
        return "${startTime.subSequence(0, 5)} - ${endTime.subSequence(0, 5)}"
    }
}

//fun List<SpaceItem>.spaceToDatabaseModel(): Array<WorkingSpaceDB> {
//    return map {
//        WorkingSpaceDB(
//            spaceId = it.id,
//            name = it.spaceName,
//            address = it.location,
//            contactNumber = it.mobile,
//            rating = it.rating.toDouble(),
//            minPrice = it.price.toDouble(),
//            description = it.description,
//            images = it.img
//        )
//    }.toTypedArray()
//}
//
//fun List<WorkingSpaceDB>.spaceToDomainModel(): List<SpaceItem> {
//    return map {
//        SpaceItem(
//            id = it.spaceId,
//            spaceName = it.name,
//            location = it.address,
//            mobile = it.contactNumber,
//            rating = it.rating.toString(),
//            price = it.minPrice.toString(),
//            description = it.description,
//            img = it.images
//        )
//    }
//}

//fun List<BookItem>.bookingToDatabaseModel(): Array<BookingDB> {
//    return map {
//
//        BookingDB(
//            bookingId = it.bookId,
//            roomId = it.bookName,
//            date = Date(31 / 4 / 2023),
//            time = Time(20, 29, 29)
//        )
//    }.toTypedArray()
//}
//
//fun List<BookingDB>.bookingToDomainModel(): List<BookItem> {
//    return map {
//        BookItem(
//            bookId = it.bookingId,
//            bookName = "Book: " + it.bookingId,
//            date = it.date.toString(),
//            time = it.time.toString()
//        )
//    }
//}



