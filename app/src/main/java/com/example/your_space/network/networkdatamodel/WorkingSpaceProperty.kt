package com.example.your_space.network.networkdatamodel

import android.os.Parcelable
import com.example.your_space.database.WorkingSpaceDB
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
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
    val minPrice: Double = 0.0,
    @Json(name = "maxPrice")
    val maxPrice: Double = 0.0,
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

fun List<SpaceItemProperty>.workingSpacesPropertyModelToDatabaseModel(): Array<WorkingSpaceDB> {
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

