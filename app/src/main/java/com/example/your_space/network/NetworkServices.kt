package com.example.your_space.network

import com.example.your_space.network.networkdatamodel.BookingProperty
import com.example.your_space.network.networkdatamodel.SpaceItemProperty
import com.example.your_space.network.networkdatamodel.WorkingSpaceRoomProperty
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

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

    @DELETE("bookings/{id}")
    suspend fun cancelBooking(@Path("id") id : Int): Response<ResponseBody>
}

object Network {
    val NetworkServices: YourAppApiInterface by lazy {
        retrofit.create(YourAppApiInterface::class.java)
    }
}


