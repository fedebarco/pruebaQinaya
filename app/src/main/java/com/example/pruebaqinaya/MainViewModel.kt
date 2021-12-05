package com.example.pruebaqinaya

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel:ViewModel() {
    val value = MutableLiveData<String>()
    val countries = MutableLiveData<List<countriesResponse>>(listOf())
    val toComputers= MutableLiveData<String>("hola")
    val toLogin = MutableLiveData<String>("hola")
    val pais = MutableLiveData<String>("Colombia")
    val moneda = MutableLiveData<String>("COP")
    val toregister = MutableLiveData<String>("hola")
    val userMain=MutableLiveData<userResponse>()
    val UserComputers=MutableLiveData<List<computeresponse>>(listOf())


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://nacc.qinaya.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun searchCountries() {
        viewModelScope.launch {
            val call =
                getRetrofit().create(APIservice::class.java).getcountries("v2/mobile/countries")
                    .enqueue(object : Callback<List<countriesResponse>> {
                        override fun onResponse(
                            call: Call<List<countriesResponse>>,
                            response: Response<List<countriesResponse>>
                        ) {
                            countries.postValue(response.body())
                        }

                        override fun onFailure(call: Call<List<countriesResponse>>, t: Throwable) {
                        }


                    })
            //val countriesCall=call.body()

            //respuesta.postValue(countriesCall.toString())
            //val names=countriesCall?.id ?: emptyList()
            //countries.postValue(names)


        }


    }

    fun rawJSON(user: String, password: String,navController: NavController) {

        // Create JSON using JSONObjec
        val jsonObject = JSONObject()
        jsonObject.put("email", user)
        jsonObject.put("password", password)

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())


        getRetrofit().create(APIservice::class.java).userLogin(requestBody = requestBody).enqueue(object:Callback<userResponse>{
            override fun onResponse(call: Call<userResponse>, response: Response<userResponse>) {
                if (response.isSuccessful){
                    toComputers.postValue(response.body()!!.user.id.toString())
                    cimputerJSON(response.body()!!.user.id,navController)
                   // navController.navigate("main_page")
                }
                else{
                    toLogin.postValue(response.code().toString())
                }

            }

            override fun onFailure(call: Call<userResponse>, t: Throwable) {

            }

        })


    }

    fun registerJSON(
        name: String,
        user: String,
        country: Int,
        phone: String,
        sub: Int,
        curency: Int,
        password: String
    ) {

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("name", name)
        jsonObject.put("email", user)
        jsonObject.put("country", country)
        jsonObject.put("phone", phone)
        jsonObject.put("subscription", sub)
        jsonObject.put("currency", curency)
        jsonObject.put("password", password)


        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        viewModelScope.launch {
            // Do the POST request and get response
            val response =
                getRetrofit().create(APIservice::class.java).userRegister(requestBody = requestBody)

            withContext(Dispatchers.Main.immediate) {
                if (response.isSuccessful) {

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )
                    toregister.postValue(prettyJson)

                } else {
                    toregister.postValue(response.code().toString())

                }

            }

        }
    }

    fun cimputerJSON(id: Long,navController: NavController) {
        viewModelScope.launch {


            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("id", id)


            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()

            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())



            // Do the POST request and get response

            getRetrofit().create(APIservice::class.java).userComputers(requestBody = requestBody)
                .enqueue(object:Callback<List<computeresponse>> {
                    override fun onResponse(
                        call: Call<List<computeresponse>>,
                        response: Response<List<computeresponse>>
                    ) {
                        if (response.isSuccessful){
                            UserComputers.postValue(response.body())
                            toComputers.postValue(response.code().toString())
                            toComputers.postValue("que pasa mijo")
                            navController.navigate("main_page")
                        }else{
                            toComputers.postValue("que pasa mijo")
                        }
                    }

                    override fun onFailure(call: Call<List<computeresponse>>, t: Throwable) {
                        toComputers.postValue("que pasa perro")

                    }
                })
        }
    }
}



