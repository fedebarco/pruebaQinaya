package com.example.pruebaqinaya

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel:ViewModel() {
    val value = MutableLiveData<String>()
    val countries= MutableLiveData<List<String>>(listOf("colombia"))
    val respuesta= MutableLiveData<String>("hola")

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://nacc.qinaya.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun searchCountries(){
        viewModelScope.launch {
            val call=getRetrofit().create(APIservice::class.java).getcountries("v2/mobile/countries").enqueue(object: Callback<List<countriesResponse>>{
                override fun onResponse(
                    call: Call<List<countriesResponse>>,
                    response: Response<List<countriesResponse>>
                ) {
                    respuesta.postValue(response.body().toString())
                }

                override fun onFailure(call: Call<List<countriesResponse>>, t: Throwable) {
                    respuesta.postValue("no funciono")
                }


            })
            //val countriesCall=call.body()

                //respuesta.postValue(countriesCall.toString())
                //val names=countriesCall?.id ?: emptyList()
                //countries.postValue(names)


        }


    }

}




