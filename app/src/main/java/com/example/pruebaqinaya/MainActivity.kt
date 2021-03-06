package com.example.pruebaqinaya


import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pruebaqinaya.Internet.ConnectionLiveData
import com.example.pruebaqinaya.data.UserMachine
import com.example.pruebaqinaya.ui.theme.PruebaQinayaTheme
import zendesk.core.AnonymousIdentity
import zendesk.core.Zendesk
import zendesk.support.Support


class MainActivity : ComponentActivity() {


    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val connectionLiveData= ConnectionLiveData(this)
        val model: MainViewModel by viewModels()
        val sharedPref = getSharedPreferences("my_prefs",Context.MODE_PRIVATE)
        val context=applicationContext
        val cont2=this
        Zendesk.INSTANCE.init(context, "https://qinayahelp.zendesk.com",
            "645a2e42147fccccc6355f8a0a7cf00b615c633a1e5653d1",
            "mobile_sdk_client_bb1bdfd5957f4fda611a")
        Support.INSTANCE.init(Zendesk.INSTANCE)

        val identity = AnonymousIdentity.Builder()
            .withNameIdentifier("name_user")
            .withEmailIdentifier("email_user")
            .build()
        Zendesk.INSTANCE.setIdentity(identity)

        Support.INSTANCE.helpCenterLocaleOverride = java.util.Locale("es-419")

        setContent {
            PruebaQinayaTheme {
                val isNetworkAvailable=connectionLiveData.observeAsState(false).value
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainNavigation(model = model,shared = sharedPref,start = model.checkLogin(sharedPref),context=context,conectar = isNetworkAvailable,c2=cont2)
                }
            }
        }
    }
}


@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun MainNavigation(model: MainViewModel,shared: SharedPreferences,start:String,context: Context,conectar: Boolean,c2:Context){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = start) {
        composable("login_qinaya") { LoginQinaya(model = model,navController = navController,shared = shared) }
        composable("register_qinaya") { RegisterScreen(model = model, navController = navController) }
        composable("main_page") { MainPage(navController = navController,model = model,shared=shared,context = context,conectar=conectar)}
        composable("remoto") { Remoto(model=model,navController = navController)}
        composable("store") { StoreQinaya(navController = navController, model = model) }
        composable("item_store"){ ItemStore(navController = navController)}
        composable ("recover_password"){ RecoverPassword(navController = navController,model=model)}
        composable("change_password") { ChangePassword(navController = navController, model = model)}
        composable("perfil_screen"){ SettingQinaya(navController = navController, shared = shared,context2 = c2) }
        composable("share_screen"){ ShareScreen(navController=navController)}
        }
    }


@Composable
fun LoadingPage(isDisplayed:Boolean){
    val transition = rememberInfiniteTransition()
    val image by transition.animateFloat(
        0f,
        1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    var resourseI by remember {
        mutableStateOf(R.drawable.azul1)
    }
    image.let {
        if (0f<it && it<0.125f){resourseI=R.drawable.azul1
        }else if(it<0.25){resourseI=R.drawable.azul2
        }else if(it<0.375){resourseI=R.drawable.morado1
        }else if(it<0.5){resourseI=R.drawable.morado2
        }else if(it<0.625){resourseI=R.drawable.rosa1
        }else if(it<0.75){resourseI=R.drawable.naranja1
        }else if(it<0.875){resourseI=R.drawable.amarillo1
        }else{resourseI=R.drawable.verde1
        }
    }
    if (isDisplayed){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = resourseI),
                contentDescription = ""
            )

        }
        
    }

}


