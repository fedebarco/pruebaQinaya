package com.example.pruebaqinaya

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface APIservice {
    @GET
    fun getcountries(@Url url:String): Call<List<countriesResponse>>
}