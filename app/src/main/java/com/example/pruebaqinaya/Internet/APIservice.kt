package com.example.pruebaqinaya

import com.example.pruebaqinaya.data.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://qinaya.co/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface APIservice {
    @GET
    suspend fun getcountries(@Url url:String): List<CountriesResponse>

    @POST("v2/mobile/default")
    suspend fun getDefault(@Body requestBody: RequestBody): DefaultResponse

    @POST ("v2/mobile/login")
    suspend fun userLogin(@Body login:Login):UserResponse

    @POST ("v2/mobile/register")
    suspend fun userRegister(@Body registration: Registration): RegisterResponse

    @POST ("v2/mobile/user_home")
    suspend fun userComputers(@Body requestBody: RequestBody):List<ComputeResponse>

    @POST ("v2/mobile/start_trial")
    fun userTrial(@Body requestBody: RequestBody):Call<TrialResponse>

    @POST ("v2/mobile/start_trial")
    suspend fun userPrueba(@Body requestBody: RequestBody): PruebaResponse

    @POST ("v2/mobile/start_session")
    suspend fun startMachine(@Body requestBody: RequestBody)

    @POST ("v2/mobile/end_session")
    suspend fun endMachine(@Body requestBody: RequestBody)

    @POST ("v2/mobile/start_session")
    suspend fun errorMachine(@Body requestBody: RequestBody):ErrorResponse
}

object qinayaApi {
    val retrofitService : APIservice by lazy { retrofit.create(APIservice::class.java) }
}