@ExperimentalAnimationApi
@Composable
fun MainPage(navController: NavController,model: MainViewModel,shared: SharedPreferences,context: Context,conectar:Boolean) {

    val (showDialog, setShowDialog) =  remember { mutableStateOf(model.trialinit.value!!) }
    val loading=model.loading.value
    val micompu=model.UserCom1.observeAsState().value
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

    Scaffold(
        topBar = {
                 TopAppBar (
                     backgroundColor = Color.Transparent,
                     title = { Image(painter = painterResource(id = R.drawable.icono_qinaya_adj), contentDescription ="" )},
                     actions = {
                         // RowScope here, so these icons will be placed horizontally
                         IconButton(onClick = {
                             val imeManager =
                                 context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                             imeManager.showInputMethodPicker()

                         }) {
                             Icon(Icons.Filled.Keyboard, contentDescription = "Localized description")
                         }
                         IconButton(onClick = {
                             navController.navigate("perfil_screen")
                         }) {
                             Icon(Icons.Filled.Settings, contentDescription = "Localized description")
                         }
                     }
                         )

        }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colors.background)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn() {
                    item {
                        Card(modifier = Modifier.height(300.dp)) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {

                                Row() {
                                    micompu.let {
                                        if (it != null) {
                                            Inicio(
                                                micompu = it,
                                                navController = navController,
                                                model = model
                                            )

                                            Card(
                                                modifier = Modifier
                                                    .width(600.dp)
                                                    .padding(15.dp)
                                            ) {
                                                Column(
                                                    modifier = Modifier.width(500.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Image(
                                                        modifier = Modifier.width(400.dp),
                                                        contentScale = ContentScale.FillWidth,
                                                        painter = painterResource(id = resourseI),
                                                        contentDescription = ""
                                                    )
                                                }
                                            }
                                        } else {
                                            ItemStore(navController = navController)

                                            Card(
                                                modifier = Modifier
                                                    .width(600.dp)
                                                    .padding(15.dp)
                                            ) {
                                                Column(
                                                    modifier = Modifier.width(500.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Image(
                                                        modifier = Modifier.width(400.dp),
                                                        contentScale = ContentScale.FillWidth,
                                                        painter = painterResource(id = resourseI),
                                                        contentDescription = ""
                                                    )
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }

                    item {

                        ComputerRow(
                            navController = navController,
                            model = model,
                            shared = shared,
                            conectar = conectar
                        )
                    }
                    item { GiftCode(model = model) }
                }
                LoadingPage(isDisplayed = loading)

            }


        }

        AlertDialogTrial(model, showDialog, setShowDialog)


    }
}

@Composable
fun Inicio(micompu: UserMachine,navController: NavController,model: MainViewModel){
    Card(modifier = Modifier.height(600.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.width(500.dp)) {
                Column(
                    modifier = Modifier
                        .padding(15.dp)
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.CalendarToday,
                            contentDescription = ""
                        )
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
                        Icon(
                            Icons.Filled.SettingsSystemDaydream,
                            contentDescription = ""
                        )
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
                Button(
                    modifier = Modifier.padding(5.dp),
                    onClick = {//aca para conectar
                }) {
                    Text(text = "RECARGA UN PLAN")
                }
                Button(
                    modifier = Modifier.padding(5.dp),
                    onClick = {
                        navController.navigate("remoto")
                        model.linkmaquina.postValue(micompu.url)
                        //model.startJSON(id)
                    }) {
                    Text(text = siono(micompu.planActivo))
                }
            }
        }
    }
}



@Composable
fun ComputerRow(navController: NavController,model: MainViewModel,shared: SharedPreferences,conectar:Boolean){
    val (computers, setcomputers) =  remember { mutableStateOf(listOf<UserMachine>()) }
    var textresponseP by remember { mutableStateOf("") }
    val (idP, setidP) =  remember { mutableStateOf<Long>(0) }
    conectar.let {
        if (it) {
            if (computers.isEmpty()) {
                model.cimputerJSON(shared.getString("id", "")!!.toLong(), setcomputers)
                textresponseP = "Bienvenido a Qinaya:"
            }
        } else {
            setcomputers(listOf())
            textresponseP = "No hay internet"
        }
    }


        Card(modifier = Modifier.height(500.dp),
            shape = RoundedCornerShape(50.dp)) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primaryVariant)
            ) {
                Text(text = "Mis Compus", fontSize = 30.sp, color = Color.White)
                LazyRow {
                    items(computers) { micompu ->
                        RowMachine(
                            micompu = micompu,
                            model = model,
                            id = shared.getString("id", "")!!.toLong(),
                            idP = idP,
                            setMainB = setidP

                        ) //MyLinux(micompu = micompu,navController = navController,model = model,id = shared.getString("id","")!!.toLong() )

                    }
                    item { RowBuy(navController = navController, model =model )  }

                }
            }
        }



}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RowMachine(micompu:UserMachine,model: MainViewModel,id:Long,idP:Long, setMainB: (Long) -> Unit){
    Card (modifier = Modifier
        .height(400.dp)
        .clickable {
            setMainB(id)
            model.UserCom1.postValue(micompu)

        }
        .fillMaxWidth()
        .padding(15.dp)
        , shape = RoundedCornerShape(50.dp)
    ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

                    modifier = Modifier
                        .width(200.dp)
                        .padding(15.dp)
            ) {


                Text(fontSize = 20.sp, text = micompu.nombreMaquina)
                Text(text = micompu.tiempoDisponible)

            }
    }

}


