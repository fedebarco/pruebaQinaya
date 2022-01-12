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



    fun searchCountries() {
        viewModelScope.launch {
            try {
                val countriesResult = qinayaApi.retrofitService.getcountries("v2/mobile/countries")
                countries.postValue(countriesResult)
            } catch (e: Exception) {
            }
        }


    }

    fun rawJSON(user: String, password: String,navController: NavController,shared: SharedPreferences) {

        viewModelScope.launch {
            // Save the user

            // Create JSON using JSONObjec
            val jsonObject = JSONObject()
            jsonObject.put("email", user)
            jsonObject.put("password", password)

            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()

            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            try {
                val userResult = qinayaApi.retrofitService.userLogin(requestBody = requestBody)
                toComputers.postValue(userResult.user.id.toString())
                navController.navigate("main_page")
                cimputerJSON(userResult.user.id)
                with(shared.edit()) {
                    putString("email", user)
                    putString("password", password)
                    putString("id", userResult.user.id.toString())
                    putString("active", "true")

                    commit()
                }
            }catch (e: Exception) {
                toLogin.postValue(e.toString())
            }
        }


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

        viewModelScope.launch {

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

            try {
                val registerResult = qinayaApi.retrofitService.userRegister(requestBody = requestBody)
                toregister.postValue("registro completo verifica tu correo ")
            } catch (e: Exception) {
                toregister.postValue(e.toString())
            }
        }

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
            try {
                val computerResult =
                    qinayaApi.retrofitService.userComputers(requestBody = requestBody)
                toLogin.postValue("1 error")
                UserComputers.postValue(computerResult)
                toComputers.postValue("1 error")
                isTrialUsed(id)

            } catch (e: Exception) {
                toComputers.postValue(e.toString())
                toLogin.postValue(e.toString())
            }
        }

    }


    fun startTrialJson(id: Long){
        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("id", id)


        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

    }

    fun isTrialUsed(id: Long){
        viewModelScope.launch {
            val jsonObject = JSONObject()
            jsonObject.put("id", id)


            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()

            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            try {
                val trialResult = qinayaApi.retrofitService.userPrueba(requestBody = requestBody)
                if (trialResult.usedTrial == "True") {
                    trialinit.postValue(false)
                } else {
                    trialinit.postValue(true)
                }
            } catch (e: Exception) {
                toregister.postValue(e.toString())
            }
        }

    }
}



