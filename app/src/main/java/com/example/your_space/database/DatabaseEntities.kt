package com.example.your_space.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SpaceItems_table")
data class SpaceItemDB(
    @PrimaryKey
    var id: Int,
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
    val userId : Int,
    val email : String,
    val firstName : String,
    val lastName : String,
    val password : String,
    val mobileNo : String,
    val address : String,
    val bio : String,
    val points : Int,
    val picture : String
)

@Entity(tableName = "requests_table")
data class RequestDB(
    @PrimaryKey
    val requestId : Int,
    val name : String,
    val status : String,
    val address : String,
    val noOfRooms : Int
)

@Entity(tableName = "spaceRooms_table")
data class SpaceRoomDB(
    @PrimaryKey
    val roomId : Int,
    val number : Int,
    val name : String,
    val activity : String,
    val type : String,
    val price : Float,
    val image : String
)

@Entity(tableName = "workingSpaces_table")
data class WorkingSpaceDB(
    @PrimaryKey
    val spaceId : Int,
    val address : String,
    val district : String,
    val rating : Double,
    val images : String,
    val roomNumbers : Int,
    val description : String,
    val name : String,
    val contactNumber : String,
    val minPrice : Double,
    val maxPrice : Double,
    val drinks : Boolean,
    val owner : String,
    val outdoors : Boolean
)

@Entity(tableName = "booking_table")
data class BookingDB(
    @PrimaryKey
    val id : Int,
)




