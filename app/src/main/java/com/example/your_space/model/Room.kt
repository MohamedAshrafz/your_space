package com.example.your_space.model

data class Room (
    val roomId : Int,
    val number : Int,
    val name : String,
    val activity : String,
    val type : String,
    val price : Float,
    val image : String,
    val bookings : List<Booking>
        )