@Composable
fun Remoto(model: MainViewModel,navController: NavController){

    val url=model.linkmaquina.value
    val textresponse2 by model.responsemaquina.observeAsState()
    var clickI by remember { mutableStateOf(false) }
    var enterE by remember { mutableStateOf(false) }
    val backHandlingEnabled by remember { mutableStateOf(true) }
    BackHandler(backHandlingEnabled) {
        // Handle back press
        clickI=true
    }


    Box() {
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
                    text = "$textresponse2",
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
                        if(clickI){
                            //val kcc =MotionEvent.obtain(SystemClock.uptimeMillis(),1000-SystemClock.uptimeMillis(),MotionEvent.ACTION_UP,100f,100f,0)
                            val sd = KeyEvent(
                                0,
                                0,
                                KeyEvent.ACTION_DOWN,
                                KeyEvent.KEYCODE_SHIFT_RIGHT,
                                0,
                                0,
                                0,
                                0
                            )
                            val kd = KeyEvent(
                                0,
                                0,
                                KeyEvent.ACTION_DOWN,
                                KeyEvent.KEYCODE_F10,
                                0,
                                0,
                                0,
                                0
                            )
                            val ku =
                                KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_F10, 0, 0, 0, 0)
                            val su = KeyEvent(
                                0,
                                0,
                                KeyEvent.ACTION_UP,
                                KeyEvent.KEYCODE_SHIFT_RIGHT,
                                0,
                                0,
                                0,
                                0
                            )
                            it.dispatchKeyEvent(sd)
                            it.dispatchKeyEvent(kd)
                            it.dispatchKeyEvent(ku)
                            it.dispatchKeyEvent(su)
                            //it.dispatchGenericMotionEvent(kcc)
                            clickI=false
                        }
                        if(enterE){
                            val kcc =MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis()+10000,MotionEvent.ACTION_UP,100f,100f,0)
                            val kd = KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_NUMPAD_ENTER, 0, 0, 0, 0)
                            val ku = KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_NUMPAD_ENTER, 0, 0, 0, 0)
                            it.dispatchKeyEvent(kd)
                            it.dispatchKeyEvent(ku)
                            //it.dispatchGenericMotionEvent(kcc)

                            enterE=false

                        }


                    }
                )
            }
        }
        FloatingActionButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = { model.endJSON(navController = navController) }) {
            Icon(Icons.Filled.ExitToApp, contentDescription = "Localized description")
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
fun GiftCode(model: MainViewModel){
    var textCode by remember { mutableStateOf("") }
    val (showDialog, setShowDialog) =  remember { mutableStateOf(false) }
    val scrollState= rememberScrollState()
    Card (modifier = Modifier
        .height(600.dp)
        .fillMaxWidth()
        .padding(15.dp)
        , shape = RoundedCornerShape(50.dp)

    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(IntrinsicSize.Max)

        ) {
            Text(fontSize = 20.sp,text = "Tienes un codigo para redimir")
            Text("activa tu plan aqui")
            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = 50.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                onClick = { setShowDialog(true) }
            ) {
                Text("maquina actual")
                Icon(Icons.Filled.ExpandMore, contentDescription = "")
            }
            CompusItemScreen(showDialog = showDialog, setShowDialog = setShowDialog, model = model)
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(value = textCode,
                    onValueChange = { textCode = it },
                    label = { Text(text = "Tu codigo:") },
                    modifier = Modifier
                        .padding(horizontal = 50.dp, vertical = 8.dp)
                        .weight(1f)
                )
                Button(onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(horizontal = 50.dp, vertical = 8.dp)
                ) {
                    Text(text = "Redimir")
                }

            }
        }
    }

}


@Composable
fun AppBarReturn(navController: NavController,pageM:String){
    TopAppBar (
        backgroundColor = Color.Transparent,
        title = { Image(painter = painterResource(id = R.drawable.icono_qinaya_adj), contentDescription ="" )},
        actions = {
            // RowScope here, so these icons will be placed horizontally
            IconButton(onClick = {
                navController.navigate(pageM)

            }) {
                Icon(Icons.Filled.House, contentDescription = "Localized description")
            }

        }
    )

}


fun siono(nn:Boolean):String{
    return if (nn) {
        "CONECTAR"
    } else {
        "Recarga compu"
    }
}

@Composable
fun RowBuy(navController:NavController,model: MainViewModel) {
    Card (modifier = Modifier
        .height(400.dp)
        .clickable { model.UserCom1.postValue(null)}
        .width(215.dp)
        .padding(15.dp)
        , shape = RoundedCornerShape(50.dp)
    ){
        Column(modifier=Modifier.padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Localized description")
            Text(fontSize = 20.sp,text = "Nueva Compu")
            Text(text = "Compra una nueva compu")
        }

    }
}

@ExperimentalAnimationApi
@Composable
fun GridBuy(navController:NavController,model: MainViewModel) {
    Card (modifier = Modifier
        .clickable { model.UserCom1.postValue(null)}
        .fillMaxWidth()
        .padding(15.dp)
        , shape = RoundedCornerShape(50.dp)
    ){
        Column(modifier=Modifier.padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Filled.Add, contentDescription = "Localized description")
            Text(fontSize = 20.sp,text = "Nueva Compu")
            Text(text = "Compra una nueva compu")
        }

    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PruebaQinayaTheme {
    }
}



