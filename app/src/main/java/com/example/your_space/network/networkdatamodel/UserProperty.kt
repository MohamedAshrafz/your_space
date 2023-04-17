package com.example.your_space.network.networkdatamodel

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class UserProperty (
        @Json(name = "userId")
        val userId: Int,
        @Json(name = "email")
        val email: String,
        @Json(name = "firstName")
        val firstName: String,
        @Json(name = "lastName")
        val lastName: String,
        @Json(name = "password")
        val password: String,
        @Json(name = "mobileNo")
        val mobileNo: String,
        @Json(name = "address")
        val address: String,
        @Json(name = "birthDate")
        val birthDate: String,
        @Json(name = "bio")
        val bio: String,
        @Json(name = "points")
        val points: Int,
        @Json(name = "picture")
        val picture: String
        ) :Parcelable
