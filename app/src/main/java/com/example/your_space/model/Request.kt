package com.example.your_space.model

data class Request (
    val requestId : Int,
    val name : String,
    val status : String,
    val address : String,
    val noOfRooms : Int
        )