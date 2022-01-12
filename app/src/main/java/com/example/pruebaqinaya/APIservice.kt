package com.example.pruebaqinaya

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

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
    suspend fun getcountries(@Url url:String): List<countriesResponse>


    @POST ("v2/mobile/login")
    suspend fun userLogin(@Body requestBody: RequestBody):userResponse

    @POST ("v2/mobile/register")
    suspend fun userRegister(@Body requestBody: RequestBody):RegisterResponse

    @POST ("v2/mobile/user_home")
    suspend fun userComputers(@Body requestBody: RequestBody):List<computeresponse>

    @POST ("v2/mobile/start_trial")
    fun userTrial(@Body requestBody: RequestBody):Call<trialresponse>

    @POST ("v2/mobile/start_trial")
    suspend fun userPrueba(@Body requestBody: RequestBody):pruebaRsponse

}

object qinayaApi {
    val retrofitService : APIservice by lazy { retrofit.create(APIservice::class.java) }
}