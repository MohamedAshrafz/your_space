package com.example.your_space.network

import com.example.your_space.network.networkdatamodel.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

const val PAGE_SIZE = 4
const val IMAGE_VS_SPACEID_ENDPOINT = "images/space/"
const val DEFAULT_IMAGE_INDEX = "0"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .baseUrl(Constants.BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface YourAppApiInterface {
    @GET("spaces")
    suspend fun getAllWorkingSpaces(): List<SpaceItemProperty>

    @GET("spaces/alldata/{page}/$PAGE_SIZE")
    suspend fun getWorkingSpacesUsingPaging(@Path("page") page: Int): List<SpaceItemProperty>

    @GET("bookings")
    suspend fun getAllBookings(): List<BookingProperty>

    @GET("room/getBySpace/{spaceId}")
    suspend fun getRoomsBySpaceId(@Path("spaceId") spaceId : String): List<WorkingSpaceRoomProperty>

    @GET("ratings")
    suspend fun getRatingBySpaceId() : List<RatingProperty>

    @DELETE("bookings/{id}")
    suspend fun cancelBooking(@Path("id") id : Int): Response<ResponseBody>

    @POST("bookings")
    suspend fun addNewBooking(@Body newBooking: BookingPropertyPost) : Response<ResponseBody>
}

object Network {
    val NetworkServices: YourAppApiInterface by lazy {
        retrofit.create(YourAppApiInterface::class.java)
    }
}


