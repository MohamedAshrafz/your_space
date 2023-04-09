package com.example.your_space.model

import java.sql.Time

data class Space (
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