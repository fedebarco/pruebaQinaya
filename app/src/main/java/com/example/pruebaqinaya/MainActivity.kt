package com.example.pruebaqinaya


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pruebaqinaya.ui.theme.PruebaQinayaTheme


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
        composable("main_page") { MainPage(navController = navController) }
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
    val textResponse by model.toLogin.observeAsState()
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Column {
            OutlinedTextField(value = textUser, onValueChange ={textUser=it},label = { Text(text = "Usuario:")} )
            OutlinedTextField(value = textPassword, onValueChange ={textPassword=it},label = { Text(text = "Contraseña")} )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { model.rawJSON(textUser,textPassword)
                }
            ) {
                Text("Ingresa")
            }
            TextButton(onClick = { navController.navigate("register_qinaya") }) {
                Text("registrate")
            }
            Text("$textResponse")
            if (textUser=="fede"){
                navController.navigate("main_page")
            }

        }


    }

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
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Column {
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
                onClick = {setShowDialog(true)}
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

            DialogDemo(showDialog, setShowDialog,model)
            CountriesScreen(showDialog, setShowDialog,model)
        }


    }

}

@Composable
fun DialogDemo(showDialog: Boolean, setShowDialog: (Boolean) -> Unit,model: MainViewModel) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                setShowDialog(false)
            },
            title = {
                Text("Paises")
            },
            buttons ={
                Column(
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    Button(
                        onClick = { setShowDialog(false)
                            model.moneda.postValue("COP")}
                    ) {
                        Text("COP")
                    }
                    Button(
                        onClick = {
                            model.moneda.postValue("USD")
                            setShowDialog(false) }
                    ) {
                        Text("USD")

                    }
                }
            }


        )
    }
}



@Composable
fun MainPage(navController: NavController) {
    Column {

    }
}
@Composable
fun CountriesScreen(showDialog: Boolean, setShowDialog: (Boolean) -> Unit,model: MainViewModel) {
    val countriesNames= model.countries.value!!
    val countriesn= mutableListOf<String>()
    for (i in countriesNames){
        countriesn.add(i.name)
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                setShowDialog(false)
            },
            title = {
                Text("Paises")
            },
            buttons ={
                Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(MaterialTheme.colors.background)) {
                    LazyColumn{
                        items(countriesn){countryName->
                            MyCountries(name = countryName )
                        }
                    }

                }
            }


        )
    }
}

@Composable
fun MyCountries(name:String){
    Button(onClick = { /*TODO*/ }) {
        Text(text=name)
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PruebaQinayaTheme {
        Greeting("Android")
    }
}



