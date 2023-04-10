package com.example.your_space.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.your_space.ui.ourspaces.SpaceItem
import java.util.*

@Entity(tableName = "SpaceItems_table")
data class SpaceItemDB(
    @PrimaryKey
    var id: String,
    var spaceName: String,
    var location: String,
    var mobile: String,
    var rating: String,
    var price: String,
    var description: String,
    val img: String
)

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
    val images: String,
    val roomNumbers: Int = 0,
    val description: String,
    val name: String,
    val contactNumber: String,
    val minPrice: Double,
    val maxPrice: Double = 100.0,
    val drinks: Boolean = false,
    val owner: String = "",
    val outdoors: Boolean = false
)

@Entity(tableName = "booking_table")
data class BookingDB(
    @PrimaryKey
    val id: Int,
)

fun List<SpaceItem>.toDatabaseModel(): Array<WorkingSpaceDB> {
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

fun List<WorkingSpaceDB>.toDomainModel(): List<SpaceItem> {
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