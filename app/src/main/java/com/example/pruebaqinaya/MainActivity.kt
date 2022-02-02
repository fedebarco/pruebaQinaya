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
import androidx.compose.ui.layout.ContentScale
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
import com.example.pruebaqinaya.Internet.ConnectionLiveData
import com.example.pruebaqinaya.data.CountriesResponse
import com.example.pruebaqinaya.data.Login
import com.example.pruebaqinaya.data.Registration
import com.example.pruebaqinaya.data.UserMachine
import com.example.pruebaqinaya.ui.theme.PruebaQinayaTheme
import kotlinx.coroutines.launch
import zendesk.core.AnonymousIdentity
import zendesk.core.Zendesk
import zendesk.support.Support
import zendesk.support.guide.HelpCenterActivity


class MainActivity : ComponentActivity() {


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
            "mobile_sdk_client_bb1bdfd5957f4fda611a");
        Support.INSTANCE.init(Zendesk.INSTANCE);

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
                    MainNavigation(model = model,shared = sharedPref,start = checkLogin(sharedPref,model = model),context=context,conectar = isNetworkAvailable,c2=cont2)
                }
            }
        }
    }

    private fun checkLogin(shared : SharedPreferences,model: MainViewModel):String{
        val beginA = if (shared.getString("active","") == "true"){
            "main_page"
        }else{
            "login_qinaya"
        }
        return beginA
    }
}


@ExperimentalAnimationApi
@Composable
fun MainNavigation(model: MainViewModel,shared: SharedPreferences,start:String,context: Context,conectar: Boolean,c2:Context){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = start) {
        composable("login_qinaya") { LoginQinaya(model = model,navController = navController,shared = shared) }
        composable("register_qinaya") { RegisterScreen(model = model) }
        composable("main_page") { MainPage(navController = navController,model = model,shared=shared,context = context,conectar=conectar,context2 = c2)}
        composable("remoto") { Remoto(model=model,navController = navController)}
        composable("store") { StoreQinaya(navController = navController) }
        }

    }

@Composable
fun LoginQinaya(model: MainViewModel,navController: NavController,shared: SharedPreferences){
    var textUser by remember { mutableStateOf("") }
    var textPassword by remember { mutableStateOf("") }
    val showPassword= remember { mutableStateOf(false) }
    val (textResponse,setTextResponse)= remember { mutableStateOf("") }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val checkedState=remember{ mutableStateOf(false)}
    val backHandlingEnabled by remember { mutableStateOf(true) }
    BackHandler(backHandlingEnabled) {
        // Handle back press
    }


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
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Error: $it")
                    }
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
    var (countiQ,setCountiQ)= remember { mutableStateOf(listOf<CountriesResponse>()) }
    val scrollState= rememberScrollState()
    val textPais by model.pais.observeAsState()
    val textresponse by model.toregister.observeAsState()
    val textMoneda by model.moneda.observeAsState()
    val (showDialog, setShowDialog) =  remember { mutableStateOf(false) }
    val (showDialog1, setShowDialog1) =  remember { mutableStateOf(false) }
    model.searchCountries(setCountiQ)
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp)) {
        Column (verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
            .verticalScroll(scrollState)
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
                        var registration=Registration(textname,textEmail,1,textPhone,1,1,textPassword)
                        model.registerJSON(registration = registration)
                    }else{
                        model.toregister.postValue("las contraseñas no coinciden")
                    }
                }
            ) {
                Text("Registrate")
            }
            Text("$textresponse")

            DialogDemo(showDialog1, setShowDialog1,model)
            CountriesScreen(showDialog, setShowDialog,model,countiQ)
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


