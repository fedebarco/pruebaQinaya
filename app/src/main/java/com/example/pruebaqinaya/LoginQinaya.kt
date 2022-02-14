package com.example.pruebaqinaya

import android.content.SharedPreferences
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pruebaqinaya.data.CountriesResponse
import com.example.pruebaqinaya.data.Login
import com.example.pruebaqinaya.data.Registration
import kotlinx.coroutines.launch

@Composable
fun LoginQinaya(model: MainViewModel, navController: NavController, shared: SharedPreferences){
    var textUser by remember { mutableStateOf("") }
    var textPassword by remember { mutableStateOf("") }
    val showPassword= remember { mutableStateOf(false) }
    val (textResponse,setTextResponse)= remember { mutableStateOf("") }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val checkedState= remember{ mutableStateOf(false) }
    val backHandlingEnabled by remember { mutableStateOf(true) }
    BackHandler(backHandlingEnabled) {
        // Handle back press
    }


    Scaffold(
        scaffoldState = scaffoldState,
        content = {
            Box(modifier= Modifier.fillMaxSize()) {
                Image(painter = painterResource(id = R.drawable.back), contentDescription ="" )
            }
            Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .height(IntrinsicSize.Max)
                .border(1.dp, Color.Black)
            ) {
                Text(text = "Bienvenido.",fontSize = 30.sp)
                OutlinedTextField(value = textUser, onValueChange ={textUser=it},label = { Text(text = "Usuario:") },modifier = Modifier
                    .padding(horizontal = 50.dp, vertical = 8.dp)
                    .fillMaxWidth() )
                TextButton(onClick = { navController.navigate("recover_password") }) {
                    Text("¿olvidaste tu contraseña?")
                }
                OutlinedTextField(
                    value = textPassword,
                    onValueChange ={textPassword=it},
                    label = { Text(text = "Contraseña") },
                    modifier = Modifier
                        .padding(horizontal = 50.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    trailingIcon = {
                        if(showPassword.value){
                            IconButton(onClick = { showPassword.value=false }) {
                                Icon(imageVector = Icons.Filled.Visibility,contentDescription = null) }
                        }else{
                            IconButton(onClick = { showPassword.value=true }) {
                                Icon(imageVector = Icons.Filled.VisibilityOff,contentDescription = null)
                            }
                        }
                    },
                    visualTransformation = if(showPassword.value){
                        VisualTransformation.None
                    }else{
                        PasswordVisualTransformation()
                    }
                )
                Button(
                    modifier = Modifier
                        .padding(horizontal = 50.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    onClick = {
                        val login= Login(textUser,textPassword)
                        model.rawJSON(login,navController,shared,setTextResponse)
                    }
                ) {
                    Text("Ingresa")
                }
                Row {
                    Checkbox(checked = checkedState.value , onCheckedChange ={checkedState.value=it} )
                    Text(text = "Recuerdame")
                }
                TextButton(onClick = { navController.navigate("register_qinaya") }) {
                    Text("Registrate")
                }
                textResponse.let {
                    if (it!=""){
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Error: $it")
                        }

                    }
                }

            }
        }
    )
}

