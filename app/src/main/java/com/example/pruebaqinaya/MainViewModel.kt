package com.example.pruebaqinaya

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
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
    val toLogin = MutableLiveData<String>("")
    val pais = MutableLiveData<String>("Colombia")
    val moneda = MutableLiveData<String>("COP")
    val toregister = MutableLiveData<String>("hola")
    val userid=MutableLiveData<Long>(56)
    val UserComputers=MutableLiveData<List<computeresponse>>(listOf())
    val linkmaquina=MutableLiveData<String>()
    val trialinit=MutableLiveData<Boolean>(false)


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://nacc.qinaya.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun searchCountries() {
        viewModelScope.launch {
            getRetrofit().create(APIservice::class.java).getcountries("v2/mobile/countries")
                .enqueue(object : Callback<List<countriesResponse>> {
                    override fun onResponse(
                        call: Call<List<countriesResponse>>,
                        response: Response<List<countriesResponse>>
                    ) {
                        if (response.isSuccessful){
                                countries.postValue(response.body())
                        }
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

    fun rawJSON(user: String, password: String,navController: NavController,shared: SharedPreferences) {

        // Save the user

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
                    navController.navigate("main_page")
                    cimputerJSON(response.body()!!.user.id)
                    with(shared.edit()){
                        putString("email",user)
                        putString("password",password)
                        putString("id",response.body()!!.user.id.toString())
                        putString("active","true")

                        commit()
                    }
                    //cimputerJSON(response.body()!!.user.id,navController)

                   // navController.navigate("main_page")
                }
                else{
                    toLogin.postValue(response.code().toString())
                    toLogin.postValue(user)
                }

            }

            override fun onFailure(call: Call<userResponse>, t: Throwable) {
                toLogin.postValue(user)
            }

        })
        toLogin.postValue("r5rr0r")


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

        getRetrofit().create(APIservice::class.java).userRegister(requestBody=requestBody)
            .enqueue(object:Callback<RegisterResponse>{
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful){
                        toregister.postValue("registro completo verifica tu correo ")
                    }else{
                        toregister.postValue(response.code().toString())
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                }


            })

    }

    fun cimputerJSON(id: Long) {
        viewModelScope.launch {


            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("id", id)
            userid.postValue(id)


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
                            toLogin.postValue("1 error")
                            UserComputers.postValue(response.body())
                            toComputers.postValue(response.code().toString())
                            isTrialUsed(id)

                        }else{
                            toComputers.postValue("que pasa mijo")
                            toLogin.postValue("que pasa mijo")
                        }
                    }

                    override fun onFailure(call: Call<List<computeresponse>>, t: Throwable) {
                        toComputers.postValue("que pasa perro")
                        isTrialUsed(id)

                    }
                })
        }
    }

    fun actualizaJson(id: Long){

        val jsonObject = JSONObject()

        jsonObject.put("id", id)
        userid.postValue(id)
        toComputers.postValue(id.toString())


        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        getRetrofit().create(APIservice::class.java).userComputers(requestBody = requestBody)
            .enqueue(object : Callback<List<computeresponse>> {
                override fun onResponse(
                    call: Call<List<computeresponse>>,
                    response: Response<List<computeresponse>>
                ) {
                    toComputers.postValue("extrañisimo")
                    if (response.isSuccessful) {
                            toComputers.postValue("extrañisimo")
                            //UserComputers.postValue(response.body())
                            //toComputers.postValue(response.code().toString())
                            //isTrialUsed(id)

                    } else {
                            toComputers.postValue("que pasa mijo")
                    }
                }

                override fun onFailure(call: Call<List<computeresponse>>, t: Throwable) {
                    toComputers.postValue("que pasa perro")
                }
            })



    }

    fun startTrialJson(id: Long){
        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("id", id)


        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        getRetrofit().create(APIservice::class.java).userTrial(requestBody = requestBody)
            .enqueue(object:Callback<trialresponse>{
                override fun onResponse(
                    call: Call<trialresponse>,
                    response: Response<trialresponse>
                ) {

                }

                override fun onFailure(call: Call<trialresponse>, t: Throwable) {

                }

            })
    }

    fun isTrialUsed(id: Long){
        val jsonObject = JSONObject()
        jsonObject.put("id", id)


        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        getRetrofit().create(APIservice::class.java).userPrueba(requestBody = requestBody)
            .enqueue(object:Callback<pruebaRsponse>{
                override fun onResponse(
                    call: Call<pruebaRsponse>,
                    response: Response<pruebaRsponse>
                ) {
                    if (response.isSuccessful){
                        if (response.body()!!.usedTrial=="True"){
                            trialinit.postValue(false)
                        }else{
                            trialinit.postValue(true)
                        }
                    }
                }

                override fun onFailure(call: Call<pruebaRsponse>, t: Throwable) {
                }
            })

    }
}



