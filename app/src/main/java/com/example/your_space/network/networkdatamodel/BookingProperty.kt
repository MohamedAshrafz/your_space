package com.example.your_space.network.networkdatamodel

import java.sql.Time

data class BookingProperty (
    val id : Int,
    val startTime : Time,
    val endTime : Time,
    val room : WorkingSpaceRoomProperty,
    val user : UserProperty
        )