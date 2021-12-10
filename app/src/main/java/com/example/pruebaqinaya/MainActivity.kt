package com.example.pruebaqinaya


import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pruebaqinaya.ui.theme.PruebaQinayaTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: MainViewModel by viewModels()
        model.searchCountries()
        setContent {
            PruebaQinayaTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainNavigation(model = model)
                }
            }
        }
    }
}

@Composable
fun MainNavigation(model: MainViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login_qinaya") {
        composable("login_qinaya") { LoginQinaya(model = model,navController = navController) }
        composable("register_qinaya") { RegisterScreen(model = model) }
        composable("main_page") { MainPage(navController = navController,model = model)}
        composable("remoto") { Remoto(model=model)}
        }

    }



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun LoginQinaya(model: MainViewModel,navController: NavController){
    var textUser by remember { mutableStateOf("") }
    var textPassword by remember { mutableStateOf("") }
    val showPassword= remember { mutableStateOf(false) }
    val textResponse by model.toLogin.observeAsState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        content = {Column(modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)) {
            Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ) {
                OutlinedTextField(value = textUser, onValueChange ={textUser=it},label = { Text(text = "Usuario:")} )
                OutlinedTextField(
                    value = textPassword,
                    onValueChange ={textPassword=it},
                    label = { Text(text = "Contraseña")},
                    trailingIcon = {
                        if(showPassword.value){
                            IconButton(onClick = { showPassword.value=false }) {
                                Icon(imageVector =Icons.Filled.Visibility,contentDescription = null) }
                        }else{
                            IconButton(onClick = { showPassword.value=true }) {
                                Icon(imageVector =Icons.Filled.VisibilityOff,contentDescription = null)
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
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = {
                        model.rawJSON(textUser,textPassword,navController)
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Error: $textResponse")
                        }
                    }
                ) {
                    Text("Ingresa")
                }
                TextButton(onClick = { navController.navigate("register_qinaya") }) {
                    Text("registrate")
                }
                Text("$textResponse")
            }

        }
        }
    )




}

@Composable
fun RegisterScreen(model: MainViewModel){
    var textname by remember { mutableStateOf("") }
    var textEmail by remember { mutableStateOf("") }
    var textPhone by remember { mutableStateOf("") }
    var textPassword by remember { mutableStateOf("") }
    var textPassword2 by remember { mutableStateOf("") }
    val textPais by model.pais.observeAsState()
    val textresponse by model.toregister.observeAsState()
    val textMoneda by model.moneda.observeAsState()
    val (showDialog, setShowDialog) =  remember { mutableStateOf(false) }
    val (showDialog1, setShowDialog1) =  remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp)) {
        Column (verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
                ) {
            Text(text = "Crea tu cuenta")
            OutlinedTextField(value = textname, onValueChange ={textname=it},label = { Text(text = "Nombre:")} )
            OutlinedTextField(value = textEmail, onValueChange ={textEmail=it},label = { Text(text = "Email")} )
            OutlinedTextField(value = textPassword, onValueChange ={textPassword=it},label = { Text(text = "contraseña:")} )
            OutlinedTextField(value = textPassword2, onValueChange ={textPassword2=it},label = { Text(text = "Confirmar contraseña")} )
            OutlinedTextField(value = textPhone, onValueChange ={textPhone=it},label = { Text(text = "telefono")} )
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onClick = {setShowDialog(true)}
            ) {
                Text("$textPais")
            }
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onClick = {setShowDialog1(true)}
            ) {
                Text("$textMoneda")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (textPassword==textPassword2){
                        model.registerJSON(textname,textEmail,1,textPhone,1,1,textPassword)
                    }else{
                        model.toregister.postValue("las contraseñas no coinciden")
                    }
                }
            ) {
                Text("Registrate")
            }
            Text("$textresponse")

            DialogDemo(showDialog1, setShowDialog1,model)
            CountriesScreen(showDialog, setShowDialog,model)
        }


    }

}

@Composable
fun DialogDemo(showDialog: Boolean, setShowDialog: (Boolean) -> Unit,model: MainViewModel) {
    DropdownMenu(expanded = showDialog, onDismissRequest = {setShowDialog(false)}) {
        DropdownMenuItem(onClick = {
            setShowDialog(false)
            model.moneda.postValue("COP")
        }) {
            Text("COP")
        }
        DropdownMenuItem(onClick = {
            setShowDialog(false)
            model.moneda.postValue("USD")
        }) {
            Text("USD")
        }

    }
}



@Composable
fun MainPage(navController: NavController,model: MainViewModel) {
    val computadoras=model.UserComputers.value!!
    val textresponse=model.toComputers.observeAsState()
    val (showDialog, setShowDialog) =  remember { mutableStateOf(model.trialinit.value!!) }

    Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(MaterialTheme.colors.background)) {
        Text("$textresponse")
        LazyColumn{
            items(computadoras){micompu->
                MyLinux(name = micompu.userMachine.nombreMaquina,link =micompu.userMachine.url,navController = navController,model = model)
            }
        }
        AlertDialogTrial(model,showDialog, setShowDialog)
    }


}

@Composable
fun Remoto(model: MainViewModel){

    val url=model.linkmaquina.value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(MaterialTheme.colors.primary)
        ) {
            Text(
                text = "$url",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column {
            AndroidView(
                factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,

                        )
                        this.settings.javaScriptEnabled = true
                        this.webViewClient = WebViewClient()

                    }
                }, update = {
                    it.loadUrl("$url")
                }
            )
        }
    }
}
@Composable
fun AlertDialogTrial(model: MainViewModel,showDialog: Boolean, setShowDialog: (Boolean) -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                            setShowDialog(false)
                        },
            title = {
                            Text(text = "Prueba")
                        },
            text = {
                            Text("tienes una maquina de prueba ")
                        },
            confirmButton = {
                            Button(

                                onClick = {
                                    model.startTrialJson(id=model.userid.value!!)
                                    setShowDialog(false)
                                }) {
                                Text("Aceptar")
                            }
                        },
                        dismissButton = {
                            Button(

                                onClick = {
                                    setShowDialog(false)
                                }) {
                                Text("Rechazar")
                            }
                        }
                    )




    }
}
@Composable
fun CountriesScreen(showDialog: Boolean, setShowDialog: (Boolean) -> Unit,model: MainViewModel) {
    val countriesNames= model.countries.value!!
    val countriesn= mutableListOf<String>()
    for (i in countriesNames){
        countriesn.add(i.name)
    }
    DropdownMenu(expanded = showDialog, onDismissRequest = {setShowDialog(false)}) {
        for (item in countriesn){
            MyCountries(name = item,setShowDialog=setShowDialog,model=model )
        }
    }

}

@Composable
fun MyLinux(name:String,link:String,navController:NavController,model: MainViewModel){
    Button(onClick = { navController.navigate("remoto")
        model.linkmaquina.postValue(link)

    }) {
        Text(text=name)
    }
}

@Composable
fun MyCountries(name:String,setShowDialog: (Boolean) -> Unit,model: MainViewModel){
    DropdownMenuItem(
        onClick = { model.pais.postValue(name)
            setShowDialog(false)

    }) {
        Text(text = name)

    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PruebaQinayaTheme {
        Greeting("Android")
    }
}



