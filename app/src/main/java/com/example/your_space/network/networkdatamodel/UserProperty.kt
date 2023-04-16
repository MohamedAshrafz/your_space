package com.example.your_space.network.networkdatamodel

data class UserProperty (
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
        val requests : List<RequestProperty>,
        val bookings : List<BookingProperty>
        )
