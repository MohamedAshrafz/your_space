package com.example.your_space.network.networkdatamodel

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class WorkingSpaceRoomProperty (
    @Json(name = "roomId")
    val roomId: Int,
    @Json(name = "number")
    val number: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "activity")
    val activity: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "price")
    val price: Float,
    @Json(name = "image")
    val image: String,
    @Json(name = "space")
    val space: SpaceItemProperty
        ) :Parcelable

