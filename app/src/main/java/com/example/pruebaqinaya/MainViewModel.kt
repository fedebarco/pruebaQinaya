package com.example.pruebaqinaya

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.pruebaqinaya.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class MainViewModel:ViewModel() {
    val preferencesUser=MutableLiveData<SharedPreferences>()
    val value = MutableLiveData<String>()
    val countries = MutableLiveData<List<CountriesResponse>>(listOf())
    val userid=MutableLiveData<Long>(56)
    val UserComputers=MutableLiveData<List<UserMachine>>(listOf())
    val UserCom1=MutableLiveData<UserMachine>()
    val linkmaquina=MutableLiveData<String>()
    val trialinit=MutableLiveData<Boolean>(false)
    val userdefault=MutableLiveData<DefaultResponse>()
    val responsemaquina=MutableLiveData<String>()
    var misession=""
    val loading= mutableStateOf(false)


     fun checkLogin(shared : SharedPreferences):String{
        loadPreferences(shared)
        val beginA = if (shared.getString("active","") == "true"){
            "main_page"
        }else{
            "login_qinaya"
        }
        return beginA
    }

    fun loadPreferences(shared : SharedPreferences){
        preferencesUser.postValue(shared)
    }


    fun searchCountries(countriesQ: (List<CountriesResponse>) -> Unit) {
        viewModelScope.launch {
            try {
                val countriesResult = qinayaApi.retrofitService.getcountries("v2/mobile/countries")
                countries.postValue(countriesResult)
                countriesQ(countriesResult)
            } catch (e: Exception) {
            }
        }


    }

    fun rawJSON(login: Login,navController: NavController,shared: SharedPreferences,textRR:(String)->Unit) {

        viewModelScope.launch {
            // Save the user
            loading.value=true


            try {
                val userResult = qinayaApi.retrofitService.userLogin(login = login)
                textRR("conectando")
                navController.navigate("main_page")
                with(shared.edit()) {
                    putString("email", login.email)
                    putString("password", login.password)
                    putString("id", userResult.user.id.toString())
                    putString("active", "true")

                    commit()
                }
            }catch (e: Exception) {
                textRR(e.toString())
            }
            loading.value=false
        }


    }

    fun registerJSON(registration: Registration,textRR:(String)->Unit) {

        viewModelScope.launch {
            try {
                qinayaApi.retrofitService.userRegister(registration=registration)
                textRR("registro completo verifica tu correo ")
            } catch (e: Exception) {
                textRR(e.toString())
            }
        }

    }

    fun startJSON(id: Long) {

        viewModelScope.launch() {

                // Create JSON using JSONObject
                val jsonObject = JSONObject()
                jsonObject.put("user_id", id)
                jsonObject.put("compu_id", userdefault.value!!.userMachine.id.toString())
                jsonObject.put("session", userdefault.value!!.session)
                misession=userdefault.value!!.session


                // Convert JSONObject to String
                val jsonObjectString = jsonObject.toString()

                // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
                val requestBody =
                    jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())


                // Do the POST request and get response
                try {
                    qinayaApi.retrofitService.startMachine(requestBody = requestBody)
                    responsemaquina.postValue("empezo")
                } catch (e: Exception) {
                    responsemaquina.postValue(e.toString())

                }

        }

    }


    fun endJSON(navController: NavController) {

        viewModelScope.launch() {

            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("compu_session", misession)


            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()

            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody =
                jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())


            // Do the POST request and get response
            try {
                qinayaApi.retrofitService.endMachine(requestBody = requestBody)
            } catch (e: Exception) {

            }
            navController.navigate("main_page")

        }

    }


    fun cimputerJSON(id: Long,computers: (List<UserMachine>) -> Unit) {


        viewModelScope.launch {
            loading.value=true
            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("id", id)
            userid.postValue(id)


            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()

            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())


            // Do the POST request and get responsec
            try {
                val defaultResult = qinayaApi.retrofitService.getDefault(requestBody = requestBody)
                val buycompus=UserMachine(0,false,"Compu 2","o","o","compu ejemplo",false,"escoge",",","","",null)
                val computerResult= listOf(defaultResult.userMachine,buycompus)
                UserComputers.postValue(computerResult)
                computers(computerResult)
                userdefault.postValue(defaultResult)
                isTrialUsed(id)

            } catch (e: Exception) {

            }
            loading.value=false
        }

    }

    fun updatePassword(pOld:String,pNew:String,textRR:(String)->Unit){
        viewModelScope.launch {
            try {
                val changeU=PasswordChange(preferencesUser.value!!.getString("id","")!!.toLong(),pOld,pNew)
                val trialResult = qinayaApi.retrofitService.changePasswordByUser(changeU)
                textRR("se cambio la clave")

            }catch (e: Exception){
                textRR(e.toString())

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


    fun recoverPassword(email:String,textRR:(String)->Unit){
        viewModelScope.launch {
            try {
                val trialResult = qinayaApi.retrofitService.recoverPasswordByUser(email =email)
                textRR("se envio email de recuperacion")

            }catch (e: Exception){
                textRR(e.toString())

            }

        }

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
            }
        }

    }
}



