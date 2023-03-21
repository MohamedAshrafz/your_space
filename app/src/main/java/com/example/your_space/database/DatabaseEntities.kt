package com.example.your_space.database

import androidx.room.Entity
import androidx.room.PrimaryKey

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