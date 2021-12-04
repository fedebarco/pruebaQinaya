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
    suspend fun userLogin(@Body requestBody: RequestBody):Response<ResponseBody>

}