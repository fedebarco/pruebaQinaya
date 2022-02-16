package com.example.pruebaqinaya

import android.content.SharedPreferences
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pruebaqinaya.data.UserMachine

@OptIn(
    ExperimentalAnimationApi::class,
    androidx.compose.foundation.ExperimentalFoundationApi::class
)
@Composable
fun ComputerGrid(navController: NavController, model: MainViewModel, shared: SharedPreferences, conectar:Boolean){
    val (computers, setcomputers) =  remember { mutableStateOf(listOf<UserMachine>()) }
    var textresponseP by remember { mutableStateOf("") }
    conectar.let {
        if(it){
            if (computers.isEmpty()){
                model.cimputerJSON(shared.getString("id","")!!.toLong(),setcomputers)
                textresponseP="Bienvenido a Qinaya:"
            }
        }else{
            setcomputers(listOf())
            textresponseP="No hay internet"
        }
    }


    Card(shape = RoundedCornerShape(50.dp)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primaryVariant)
        ) {
            Text(text = "Mis Compus", fontSize = 30.sp, color = Color.White)
            LazyVerticalGrid(cells = GridCells.Adaptive(500.dp)) {
                items(computers) { micompu ->
                    Gridmachine(
                        micompu = micompu,
                        navController = navController,
                        model = model,
                        id = shared.getString("id", "")!!.toLong()
                    ) //MyLinux(micompu = micompu,navController = navController,model = model,id = shared.getString("id","")!!.toLong() )
                }
                item { GridBuy(navController = navController, model =model )  }

            }
        }
    }

    GiftCode(model = model)

}

@ExperimentalAnimationApi
@Composable
fun ComputerScreen(navController: NavController,model: MainViewModel,shared: SharedPreferences,conectar:Boolean){

    val (computers, setcomputers) =  remember { mutableStateOf(listOf<UserMachine>()) }
    var textresponseP by remember { mutableStateOf("") }
    conectar.let {
        if(it){
            if (computers.isEmpty()){
                model.cimputerJSON(shared.getString("id","")!!.toLong(),setcomputers)
                textresponseP="Bienvenido a Qinaya:"
            }
        }else{
            setcomputers(listOf())
            textresponseP="No hay internet"
        }
    }

    Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(MaterialTheme.colors.background)) {
        //Text(textresponseP)
        LazyColumn{
            items(computers){micompu->
                Mymachine(micompu = micompu,navController = navController,model = model,id = shared.getString("id","")!!.toLong() )
                //MyLinux(micompu = micompu,navController = navController,model = model,id = shared.getString("id","")!!.toLong() )
            }


        }
        GiftCode(model = model)
    }
}

@ExperimentalAnimationApi
@Composable
fun Gridmachine(micompu:UserMachine,navController:NavController,model: MainViewModel,id:Long) {
    var expaned by remember{ mutableStateOf(false)}
    Card (modifier = Modifier
        .clickable { expaned = !expaned }
        .fillMaxWidth()
        .padding(15.dp)
        , shape = RoundedCornerShape(50.dp)
    ){
        Column(modifier=Modifier.padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(fontSize = 20.sp,text = micompu.nombreMaquina)
            Text(text = micompu.tiempoDisponible)
            Button(
                modifier = Modifier.padding(5.dp),
                onClick = {
                    navController.navigate("remoto")
                    model.linkmaquina.postValue(micompu.url)
                    model.startJSON(id)
                }) {
                Text(text = siono(micompu.planActivo))
            }
            AnimatedVisibility(visible = (expaned)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .padding(15.dp)
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {


                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.CalendarToday, contentDescription = "")
                                Column {
                                    Text("Fecha de Inicio")
                                    Text(micompu.startDate)
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Schema, contentDescription = "")
                                Column {
                                    Text("Tipo de plan")
                                    Text(micompu.plan)
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.SettingsSystemDaydream, contentDescription = "")
                                Column {
                                    Text("Sistema Operativo")
                                    Text(micompu.sistemaOperativo)
                                }
                            }
                        }
                        Column(
                            modifier = Modifier
                                .padding(15.dp)
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            Icon(Icons.Filled.WatchLater, contentDescription = "")
                            Text("tiempo restante")
                            Text(micompu.tiempoDisponible)


                        }
                    }
                    Row(modifier = Modifier.padding(15.dp)) {
                        Button(onClick = {//aca para conectar
                        }) {
                            Text(text = "RECARGA UN PLAN")
                        }
                    }
                    /*Row() {
                        Button(onClick = {//aca para conectar
                        }) {
                            Icon(
                                Icons.Filled.ShoppingCart,
                                contentDescription = "Localized description"
                            )
                            Text(text = "COMPRAR COMPU")
                        }
                        IconButton(onClick = {

                        }) {
                            Icon(
                                Icons.Filled.CardGiftcard,
                                contentDescription = "Localized description"
                            )
                        }
                    }*/
                }
            }

        }

    }
}

