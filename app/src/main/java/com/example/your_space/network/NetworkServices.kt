package com.example.your_space.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.your_space.network.networkdatamodel.BookingProperty
import com.example.your_space.network.networkdatamodel.SpaceItemProperty
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

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

    @GET("spaces/allspaces/{page}")
    suspend fun getWorkingSpacesUsingPaging(@Path("page") page: String): List<SpaceItemProperty>

    @GET("bookings")
    suspend fun getAllBookings(): List<BookingProperty>


    // for testing purposes (returns raw string of the above calls)
    @GET("bookings")
    suspend fun getAllBookingsAsString(): String

    @GET("spaces")
    suspend fun getAllWorkingSpacesAsString(): String
}

object Network {
    val NetworkServices: YourAppApiInterface by lazy {
        retrofit.create(YourAppApiInterface::class.java)
    }
}


