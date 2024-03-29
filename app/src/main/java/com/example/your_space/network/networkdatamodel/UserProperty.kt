package com.example.your_space.network.networkdatamodel

import android.os.Parcelable
import com.example.your_space.database.UserDB
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class UserProperty(
    @Json(name = "userId")
    val userId: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "firstName")
    val firstName: String,
    @Json(name = "lastName")
    val lastName: String,
    @Json(name = "mobileNo")
    val mobileNo: String,
    @Json(name = "address")
    val address: String,
    @Json(name = "birthDate")
    val birthDate: String,
    @Json(name = "bio")
    val bio: String?,
    @Json(name = "points")
    val points: Int,
    @Json(name = "username")
    val userName: String,
) : Parcelable

fun UserProperty.userPropertyModelToDatabaseModel(): UserDB {
    return UserDB(
        userId = this.userId,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        mobileNo = this.mobileNo,
        address = this.address,
        birthDate = this.birthDate,
        bio = this.bio ?: "",
        points = this.points,
        userName = this.userName,
        password = ""
    )
}

fun List<UserProperty>.userPropertyModelToDatabaseModel(): Array<UserDB> {
    return map {
        UserDB(
            userId = it.userId,
            email = it.email,
            firstName = it.firstName,
            lastName = it.lastName,
            mobileNo = it.mobileNo,
            address = it.address,
            birthDate = it.birthDate,
            bio = it.bio ?: "",
            points = it.points,
            userName = it.userName,
            password = ""
        )
    }.toTypedArray()
}

@JsonClass(generateAdapter = true)
@Parcelize
data class UserPropertyPost(
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
    @Json(name = "username")
    val username: String,
    @Json(name = "bio")
    val bio: String = "",
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class UserPropertyUpdate(
    @Json(name = "userId")
    val userId: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "firstName")
    val firstName: String,
    @Json(name = "lastName")
    val lastName: String,
    @Json(name = "mobileNo")
    val mobileNo: String,
    @Json(name = "address")
    val address: String,
    @Json(name = "birthDate")
    val birthDate: String,
    @Json(name = "username")
    val username: String,
    @Json(name = "bio")
    val bio: String,
    @Json(name = "points")
    val points: Int
) : Parcelable