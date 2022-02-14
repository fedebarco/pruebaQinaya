package com.example.pruebaqinaya

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pruebaqinaya.data.CompuPlan
import com.example.pruebaqinaya.data.UserMachine

@ExperimentalMaterialApi
@Composable
fun StoreQinaya(navController: NavController, model: MainViewModel){
    Scaffold(
        topBar = {AppBarReturn(navController = navController, pageM = "main_page")},
        content = {
            Column(modifier = Modifier
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                GiftCode(model=model)
                Text(fontSize = 40.sp, text = "COMPRA UNA COMPU:")
                ProductStore(navController)
            }
        }
    )
}
@ExperimentalMaterialApi
@Composable
fun ProductStore(navController: NavController){
    val infiniteTransition = rememberInfiniteTransition()
    val image by infiniteTransition.animateFloat(
        0f,
        1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    var resourseI by remember { mutableStateOf(R.drawable.mate1) }
    image.let {
        if (image<0.5f){
            resourseI=R.drawable.mate1
        }
        else{
            resourseI=R.drawable.mate2
        }
    }
    Card(onClick = {
        navController.navigate("item_store")

    },
        modifier = Modifier
            .width(600.dp)
            .padding(15.dp)
    ) {
        Column(
            modifier = Modifier.width(500.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier=Modifier.width(400.dp),
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = resourseI),
                contentDescription = ""
            )
            Row() {
                Image(
                    modifier = Modifier.requiredSize(50.dp),
                    painter = painterResource(id = R.drawable.logo_mate),
                    contentDescription = ""
                )
                Column() {
                    Text("Maquina Linux")
                    Text("sistema operativo:Ubuntu mate")

                }
            }
        }
    }
}
@Composable
fun ItemStore(navController: NavController){
    val di=CompuPlan("Diario",R.drawable.globo,"2@ 2.4 GHz","4Gb","25Gb")
    val se=CompuPlan("Mensual",R.drawable.globo,"2@ 2.4 GHz","4Gb","25Gb")
    val me=CompuPlan("Anual",R.drawable.globo,"2@ 2.4 GHz","4Gb","25Gb")



    Scaffold(
        topBar = {AppBarReturn(navController = navController, pageM = "main_page")},
        content = {

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()) {
                ItemPay(di)
                ItemPay(se)
                ItemPay(me)
            }
        }
    )
}

@Composable
fun ItemPay(compuPlan: CompuPlan){
    Column() {
        Text(compuPlan.tipo)
        Image(
            painter = painterResource(id = compuPlan.img),
            contentDescription = ""
        )
        Text(compuPlan.almacenamiento)
        Text(compuPlan.ram)
        Text(compuPlan.cores)
        Button(onClick = {}) {
            Text("COMPRAR PLAN")

        }
    }

}
@Composable
fun CompusItemScreen(showDialog: Boolean, setShowDialog: (Boolean) -> Unit,model: MainViewModel) {
    val compus1= listOf<UserMachine>()
    val compusn= mutableListOf<String>()
    for (i in compus1){
        compusn.add(i.nombreMaquina)
    }
    compusn.add("Nueva compu")
    DropdownMenu(expanded = showDialog, onDismissRequest = {setShowDialog(false)}) {
        for (item in compusn){
            MyCompuI(name = item,setShowDialog=setShowDialog,model=model )
        }
    }

}

@Composable
fun MyCompuI(name:String,setShowDialog: (Boolean) -> Unit,model: MainViewModel){
    DropdownMenuItem(
        onClick = {
            setShowDialog(false)

        }) {
        Text(text = name)

    }
}