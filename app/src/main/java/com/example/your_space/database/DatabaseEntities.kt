package com.example.your_space.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.your_space.model.Booking
import com.example.your_space.model.Request
import com.example.your_space.model.Room
import com.example.your_space.model.User
import java.sql.Time
import java.time.LocalDate
import java.util.*

@Entity(tableName = "SpaceItems_table")
data class DatabaseSpaceItem(
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

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val userId : Int,
    val email : String,
    val firstName : String,
    val lastName : String,
    val password : String,
    val mobileNo : String,
    val address : String,
    val birthDate : Date,
    val bio : String,
    val points : Int,
    val picture : String,
    val requests : List<Request>,
    val bookings : List<Booking>
)

@Entity(tableName = "request")
data class Request(
    @PrimaryKey
    val requestId : Int,
    val name : String,
    val status : String,
    val address : String,
    val noOfRooms : Int
)

@Entity(tableName = "room")
data class Room(
    @PrimaryKey
    val roomId : Int,
    val number : Int,
    val name : String,
    val activity : String,
    val type : String,
    val price : Float,
    val image : String,
    val bookings : List<Booking>
)

@Entity(tableName = "space")
data class Space(
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
    val startTime : Time,
    val endTime : Time,
    val drinks : Boolean,
    val owner : String,
    val outdoors : Boolean
)

@Entity(tableName = "booking")
data class Booking(
    @PrimaryKey
    val id : Int,
    val startTime : Time,
    val endTime : Time,
    val date : LocalDate,
    val room : Room,
    val user : User
)




