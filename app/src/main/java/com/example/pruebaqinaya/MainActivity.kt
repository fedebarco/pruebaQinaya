package com.example.pruebaqinaya


import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: MainViewModel by viewModels()
        val sharedPref = getSharedPreferences("my_prefs",Context.MODE_PRIVATE)
        model.searchCountries()
        setContent {
            PruebaQinayaTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainNavigation(model = model,shared = sharedPref,start = checkLogin(sharedPref))
                }
            }
        }
    }

    private fun checkLogin(shared : SharedPreferences):String{
        val beginA = if (shared.getString("active","") == "true"){
            "main_page"
        }else{
            "login_qinaya"
        }
        return beginA
    }
}


@Composable
fun MainNavigation(model: MainViewModel,shared: SharedPreferences,start:String){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = start) {
        composable("login_qinaya") { LoginQinaya(model = model,navController = navController,shared = shared) }
        composable("register_qinaya") { RegisterScreen(model = model) }
        composable("main_page") { MainPage(navController = navController,model = model,shared=shared)}
        composable("remoto") { Remoto(model=model)}
        }

    }



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun LoginQinaya(model: MainViewModel,navController: NavController,shared: SharedPreferences){
    var textUser by remember { mutableStateOf("") }
    var textPassword by remember { mutableStateOf("") }
    val showPassword= remember { mutableStateOf(false) }
    val textResponse by model.toLogin.observeAsState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val checkedState=remember{ mutableStateOf(false)}

    Scaffold(
        scaffoldState = scaffoldState,
        content = {
            Box(modifier=Modifier.fillMaxSize()) {
                Image(painter = painterResource(id = R.drawable.back), contentDescription ="" )

            }
            Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .height(IntrinsicSize.Max)
                .border(1.dp, Color.Black)
            ) {
                Text(text = "Bienvenido.",fontSize = 30.sp)
                OutlinedTextField(value = textUser, onValueChange ={textUser=it},label = { Text(text = "Usuario:")},modifier = Modifier
                    .padding(horizontal = 50.dp, vertical = 8.dp)
                    .fillMaxWidth() )
                OutlinedTextField(
                    value = textPassword,
                    onValueChange ={textPassword=it},
                    label = { Text(text = "Contraseña")},
                    modifier = Modifier
                        .padding(horizontal = 50.dp, vertical = 8.dp)
                        .fillMaxWidth(),
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
                        .padding(horizontal = 50.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    onClick = {
                        model.rawJSON(textUser,textPassword,navController,shared)
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Error: $textResponse")
                        }
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
                Text("$textResponse")
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
            .height(IntrinsicSize.Max)
                ) {
            Text(text = "Crea tu cuenta",fontSize = 30.sp)
            OutlinedTextField(value = textname, onValueChange ={textname=it},label = { Text(text = "Nombre:")},modifier = Modifier
                .padding(horizontal = 50.dp, vertical = 8.dp)
                .fillMaxWidth()  )
            OutlinedTextField(value = textEmail, onValueChange ={textEmail=it},label = { Text(text = "Email")},modifier = Modifier
                .padding(horizontal = 50.dp, vertical = 8.dp)
                .fillMaxWidth()  )
            OutlinedTextField(value = textPassword, onValueChange ={textPassword=it},label = { Text(text = "contraseña:")},modifier = Modifier
                .padding(horizontal = 50.dp, vertical = 8.dp)
                .fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()  )
            OutlinedTextField(value = textPassword2, onValueChange ={textPassword2=it},label = { Text(text = "Confirmar contraseña")},modifier = Modifier
                .padding(horizontal = 50.dp, vertical = 8.dp)
                .fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation())
            OutlinedTextField(value = textPhone, onValueChange ={textPhone=it},label = { Text(text = "telefono")},modifier = Modifier
                .padding(horizontal = 50.dp, vertical = 8.dp)
                .fillMaxWidth()  )
            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = 50.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                onClick = {setShowDialog(true)}
            ) {
                Text("$textPais")
                Icon(Icons.Filled.ExpandMore,contentDescription = "")
            }
            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = 50.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                onClick = {setShowDialog1(true)}
            ) {
                Text("$textMoneda")
                Icon(Icons.Filled.ExpandMore,contentDescription = "")
            }
            Button(
                modifier = Modifier
                    .padding(horizontal = 50.dp, vertical = 8.dp)
                    .fillMaxWidth(),
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
fun MainPage(navController: NavController,model: MainViewModel,shared: SharedPreferences) {
    //model.actualizaJson(shared.getString("id","")!!.toLong())
    model.cimputerJSON(shared.getString("id","")!!.toLong())
    val computadoras=model.UserComputers.observeAsState()
    val textresponse=model.toComputers.observeAsState()
    val (showDialog, setShowDialog) =  remember { mutableStateOf(model.trialinit.value!!) }

    Scaffold(
        topBar = {
                 TopAppBar (
                     title = { Text("Qinaya") },
                     actions = {
                         // RowScope here, so these icons will be placed horizontally
                         IconButton(onClick = {
                             with(shared.edit()){
                                 putString("active", "false")
                                 commit() }
                             navController.navigate("login_qinaya")

                         }) {
                             Icon(Icons.Filled.Favorite, contentDescription = "Localized description")
                         }
                     }
                         )

        },
        content = {
            Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colors.background)) {
                Text("$textresponse")
                computadoras.value?.let{
                    LazyColumn{
                        items(it){micompu->
                            MyLinux(name = micompu.userMachine.nombreMaquina,link =micompu.userMachine.url,navController = navController,model = model)
                        }
                    }
                }
                AlertDialogTrial(model,showDialog, setShowDialog)
            }


        }
    )
}

@Composable
fun Remoto(model: MainViewModel){

    val url=model.linkmaquina.value
    val textresponse by model.linkmaquina.observeAsState()


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = { model.linkmaquina.postValue("parro")}) {
            Text("que pasa")
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(MaterialTheme.colors.primary)
        ) {
            Text(
                text = "$textresponse",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Button(onClick = { model.linkmaquina.postValue("parro")}) {
                Text("que pasa")
            }
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
    Card (modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp)
        .height(IntrinsicSize.Max)
        .border(1.dp, Color.Black)){
        Column {
            Text(text = name)
            Button(onClick = {//aca para conectar
                 }) {
                Text(text = "RECARGA UN PLAN")
            }
            Button(onClick = {
                navController.navigate("remoto")
                model.linkmaquina.postValue(link)
            }) {
                Text(text = "CONECTAR")
            }
        }

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



