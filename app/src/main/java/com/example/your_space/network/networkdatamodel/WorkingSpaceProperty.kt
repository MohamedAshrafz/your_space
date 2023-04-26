package com.example.your_space.network.networkdatamodel

import android.os.Parcelable
import com.example.your_space.database.WorkingSpaceDB
import com.example.your_space.ui.ourspaces.SpaceItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class SpaceItemProperty(
    @Json(name = "spaceId")
    val spaceId: Int,
    @Json(name = "address")
    val address: String = "",
    @Json(name = "district")
    val district: String = "",
    @Json(name = "roomNumbers")
    val roomNumbers: Int = 5,
    @Json(name = "description")
    val description: String = "",
    @Json(name = "name")
    val name: String,
    @Json(name = "contactNumber")
    val contactNumber: String = "",
    @Json(name = "minPrice")
    val minPrice: Int = 4,
    @Json(name = "maxPrice")
    val maxPrice: Int = 5,
    @Json(name = "startTime")
    val startTime: String = "",
    @Json(name = "endTime")
    val endTime: String = "",
    @Json(name = "drinks")
    val drinks: Boolean = false,
    @Json(name = "owner")
    val owner: String = "",
    @Json(name = "outdoors")
    val outdoors: Boolean = false,
    @Json(name = "ratingAverage")
    val ratingAverage: Double = 3.5,
    @Json(name = "ratingList")
    val ratingList: List<Int>? = null
) : Parcelable

fun List<SpaceItemProperty>.propertyModelToDatabaseModel(): Array<WorkingSpaceDB> {
    return map {
        WorkingSpaceDB(
            spaceId = it.spaceId.toString(),
            name = it.name,
            address = it.address,
            contactNumber = it.contactNumber,
            rating = it.ratingAverage,
            minPrice = it.minPrice.toDouble(),
            description = it.description,
        )
    }.toTypedArray()
}

// "spaceId": 3,
// "address": "nasrcity",
// "district": "cairo",
// "images": "test123",
// "roomNumbers": 4,
// "description": "test123",
// "name": "test",
// "contactNumber": "123",
// "minPrice": 10.0,
// "maxPrice": 20.0,
// "startTime": "10:00:00",
// "endTime": "12:00:00",
// "drinks": true,
// "owner": "weza",
// "outdoors": false,
// "ratingAverage": 4.0,
// "ratingList": []

