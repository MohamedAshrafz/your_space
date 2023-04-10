package com.example.your_space.model

import java.util.*

data class User (
        val userId : Int,
        val email : String,
        val firstName : String,
        val lastName : String,
        val password : String,
        val mobileNo : String,
        val address : String,
        val bio : String,
        val points : Int,
        val picture : String,
        val requests : List<Request>,
        val bookings : List<Booking>
        )
