package com.example.your_space.network.networkdatamodel

import android.os.Parcelable
import com.example.your_space.database.RatingsDB
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@JsonClass(generateAdapter = true)
@Parcelize
class RatingsProperty(
    @Json(name = "ratingId")
    val ratingId: String,
    @Json(name = "comment")
    val comment: String,
    @Json(name = "rating")
    val rating: String,
    @Json(name = "userId")
    val userId: String,
    @Json(name = "spaceId")
    val spaceId: String
) : Parcelable

fun List<RatingsProperty>.ratingPropertyModelToDatabaseModel(): Array<RatingsDB> {
    return map {
        RatingsDB(
            ratingId = it.ratingId,
            comment = it.comment,
            rating = it.rating,
            userId = it.userId,
            spaceId = it.spaceId
        )
    }.toTypedArray()
}

