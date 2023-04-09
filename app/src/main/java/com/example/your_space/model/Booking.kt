package com.example.your_space.model

import java.sql.Time
import java.time.LocalDate

data class Booking (
    val id : Int,
    val startTime : Time,
    val endTime : Time,
    val date : LocalDate,
    val room : Room,
    val user : User
        )