@ExperimentalAnimationApi
@Composable
fun MainPage(navController: NavController,model: MainViewModel,shared: SharedPreferences,context: Context,conectar:Boolean,context2:Context) {
    //model.actualizaJson(shared.getString("id","")!!.toLong())
    val computadoras=model.UserComputers.observeAsState()
    val (computers, setcomputers) =  remember { mutableStateOf(listOf<UserMachine>()) }
    var textresponseP by remember { mutableStateOf("") }
    val textresponse=model.toComputers.observeAsState()
    val textresponse2=model.responsemaquina2.observeAsState()
    val (showDialog, setShowDialog) =  remember { mutableStateOf(model.trialinit.value!!) }

    conectar.let{
        if (it){
            model.cimputerJSON(shared.getString("id","")!!.toLong(),setcomputers)
            textresponseP="Bienvenido a Qinaya:"
        }else{
            setcomputers(listOf())
            textresponseP="No hay internet"
        }
    }
    Scaffold(
        topBar = {
                 TopAppBar (
                     title = { Image(painter = painterResource(id = R.drawable.icono_qinaya_adj), contentDescription ="" )},
                     actions = {
                         // RowScope here, so these icons will be placed horizontally
                         IconButton(onClick = {
                             HelpCenterActivity.builder()
                             .show(context2)


                         }) {
                             Icon(Icons.Filled.Help, contentDescription = "Localized description")
                         }
                         IconButton(onClick = {
                             val imeManager =
                                 context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                             imeManager.showInputMethodPicker()

                         }) {
                             Icon(Icons.Filled.Keyboard, contentDescription = "Localized description")
                         }
                         IconButton(onClick = {
                             with(shared.edit()){
                                 putString("active", "false")
                                 commit() }
                             navController.navigate("login_qinaya")

                         }) {
                             Icon(Icons.Filled.ExitToApp, contentDescription = "Localized description")
                         }
                     }
                         )

        },
        content = {
            Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colors.background)) {
                Button(modifier = Modifier.fillMaxWidth(),
                    onClick = {navController.navigate("store") }) {
                    Text("TIENDA")
                    Icon(Icons.Filled.Store, contentDescription = "Localized description")

                }
                Text(textresponseP)
                LazyColumn{
                    items(computers){micompu->
                        Mymachine(micompu = micompu,navController = navController,model = model,id = shared.getString("id","")!!.toLong() )
                            //MyLinux(micompu = micompu,navController = navController,model = model,id = shared.getString("id","")!!.toLong() )
                    }
                }

                //Text("$textresponse")
                //Text("$textresponse2")
                AlertDialogTrial(model,showDialog, setShowDialog)
            }


        }
    )
}

@Composable
fun Remoto(model: MainViewModel,navController: NavController){

    val url=model.linkmaquina.value
    val textresponse2 by model.responsemaquina.observeAsState()
    var clickI by remember { mutableStateOf(false) }
    var enterE by remember { mutableStateOf(false) }
    var backHandlingEnabled by remember { mutableStateOf(true) }
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
fun StoreQinaya(navController: NavController){
    val infiniteTransition = rememberInfiniteTransition()
    val image by infiniteTransition.animateFloat(
       0f,
        1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    var resourseI by remember {
        mutableStateOf(R.drawable.mate1)
    }
    image.let {
        if (image<0.5f){
            resourseI=R.drawable.mate1
        }
        else{
            resourseI=R.drawable.mate2
        }
    }
    Scaffold(
        topBar = {AppBarReturn(navController = navController)},
        content = {
            Column(modifier = Modifier
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                giftcode()
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .border(1.dp, Color.Black)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
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

        }
    )

}

@Composable
fun giftcode(){
    var textCode by remember { mutableStateOf("") }
    Column() {
        Text(text = "Tienes un codigo para redimir")
        Text("activa tu plan aqui")
        Row(){
            OutlinedTextField(value = textCode, onValueChange ={textCode=it},
                label = { Text(text = "Usuario:")})
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Redimir")
            }

        }
    }

}


@Composable
fun AppBarReturn(navController: NavController){
    TopAppBar (
        title = { Image(painter = painterResource(id = R.drawable.icono_qinaya_adj), contentDescription ="" )},
        actions = {
            // RowScope here, so these icons will be placed horizontally
            IconButton(onClick = {
                navController.navigate("main_page")

            }) {
                Icon(Icons.Filled.House, contentDescription = "Localized description")
            }

        }
    )

}
@Composable
fun CountriesScreen(showDialog: Boolean, setShowDialog: (Boolean) -> Unit,model: MainViewModel,counQ:List<CountriesResponse>) {
    val countriesNames=counQ
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

@ExperimentalAnimationApi
@Composable
fun Mymachine(micompu:UserMachine,navController:NavController,model: MainViewModel,id:Long){
    var expaned by remember{ mutableStateOf(false)}
    Card (modifier = Modifier
        .clickable { expaned = !expaned }
        .fillMaxWidth()
        .padding(15.dp)
        .border(1.dp, Color.Black)){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row( verticalAlignment = Alignment.CenterVertically) {
                Text(text = micompu.nombreMaquina)
                Button(modifier = Modifier.padding(5.dp),

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
                    Row() {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
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
                                    Text(micompu.plan)
                                }
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                        ) {
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
    }
}



