package com.example.pruebaqinaya

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Help
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import zendesk.support.guide.HelpCenterActivity

@Composable
fun SettingQinaya(navController: NavController, shared: SharedPreferences,context2: Context){
    Scaffold(
        topBar = {AppBarReturn(navController = navController, pageM = "main_page")},
        content = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)) {
                UserProfileScreen(navController = navController)
                TextButton(onClick = {navController.navigate("share_screen") }) {
                    Icon(Icons.Filled.Help, contentDescription = "Help")
                    Text(text = "Compartir")
                }
                TextButton(onClick = {
                    HelpCenterActivity.builder()
                    .show(context2) }) {
                    Icon(Icons.Filled.Help, contentDescription = "Help")
                    Text(text = "Centro de ayuda")
                }
                TextButton(onClick = {
                    with(shared.edit()) {
                        putString("active", "false")
                        commit()
                    }
                    navController.navigate("login_qinaya")

                }) {
                    Icon(Icons.Filled.ExitToApp, contentDescription = "Exit")
                    Text(text = "cerrar sesion")
                }
            }
        }
    )
}

@Composable
fun UserProfileScreen(navController: NavController){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .border(1.dp, Color.Black)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Perfil")
                    Text("Email")
                    Button(onClick = { navController.navigate("change_password") }) {
                        Text(text = "cambiar contraseña")

                    }
                }
            }


}

@Composable
fun ShareScreen(navController: NavController){
    Scaffold(
        topBar = {AppBarReturn(navController = navController, pageM = "main_page")},
        content = {
            val (email, setEmail) = remember { mutableStateOf("") }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Compartir")
                Button(onClick = { }) {
                    Text(text = "whatsapp")

                }
                Button(onClick = { }) {
                    Text(text = "Messenger")

                }
                OutlinedTextField(value = email, onValueChange = { setEmail(it) })
                Button(onClick = { }) {
                    Text(text = "Enviar invitacion")

                }
            }
        }
    )
}

@Composable
fun ChangePassword(navController: NavController){
    Scaffold(
        topBar = {AppBarReturn(navController = navController, pageM = "main_page")},
        content = {
            val (passwordOld, setPasswordOld) = remember { mutableStateOf("") }
            val (passwordNew, setPasswordNew) = remember { mutableStateOf("") }
            val (passwordNew2, setPasswordNew2) = remember { mutableStateOf("") }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .border(1.dp, Color.Black)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Cambia tu contraseña")
                    OutlinedTextField(value = passwordOld, onValueChange = { setPasswordOld(it) })
                    OutlinedTextField(value = passwordNew, onValueChange = { setPasswordNew(it) })
                    OutlinedTextField(value = passwordNew2, onValueChange = { setPasswordNew2(it) })
                    Button(onClick = { }) {
                        Text("Enviar")
                    }

                }
            }
        }
    )
}
