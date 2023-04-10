package com.example.your_space.model

import java.sql.Time
import java.time.LocalDate

data class Booking (
    val id : Int,
    val startTime : Time,
    val endTime : Time,
    val room : WorkingSpaceRoom,
    val user : User
        )