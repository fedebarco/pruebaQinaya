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
    model.searchCountries()
    val countriesNames= model.countries.value
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login_qinaya") {
        composable("login_qinaya") { LoginQinaya(model = model,navController = navController) }
        composable("register_qinaya") { RegisterScreen(model = model) }
        composable("main_page") { MainPage(navController = navController) }
        composable("countries_screen") { CountriesScreen(countriesNames = countriesNames?: listOf("colombia"),model=model) }
        /*...*/
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
                onClick = { model.rawJSON(textUser,textPassword) }
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

@Composable
fun RegisterScreen(model: MainViewModel){
    var textUser by remember { mutableStateOf("") }
    var textPassword by remember { mutableStateOf("") }
    val textResponse by model.toLogin.observeAsState()
    val openDialog = remember { mutableStateOf(true) }
    val (showDialog, setShowDialog) =  remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Column {
            OutlinedTextField(value = textUser, onValueChange ={textUser=it},label = { Text(text = "Nombre:")} )
            OutlinedTextField(value = textPassword, onValueChange ={textPassword=it},label = { Text(text = "Email")} )
            OutlinedTextField(value = textUser, onValueChange ={textUser=it},label = { Text(text = "contraseña:")} )
            OutlinedTextField(value = textPassword, onValueChange ={textPassword=it},label = { Text(text = "contraseña")} )
            OutlinedTextField(value = textPassword, onValueChange ={textPassword=it},label = { Text(text = "contraseña")} )
            Button(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                onClick = {setShowDialog(true)}
            ) {
                Text("Pais")
            }
            Button(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                onClick = {setShowDialog(true)}
            ) {
                Text("Moneda")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { model.rawJSON(textUser,textPassword) }
            ) {
                Text("Registrate")
            }
            Text("$textResponse")
            DialogDemo(showDialog, setShowDialog)
        }


    }

}

@Composable
fun DialogDemo(showDialog: Boolean, setShowDialog: (Boolean) -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
            },
            title = {
                Text("Title")
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Change the state to close the dialog
                        setShowDialog(false)
                    },
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        // Change the state to close the dialog
                        setShowDialog(false)
                    },
                ) {
                    Text("Dismiss")
                }
            },
            text = {
                Text("This is a text on the dialog")
            },
        )
    }
}



@Composable
fun MainPage(navController: NavController) {
    Row {
        Button(onClick = {navController.navigate("countries_screen")}) {
            Text(text = "Paises:")

        }

    }
}
@Composable
fun CountriesScreen(countriesNames:List<String>,model: MainViewModel) {

    val prueba=model.respuesta.observeAsState()


    Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(MaterialTheme.colors.background)) {
        Text(text = "Paises")
        Text(text = " la respuesta es: $prueba")
        LazyColumn{
            items(countriesNames){countryName->
                MyCountries(name = countryName )
            }
        }

    }
}

@Composable
fun MyCountries(name:String){
    Text(text=name)
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PruebaQinayaTheme {
        Greeting("Android")
    }
}



