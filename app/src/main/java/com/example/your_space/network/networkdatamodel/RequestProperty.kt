package com.example.your_space.network.networkdatamodel

data class RequestProperty (
    val requestId : Int,
    val name : String,
    val status : String,
    val address : String,
    val noOfRooms : Int
        )