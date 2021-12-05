package com.example.pruebaqinaya

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface APIservice {
    @GET
    fun getcountries(@Url url:String): Call<List<countriesResponse>>


    @POST ("v2/mobile/login")
    fun userLogin(@Body requestBody: RequestBody):Call<userResponse>

    @POST ("v2/mobile/register")
    suspend fun userRegister(@Body requestBody: RequestBody):Response<ResponseBody>

    @POST ("v2/mobile/user_home")
    fun userComputers(@Body requestBody: RequestBody):Call<List<computeresponse>>

}