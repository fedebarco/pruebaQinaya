package com.example.pruebaqinaya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
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
    val prueba=model.respuesta.value
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_page") {
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
fun MainPage(navController: NavController) {
    Row {
        Button(onClick = {navController.navigate("countries_screen")}) {
            Text(text = "Paises:")

        }

    }
}
@Composable
fun CountriesScreen(countriesNames:List<String>,model: MainViewModel) {

    var prueba=model.respuesta.observeAsState()


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



