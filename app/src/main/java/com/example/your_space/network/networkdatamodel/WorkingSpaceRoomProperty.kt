package com.example.your_space.network.networkdatamodel

import android.os.Parcelable
import com.example.your_space.database.SpaceRoomDB
import com.example.your_space.database.WorkingSpaceDB
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class WorkingSpaceRoomProperty (
    @Json(name = "id")
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
    @Json(name = "spaceId")
    val space: String,

        ) :Parcelable


fun List<WorkingSpaceRoomProperty>.roomPropertyModelToDatabaseModel(): Array<SpaceRoomDB> {
    return map {
        SpaceRoomDB(
            roomId = it.roomId,
            number = it.number,
            name = it.name,
            activity = it.activity,
            type = it.type,
            price = it.price,
            spaceId = it.space
        )
    }.toTypedArray()
}