@Composable
fun RegisterScreen(model: MainViewModel,navController: NavController){
    var textname by remember { mutableStateOf("") }
    var textEmail by remember { mutableStateOf("") }
    var textPhone by remember { mutableStateOf("") }
    var textPassword by remember { mutableStateOf("") }
    var textPassword2 by remember { mutableStateOf("") }
    val (countiQ,setCountiQ)= remember { mutableStateOf(listOf<CountriesResponse>()) }
    val scrollState= rememberScrollState()
    val (textPais,setTextPais)= remember { mutableStateOf("Colombia")}
    val textresponse by model.toregister.observeAsState()
    val (textMoneda,setTextMoneda )=remember { mutableStateOf("COP") }
    val (showDialog, setShowDialog) =  remember { mutableStateOf(false) }
    val (showDialog1, setShowDialog1) =  remember { mutableStateOf(false) }
    model.searchCountries(setCountiQ)
    Scaffold(
        topBar = {AppBarReturn(navController =navController , pageM ="login_qinaya")},
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(IntrinsicSize.Max)
                ) {
                    Text(text = "Crea tu cuenta", fontSize = 30.sp)
                    OutlinedTextField(value = textname,
                        onValueChange = { textname = it },
                        label = { Text(text = "Nombre:") },
                        modifier = Modifier
                            .padding(horizontal = 50.dp, vertical = 8.dp)
                            .fillMaxWidth()
                    )
                    OutlinedTextField(value = textEmail,
                        onValueChange = { textEmail = it },
                        label = { Text(text = "Email") },
                        modifier = Modifier
                            .padding(horizontal = 50.dp, vertical = 8.dp)
                            .fillMaxWidth()
                    )
                    OutlinedTextField(value = textPassword,
                        onValueChange = { textPassword = it },
                        label = { Text(text = "contraseña:") },
                        modifier = Modifier
                            .padding(horizontal = 50.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    OutlinedTextField(value = textPassword2,
                        onValueChange = { textPassword2 = it },
                        label = { Text(text = "Confirmar contraseña") },
                        modifier = Modifier
                            .padding(horizontal = 50.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    OutlinedTextField(value = textPhone,
                        onValueChange = { textPhone = it },
                        label = { Text(text = "telefono") },
                        modifier = Modifier
                            .padding(horizontal = 50.dp, vertical = 8.dp)
                            .fillMaxWidth()
                    )
                    OutlinedButton(
                        modifier = Modifier
                            .padding(horizontal = 50.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        onClick = { setShowDialog(true) }
                    ) {
                        Text(textPais)
                        Icon(Icons.Filled.ExpandMore, contentDescription = "")
                    }
                    OutlinedButton(
                        modifier = Modifier
                            .padding(horizontal = 50.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        onClick = { setShowDialog1(true) }
                    ) {
                        Text(textMoneda)
                        Icon(Icons.Filled.ExpandMore, contentDescription = "")
                    }
                    Button(
                        modifier = Modifier
                            .padding(horizontal = 50.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        onClick = {
                            if (textPassword == textPassword2) {
                                val registration = Registration(
                                    textname,
                                    textEmail,
                                    1,
                                    textPhone,
                                    1,
                                    1,
                                    textPassword
                                )
                                model.registerJSON(registration = registration)
                            } else {
                                model.toregister.postValue("las contraseñas no coinciden")
                            }
                        }
                    ) {
                        Text("Registrate")
                    }
                    Text("$textresponse")

                    MonedaDemo(showDialog1, setShowDialog1, setTextMoneda)
                    CountriesScreen(showDialog, setShowDialog, countiQ,setTextPais)
                }
            }
        }
    )

}
@Composable
fun MonedaDemo(showDialog: Boolean, setShowDialog: (Boolean) -> Unit,setMoneda:(String)->Unit) {
    DropdownMenu(expanded = showDialog, onDismissRequest = {setShowDialog(false)}) {
        DropdownMenuItem(onClick = {
            setShowDialog(false)
            setMoneda("COP")
        }) {
            Text("COP")
        }
        DropdownMenuItem(onClick = {
            setShowDialog(false)
            setMoneda("USD")
        }) {
            Text("USD")
        }

    }
}

@Composable
fun CountriesScreen(showDialog: Boolean, setShowDialog: (Boolean) -> Unit,counQ:List<CountriesResponse>,setPais:(String)->Unit) {
    val countriesNames=counQ
    val countriesn= mutableListOf<String>()
    for (i in countriesNames){
        countriesn.add(i.name)
    }
    DropdownMenu(expanded = showDialog, onDismissRequest = {setShowDialog(false)}) {
        for (item in countriesn){
            MyCountries(name = item,setShowDialog=setShowDialog,setPais=setPais )
        }
    }

}
@Composable
fun RecoverPassword(navController: NavController){
    Scaffold(
        topBar = {AppBarReturn(navController =navController , pageM ="login_qinaya")},
        content = {
            val (email, setEmail) = remember { mutableStateOf("") }
            Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .height(IntrinsicSize.Max)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .border(1.dp, Color.Black)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Recupera contraseña")
                        OutlinedTextField(value = email, onValueChange = { setEmail(it) })
                        Button(onClick = { }) {
                            Text(text = "Enviar")

                        }


                    }
                }
            }
        }
    )
}

@Composable
fun MyCountries(name:String,setShowDialog: (Boolean) -> Unit,setPais: (String) -> Unit){
    DropdownMenuItem(
        onClick = { setPais(name)
            setShowDialog(false)

        }) {
        Text(text = name)

    }
}


