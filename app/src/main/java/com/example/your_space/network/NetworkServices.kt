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

const val PAGE_SIZE = 50
const val IMAGE_VS_SPACEID_ENDPOINT = "api/images/space/"
const val API = "api/"
const val LOGIN = "login"

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
    @GET("${API}user")
    suspend fun getAllUsers(): List<UserProperty>

    @POST(LOGIN)
    suspend fun loginWithUsernameAndPassword(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<UserProperty>

    @POST("${API}user")
    suspend fun addNewUser(@Body newUser: UserPropertyPost): Response<ResponseBody>

    @PUT("${API}user/{userId}")
    suspend fun updateUser(
        @Body newUser: UserPropertyUpdate,
        @Header("Cookie") cookie: String,
        @Path("userId") userId: String
    ): Response<ResponseBody>

    @GET("${API}spaces")
    suspend fun getAllWorkingSpaces(): List<SpaceItemProperty>

    //    @Headers(
//        "Cookie:JSESSIONID=A2B46CE2EAFDCE3C8D978A1576F72A22"
//    )
    @GET("${API}spaces/alldata/{page}/$PAGE_SIZE")
    suspend fun getWorkingSpacesUsingPaging(
        @Path("page") page: Int,
        @Header("Cookie") cookie: String
    ): List<SpaceItemProperty>

    @GET("${API}bookings")
    suspend fun getAllBookings(): List<BookingProperty>

    @GET("${API}room/getBySpace/{spaceId}")
    suspend fun getRoomsBySpaceId(
        @Path("spaceId") spaceId: String,
        @Header("Cookie") cookie: String
    ): List<WorkingSpaceRoomProperty>

    @DELETE("${API}bookings/{id}")
    suspend fun cancelBooking(
        @Path("id") id: Int,
        @Header("Cookie") cookie: String
    ): Response<ResponseBody>

    @POST("${API}bookings")
    suspend fun addNewBooking(
        @Body newBooking: BookingPropertyPost,
        @Header("Cookie") cookie: String
    ): Response<ResponseBody>

    @POST("${API}message")
    suspend fun postMessage(
        @Query("userId") userId: String,
        @Query("message") message: String,
        @Header("Cookie") cookie: String
    ): Response<ResponseBody>
}

object Network {
    val NetworkServices: YourAppApiInterface by lazy {
        retrofit.create(YourAppApiInterface::class.java)
    }
}