@Composable
fun MyLinux(micompu:UserMachine,navController:NavController,model: MainViewModel,id:Long){
    Card (modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp)
        .border(1.dp, Color.Black)){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = micompu.nombreMaquina)
            Row() {
                Column(modifier = Modifier
                    .fillMaxWidth(0.5f)) {


                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.CalendarToday, contentDescription = "")
                        Column {
                            Text("Fecha de Inicio")
                            Text(micompu.startDate)
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Schema, contentDescription = "")
                        Column {
                            Text("Tipo de plan")
                            Text(micompu.plan)
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.SettingsSystemDaydream, contentDescription = "")
                        Column {
                            Text("Sistema Operativo")
                            Text(micompu.plan)
                        }
                    }
                }
                Column(modifier = Modifier
                    .fillMaxWidth(0.5f)) {
                    Column {
                        Icon(Icons.Filled.WatchLater, contentDescription = "")
                        Text("tiempo restante")
                        Text("0:00")
                    }

                }
            }
            Row() {
                Button(onClick = {//aca para conectar
                }) {
                    Text(text = "RECARGA UN PLAN")
                }
                Button(onClick = {
                    navController.navigate("remoto")
                    model.linkmaquina.postValue(micompu.url)
                    model.startJSON(id)
                }) {
                    Text(text = "CONECTAR")
                }
            }
            Row() {
                Button(onClick = {//aca para conectar
                }) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Localized description")
                    Text(text = "COMPRAR COMPU")
                }
                IconButton(onClick = {

                }) {
                    Icon(Icons.Filled.CardGiftcard, contentDescription = "Localized description")
                }
            }
        }

    }
}
@ExperimentalAnimationApi
@Composable
fun Mymachine(micompu:UserMachine,navController:NavController,model: MainViewModel,id:Long){
    var expaned by remember{ mutableStateOf(false)}
    Card (modifier = Modifier
        .clickable { expaned = !expaned }
        .fillMaxWidth()
        .padding(15.dp)
        , shape = RoundedCornerShape(50.dp)

    ){
        Column(modifier=Modifier.padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(fontSize = 20.sp,text = micompu.nombreMaquina)
                    Text(text = micompu.tiempoDisponible)
                }
                Button(
                    onClick = {
                        navController.navigate("remoto")
                        model.linkmaquina.postValue(micompu.url)
                        model.startJSON(id)
                    }) {
                    Text(text = "CONECTAR")
                }
            }
            AnimatedVisibility(visible = (expaned)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .padding(15.dp)
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {


                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.CalendarToday, contentDescription = "")
                                Column {
                                    Text("Fecha de Inicio")
                                    Text(micompu.startDate)
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Schema, contentDescription = "")
                                Column {
                                    Text("Tipo de plan")
                                    Text(micompu.plan)
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.SettingsSystemDaydream, contentDescription = "")
                                Column {
                                    Text("Sistema Operativo")
                                    Text(micompu.sistemaOperativo)
                                }
                            }
                        }
                        Column(
                            modifier = Modifier
                                .padding(15.dp)
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            Icon(Icons.Filled.WatchLater, contentDescription = "")
                            Text("tiempo restante")
                            Text(micompu.tiempoDisponible)


                        }
                    }
                    Row(modifier = Modifier.padding(15.dp)) {
                        Button(onClick = {//aca para conectar
                        }) {
                            Text(text = "RECARGA UN PLAN")
                        }
                    }
                    /*Row() {
                        Button(onClick = {//aca para conectar
                        }) {
                            Icon(
                                Icons.Filled.ShoppingCart,
                                contentDescription = "Localized description"
                            )
                            Text(text = "COMPRAR COMPU")
                        }
                        IconButton(onClick = {

                        }) {
                            Icon(
                                Icons.Filled.CardGiftcard,
                                contentDescription = "Localized description"
                            )
                        }
                    }*/
                }
            }
        }

    }
}


