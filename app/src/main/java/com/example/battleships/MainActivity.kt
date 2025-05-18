package com.example.battleships

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Paint.Align
import android.icu.text.RelativeDateTimeFormatter
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animate
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column



import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.Font
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.battleships.ui.theme.BattleShipsTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.core.view.drawToBitmap
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileOutputStream
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

var umode1=0
var umode2=0
var user1=""
var user2=""
var rows=0
var cols=0
var difficulty=0
var np=2;
var nt=1;
var rounds=1;
var cannons1=3;
var cannons2=3;
var u1turn=1;
var u2turn=1;
data class selectedTile(val offset: Offset,val x: Int, val y: Int, val width: Int, val height: Int)
data class Tile(val x: Int, val y: Int, val width: Int, val height: Int)
data class Snapping(val x: Float, val y: Float, val width: Int, val height: Int, val row: Int, val col: Int)
data class Shipposition(val id: Int, var size:Int, var x: Int, var y: Int, var rot: Int, var xcoord: Float, var ycoord: Float)
var grid1: List<Tile> = emptyList()
var grid2:List<Tile> = emptyList()
var u1ships= mutableMapOf<Int, Shipposition>()
var u1shipstrial= mutableMapOf<Int,Shipposition>()
var u2ships= mutableMapOf<Int, Shipposition>()
var u2shipstrial= mutableMapOf<Int,Shipposition>()
var u1hitships= mutableListOf<Shipposition>()
var u2hitships= mutableListOf<Shipposition>()
data class ShipData(val size: Int, val imageRes: Int)
data class PlacedShip(
    val imageRes: Int,
    val size: Int,
    val offsetX: Float,
    val offsetY: Float,
    val rotation: Float
)
data class AttackedTile(val x: Int, val y: Int)
var u1attackedtiles= mutableListOf<AttackedTile>()
var u2attackedtiles= mutableListOf<AttackedTile>()
var tourn1= mutableMapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0, 6 to 0, 7 to 0)
var tourn2=mutableMapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0, 6 to 0, 7 to 0)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BattleShipsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


var hello=0
var last_open = mutableListOf(1)
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)



@Composable

fun Greeting(name: String, modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val widthDp = configuration.screenWidthDp.toFloat()
    val heightDp = configuration.screenHeightDp.toFloat()
    /*val fileOutput = LocalContext.current.openFileOutput("players.txt", Context.MODE_PRIVATE)
    val scoreOutput = LocalContext.current.openFileOutput("scores.txt", Context.MODE_PRIVATE)
    val botscoreOutput = LocalContext.current.openFileOutput("bot.txt", Context.MODE_PRIVATE)*/
    var u1mode by remember { mutableStateOf(1) }
    var u2mode by remember { mutableStateOf(1) }
    var screen_no by remember { mutableStateOf(0) }
    val context = LocalContext.current


    var add_player by remember { mutableStateOf(false) }
    val listState= rememberScrollState()
    var Context1=LocalContext.current
    var dark_mode by remember { mutableStateOf(readsettings(Context1)[0].toBoolean()) }
    var enable_tiles by remember { mutableStateOf(readsettings(Context1)[1].toBoolean()) }
    var sounds by remember { mutableStateOf(readsettings(Context1)[2].toBoolean()) }
    var volume by remember { mutableStateOf(readsettings(Context1)[3].toFloat()) }
    var autoss by remember { mutableStateOf(readsettings(Context1)[4].toBoolean()) }
    val refresh = remember { mutableStateOf(false) }
    var correct1 by remember { mutableStateOf(Color.Green) }
    LaunchedEffect(Unit) {
        MusicController.start(context)
    }
    if (sounds==false){
        volume=0f
    }
    LaunchedEffect(volume) {
        MusicController.setVolume(volume)
    }
    DisposableEffect(Unit) {
        onDispose {
            MusicController.stop()
        }
    }
    if (!dark_mode ){
        correct1=Color.Red
    }
    else{
        correct1=Color.Green
    }
    var correct2 by remember { mutableStateOf(Color.Green) }
    if (!enable_tiles ){
        correct2=Color.Red
    }
    else{
        correct2=Color.Green
    }
    var correct3 by remember { mutableStateOf(Color.Green) }
    if (!sounds ){
        correct3=Color.Red
    }
    else{
        correct3=Color.Green
    }
    var correct4 by remember { mutableStateOf(Color.Green) }
    if (!autoss ){
        correct4=Color.Red
    }
    else{
        correct4=Color.Green
    }
    var fore_color=Color.White
    var back_color=Color.DarkGray
    var contrast_color=Color.Black
    var bccolor1=Color.Cyan
    var bccollor2=Color.Green
    var bccolor3=Color.Yellow
    var fccolor1=Color.Red
    var fccolor2=Color.Blue
    var fccolor=Color.Magenta

    if (!dark_mode){

        fore_color=Color.Black
        back_color=Color.LightGray
        contrast_color=Color.White
        bccolor1=Color.Red
        bccollor2=Color.Blue
        bccolor3=Color.Magenta
        fccolor1=Color.Cyan
        fccolor2=Color.Green
        fccolor=Color.Yellow
    }
    if(screen_no==0){
        umode1=0
        umode2=0
        user1=""
        user2=""
        rows=0
        cols=0
        difficulty=0
        np=2;
        nt=1;
        rounds=1;
        cannons1=3;
        cannons2=3;
        u1turn=1;
        u2turn=1;
        refresh.value=false
        grid1= emptyList()
        grid2 = emptyList()
        u1ships= mutableMapOf<Int, Shipposition>()
        u1shipstrial= mutableMapOf<Int,Shipposition>()
        u2ships= mutableMapOf<Int, Shipposition>()
        u2shipstrial= mutableMapOf<Int,Shipposition>()
        u1hitships= mutableListOf<Shipposition>()
        u2hitships= mutableListOf<Shipposition>()
        u1attackedtiles= mutableListOf<AttackedTile>()
        u2attackedtiles= mutableListOf<AttackedTile>()
        Scaffold (modifier = Modifier.fillMaxSize()){ innerPadding ->
            Column(modifier = Modifier.fillMaxSize().background(back_color).padding(0.dp,(20* heightDp/914).dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.065f))

                println("$widthDp, $heightDp")

                androidx.compose.material3.Text("BATTLESHIPS", fontSize= 50.sp, fontWeight=FontWeight.ExtraBold, color = fore_color)
                androidx.compose.material3.Text("ARMADA", fontSize= 50.sp, fontWeight=FontWeight.ExtraBold, color = fore_color)

                Box(modifier.fillMaxWidth().height((480*heightDp/914).dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.title_backdrop),
                        contentDescription = "Background Image",

                        contentScale = ContentScale.Crop 
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            ,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                    IconButton(onClick = { Soundplayer.click(Context1, sounds);last_open.addLast(screen_no);  screen_no = 1 }, modifier = Modifier.size((150*widthDp/411).dp, (150*heightDp/914).dp) ){
                        Image(
                            painter = painterResource(id = R.drawable.button1),
                            contentDescription = "My Icon",
                            modifier = Modifier.size((150*widthDp/411).dp, (150*heightDp/914).dp)
                        )

                    }
                }
                    }
                Row (modifier = Modifier.fillMaxWidth().padding((20*widthDp/411).dp, (20*heightDp/914).dp), horizontalArrangement = Arrangement.SpaceEvenly){
                    Column {
                        IconButton(onClick = { Soundplayer.click(Context1, sounds); screen_no = 2; last_open.addLast(0) }, modifier = Modifier.size((80*widthDp/411).dp, (80*heightDp/914).dp).clip(
                            CircleShape).background(fore_color)) {
                            IconButton(onClick = {Soundplayer.click(Context1, sounds); screen_no = 2; last_open.addLast(0)}, modifier = Modifier.size((70*widthDp/411).dp, (70*heightDp/914).dp).clip(
                                CircleShape).background(Color(246,216,21,255))) {
                                    Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Settings", tint=contrast_color, modifier = Modifier.size((60*widthDp/411).dp, (60*heightDp/914).dp))
                            }

                        }
                    }
                    Column {
                        IconButton(onClick = { Soundplayer.click(Context1, sounds); screen_no = 30; last_open.addLast(0) }, modifier = Modifier.size((80*widthDp/411).dp, (80*heightDp/914).dp).clip(
                            CircleShape).background(fore_color)) {
                            IconButton(onClick = { Soundplayer.click(Context1, sounds); screen_no =30; last_open.addLast(0)}, modifier = Modifier.size((70*widthDp/411).dp, (70*heightDp/914).dp).clip(
                                CircleShape).background(Color(246,216,21,255))) {
                                Icon(painter = painterResource(R.drawable.history), contentDescription = "History", tint=contrast_color, modifier = Modifier.size((60*widthDp/411).dp, (60*heightDp/914).dp))
                            }

                        }
                    }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(
                                onClick = {Soundplayer.click(Context1, sounds); screen_no = 4; last_open.addLast(0) }, modifier = Modifier.size((80*widthDp/411).dp, (80*heightDp/914).dp).clip(
                                    CircleShape
                                ).background(fore_color)
                            ) {
                                IconButton(
                                    onClick = {Soundplayer.click(Context1, sounds); screen_no = 4; last_open.addLast(0) },
                                    modifier = Modifier.size((70*widthDp/411).dp, (70*heightDp/914).dp).clip(
                                        CircleShape
                                    ).background(Color(246, 216, 21, 255))
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.List,
                                        contentDescription = "Settings",
                                        tint = contrast_color,
                                        modifier = Modifier.size((60*widthDp/411).dp, (60*heightDp/914).dp)
                                    )
                                }

                            }
                        }
                        Column () {
                            IconButton(onClick = { Soundplayer.click(Context1, sounds);screen_no = 3; last_open.addLast(0) }, modifier = Modifier.size((80*widthDp/411).dp, (80*heightDp/914).dp).clip(
                                CircleShape).background(fore_color)) {
                                IconButton(onClick = {Soundplayer.click(Context1, sounds); screen_no = 3; last_open.addLast(0)}, modifier = Modifier.size((70*widthDp/411).dp, (70*heightDp/914).dp).clip(
                                    CircleShape).background(Color(246,216,21,255))) {
                                    Text("?", fontSize = 60.sp, color = contrast_color)
                                }

                            }
                        }




                }
            }
        }
    }
    if (screen_no==1){
        np=2
        nt=1
        Scaffold (topBar = {
            Column(modifier = Modifier.background(contrast_color)) {
                TopAppBar(title = {
                    IconButton(onClick = {Soundplayer.click(Context1, sounds); screen_no = last_open.last(); last_open.removeLast() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = fore_color
                        )
                    }
                },  colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = contrast_color))
            }
        }, modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.fillMaxSize().background(back_color).padding((20*widthDp/411).dp, (20*heightDp/914).dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))
                Text("GAME MODE", fontSize = 40.sp, color= fore_color, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))
                IconButton(onClick = {Soundplayer.click(Context1, sounds);screen_no=5; last_open.addLast(1)}, modifier=Modifier.clip(RoundedCornerShape((50*heightDp/914).dp)).background(bccolor1).size((200*widthDp/411).dp, (80*heightDp/914).dp)) {
                    Row (modifier= Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                        Text("PVP", color = fccolor1, fontSize = 25.sp, fontFamily = FontFamily(Font(R.font.macondo, FontWeight.Normal)))
                        Icon(imageVector = Icons.Outlined.Person, contentDescription = "PVP", tint= contrast_color, modifier = Modifier.requiredSize((60*widthDp/411).dp, (60*heightDp/914).dp))
                    }
                }
                Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))

                IconButton(onClick = {Soundplayer.click(Context1, sounds);screen_no=5; np=1; last_open.addLast(1)}, modifier=Modifier.clip(RoundedCornerShape((50*widthDp/411).dp)).background(bccollor2).size((200*widthDp/411).dp, (80*heightDp/914).dp)) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Bot  ",
                            color = fccolor2,
                            fontSize = 25.sp,
                            fontFamily = FontFamily(Font(R.font.macondo, FontWeight.Normal))
                        )
                        Icon(
                            painter = painterResource(R.drawable.robot),
                            contentDescription = "Bot",
                            tint = contrast_color,
                            modifier = Modifier.size((60*widthDp/411).dp, (60*heightDp/914).dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))
                IconButton(onClick = {Soundplayer.click(Context1, sounds);screen_no=5; nt=2; last_open.addLast(1)}, modifier=Modifier.clip(RoundedCornerShape((50*heightDp/914).dp)).background(bccolor3).size((200*widthDp/411).dp, (80*heightDp/914).dp)) {
                    Row (modifier= Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                        Text("Tourn", color = fccolor, fontSize = 25.sp, fontFamily = FontFamily(Font(R.font.macondo, FontWeight.Normal)))
                        Icon(painter = painterResource(R.drawable.tophy), contentDescription = "Tournament", tint= contrast_color, modifier = Modifier.size((60*widthDp/411).dp, (60*heightDp/914).dp))
                    }
                }

            }


        }
    }
    if (screen_no==2) {
        Scaffold(topBar = {
            Column {
                TopAppBar(
                    title = {
                        IconButton(onClick = {
                            screen_no = last_open.last(); last_open.removeLast()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = fore_color
                            )
                        }
                    }, colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = contrast_color
                    )
                )
            }
        }, modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().background(back_color).padding((50*widthDp/411).dp, (50*heightDp/914).dp)
                    .verticalScroll(listState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                val context=LocalContext.current
                val settings= readsettings(context)
                Row {
                    Text("Dark Mode: ", color=fore_color, fontStyle = FontStyle(R.font.macondo))
                    Spacer(modifier=Modifier.fillMaxHeight().width((30*widthDp/411).dp))
                    IconButton(onClick = { Soundplayer.click(Context1, sounds);editsettings(context, 0, 100f); dark_mode=!dark_mode }, modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp))
                        .background(correct1)
                        .size((100*widthDp/411).dp, (40*heightDp/914).dp)) {
                        if (settings[0]=="true") {
                            Row (modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){
                                Text("On", color = fore_color)
                                Spacer(modifier=Modifier.fillMaxHeight().width((10*widthDp/411).dp))
                                Icon(
                                    painter = painterResource(R.drawable.circle),
                                    contentDescription = "Check",
                                    tint = fore_color
                                )
                            }
                        }
                        else {
                            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(R.drawable.circle),
                                    contentDescription = "Check",
                                    tint = fore_color
                                )
                                Spacer(modifier=Modifier.fillMaxHeight().width((10*widthDp/411).dp))
                                Text("Off", color = fore_color)
                            }
                        }

                    }
                }
                Spacer(modifier = Modifier.fillMaxWidth().height((30*heightDp/914).dp))
                Row {

                    Text("Randomize Tile\n Size: ", color=fore_color, fontStyle = FontStyle(R.font.macondo))
                    Spacer(modifier=Modifier.fillMaxHeight().width((30*widthDp/411).dp))
                    IconButton(onClick = {Soundplayer.click(Context1, sounds); editsettings(context, 1, 100f); enable_tiles=!enable_tiles}, modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp))
                        .background(correct2)
                        .size((100*widthDp/411).dp, (40*heightDp/914).dp)) {
                        if (settings[1]=="true") {
                            Row (modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){
                                Text("On", color = fore_color)
                                Spacer(modifier=Modifier.fillMaxHeight().width((10*widthDp/411).dp))
                                Icon(
                                    painter = painterResource(R.drawable.circle),
                                    contentDescription = "Check",
                                    tint = fore_color
                                )
                            }
                        }
                        else {
                            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(R.drawable.circle),
                                    contentDescription = "Check",
                                    tint = fore_color
                                )
                                Spacer(modifier=Modifier.fillMaxHeight().width((10*widthDp/411).dp))
                                Text("Off", color = fore_color)
                            }
                        }

                    }
                }
                Spacer(modifier = Modifier.fillMaxWidth().height((30*heightDp/914).dp))
                Row {

                    Text("Allow Sounds: ", color=fore_color, fontStyle = FontStyle(R.font.macondo))
                    Spacer(modifier=Modifier.fillMaxHeight().width((30*widthDp/411).dp))
                    IconButton(onClick = { Soundplayer.click(Context1, sounds);editsettings(context, 2, 100f); sounds=!sounds}, modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp))
                        .background(correct3)
                        .size((100*widthDp/411).dp, (40*heightDp/914).dp)) {
                        if (settings[2]=="true") {
                            Row (modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){
                                Text("On", color = fore_color)
                                Spacer(modifier=Modifier.fillMaxHeight().width((10*widthDp/411).dp))
                                Icon(
                                    painter = painterResource(R.drawable.circle),
                                    contentDescription = "Check",
                                    tint = fore_color
                                )
                            }
                        }
                        else {
                            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(R.drawable.circle),
                                    contentDescription = "Check",
                                    tint = fore_color
                                )
                                Spacer(modifier=Modifier.fillMaxHeight().width((10*widthDp/411).dp))
                                Text("Off", color = fore_color)
                            }
                        }

                    }
                }
                Spacer(modifier = Modifier.fillMaxWidth().height((30*heightDp/914).dp))
                VolumeSlider(context=Context1,volume = volume, onVolumeChange = { volume = it }, widthDp, heightDp)

                Spacer(modifier = Modifier.fillMaxWidth().height((30*heightDp/914).dp))
                Row {

                    Text("Auto Screenshot: ", color=fore_color, fontStyle = FontStyle(R.font.macondo))
                    Spacer(modifier=Modifier.fillMaxHeight().width((30*widthDp/411).dp))
                    IconButton(onClick = {Soundplayer.click(Context1, sounds); editsettings(context, 4, 100f); autoss=!autoss}, modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp))
                        .background(correct4)
                        .size((100*widthDp/411).dp, (40*heightDp/914).dp)) {
                        if (settings[4]=="true") {
                            Row (modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){
                                Text("On", color = fore_color)
                                Spacer(modifier=Modifier.fillMaxHeight().width((10*widthDp/411).dp))
                                Icon(
                                    painter = painterResource(R.drawable.circle),
                                    contentDescription = "Check",
                                    tint = fore_color
                                )
                            }
                        }
                        else {
                            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(R.drawable.circle),
                                    contentDescription = "Check",
                                    tint = fore_color
                                )
                                Spacer(modifier=Modifier.fillMaxHeight().width((10*widthDp/411).dp))
                                Text("Off", color = fore_color)
                            }
                        }

                    }

                }
                Spacer(modifier = Modifier.fillMaxWidth().height((30*heightDp/914).dp))
                Row {
                    IconButton(
                        onClick = {Soundplayer.click(Context1, sounds); resetsettings(context); dark_mode=true; sounds=true; enable_tiles=true;volume=1f; autoss=true},
                        modifier = Modifier.size((240*widthDp/411).dp, (40*heightDp/914).dp).clip(RoundedCornerShape((10*widthDp/411).dp)).background(Color.Red)
                    ) {
                        Text("Reset Settings", color = contrast_color, fontWeight = FontWeight.Bold, fontSize = 20.sp, fontFamily = FontFamily( Font(R.font.galgony, FontWeight.Normal)))
                    }
                }
                Spacer(modifier = Modifier.fillMaxWidth().height((30*heightDp/914).dp))
                Row {
                    IconButton(
                        onClick = {Soundplayer.click(Context1, sounds); clear_history(context)},
                        modifier = Modifier.size((240*widthDp/411).dp, (40*heightDp/914).dp).clip(RoundedCornerShape((10*widthDp/411).dp)).background(Color.Red)
                    ) {
                        Text("Clear Score History", color = contrast_color, fontWeight = FontWeight.Bold, fontSize = 20.sp, fontFamily = FontFamily( Font(R.font.galgony, FontWeight.Normal)))
                    }
                }
                Spacer(modifier = Modifier.fillMaxWidth().height((30*heightDp/914).dp))
                Row {
                    IconButton(
                        onClick = { Soundplayer.click(Context1, sounds);clear_players(context)},
                        modifier = Modifier.size((240*widthDp/411).dp, (40*heightDp/914).dp).clip(RoundedCornerShape((10*widthDp/411).dp)).background(Color.Red)
                    ) {
                        Text("Clear Players", color = contrast_color, fontWeight = FontWeight.Bold, fontSize = 20.sp, fontFamily = FontFamily( Font(R.font.galgony, FontWeight.Normal)))
                    }
                }
            }
        }
    }
    if (screen_no == 3) {
        Scaffold(topBar = {
            Column {
                TopAppBar(title = {
                    IconButton(onClick = {
                        Soundplayer.click(Context1, sounds)
                        screen_no = last_open.last()
                        last_open.removeLast()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = fore_color
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = contrast_color
                ))
            }
        }, modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(back_color)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "HOW TO PLAY BATTLESHIPS ARMADA",
                    style = MaterialTheme.typography.headlineLarge,
                    color = fore_color,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Game Objective
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = contrast_color)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "OBJECTIVE",
                            style = MaterialTheme.typography.titleLarge,
                            color = fore_color,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Sink all of your opponent's ships before they sink yours. " +
                                    "Each player has 4 ships of different sizes to place on their grid.",
                            color = fore_color
                        )
                    }
                }

                // Ship Types
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = contrast_color)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "SHIP FLEET",
                            style = MaterialTheme.typography.titleLarge,
                            color = fore_color,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Ship 1 (3 tiles)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.ship2),
                                contentDescription = "Large Ship",
                                modifier = Modifier.size(100.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("1 Large Ship (3 tiles)", color = fore_color)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Ship 2 (2 tiles)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.ship1),
                                contentDescription = "Medium Ship",
                                modifier = Modifier.size(80.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("1 Medium Ship (2 tiles)", color = fore_color)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Ships 3 & 4 (1 tile each)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.ship3),
                                contentDescription = "Small Ship",
                                modifier = Modifier.size(50.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("2 Small Ships (1 tile each)", color = fore_color)
                        }
                    }
                }

                // Game Phases
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = contrast_color)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "GAME PHASES",
                            style = MaterialTheme.typography.titleLarge,
                            color = fore_color,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Deployment Phase
                        Text(
                            "1. DEPLOYMENT PHASE",
                            style = MaterialTheme.typography.titleMedium,
                            color = fore_color,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "- Drag and drop your ships onto your grid\n" +
                                    "- Rotate ships by clicking on them\n" +
                                    "- Use the AUTO button for automatic placement\n" +
                                    "- Click SAVE when all ships are placed",
                            color = fore_color
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Battle Phase
                        Text(
                            "2. BATTLE PHASE",
                            style = MaterialTheme.typography.titleMedium,
                            color = fore_color,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "- Take turns attacking opponent's grid\n" +
                                    "- Tap a tile to select it, tap again to fire\n" +
                                    "- Gray = Miss, Red = Hit\n" +
                                    "- Destroyed ships are highlighted",
                            color = fore_color
                        )
                    }
                }

                // Game Modes
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = contrast_color)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "GAME MODES",
                            style = MaterialTheme.typography.titleLarge,
                            color = fore_color,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // PvP
                        Text(
                            "• PLAYER VS PLAYER (PVP)",
                            style = MaterialTheme.typography.titleMedium,
                            color = fore_color
                        )
                        Text(
                            "Challenge a friend on the same device",
                            color = fore_color
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Bot
                        Text(
                            "• PLAYER VS BOT",
                            style = MaterialTheme.typography.titleMedium,
                            color = fore_color
                        )
                        Text(
                            "Play against AI with 3 difficulty levels:\n" +
                                    "- Easy: Random attacks\n" +
                                    "- Medium: Some strategy\n" +
                                    "- Hard: Advanced targeting",
                            color = fore_color
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Tournament
                        Text(
                            "• TOURNAMENT MODE",
                            style = MaterialTheme.typography.titleMedium,
                            color = fore_color
                        )
                        Text(
                            "Compete in a best-of series (3, 5, or 7 games)",
                            color = fore_color
                        )
                    }
                }

                // Scoring
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = contrast_color)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "SCORING",
                            style = MaterialTheme.typography.titleLarge,
                            color = fore_color,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "- Small Ship: 150 points\n" +
                                    "- Medium Ship: 300 points\n" +
                                    "- Large Ship: 450 points\n" ,

                            color = fore_color
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Track your scores and compare with friends!",
                            color = fore_color,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        Soundplayer.click(Context1, sounds)
                        screen_no = last_open.last()
                        last_open.removeLast()
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("BACK TO GAME")
                }
            }
        }
    }
    if (screen_no==4) {

        Scaffold(topBar = {
            Column {
                TopAppBar(title = {
                    IconButton(onClick = { screen_no = last_open.last(); last_open.removeLast() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = fore_color
                        )
                    }
                },   colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = contrast_color))
            }
        }, modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().background(back_color).padding((20*widthDp/411).dp, (20*heightDp/914).dp)
                    .verticalScroll(listState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                val scores=scoreList(LocalContext.current).toList().sortedByDescending { it.second[0].toInt() }.toMap()
                var counter=1
                Row(modifier=Modifier.fillMaxWidth().height((80*heightDp/914).dp).background(color = contrast_color).padding((20*widthDp/411).dp, (0*heightDp/914).dp), verticalAlignment = Alignment.CenterVertically){

                    Text( "NAME", color = fore_color, fontWeight = FontWeight.Bold)
                    Text("PvP   Bot",color = fore_color, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right)

                }
                for (i in scores){
                    val a=i.key
                    val b= i.value
                    var color= contrast_color
                    var icon=R.drawable.a1
                    if (counter==1){
                        color= Color(255,215,0,255)

                    }
                    else if (counter==2){
                        color= Color(192,192,192,255)
                        icon =R.drawable.a2
                    }
                    else if (counter==3){
                        color= Color(205,127,50,255)
                        icon =R.drawable.a3
                    }
                    Row(modifier=Modifier.fillMaxWidth().height((80*heightDp/914).dp).background(color = color).padding((20*widthDp/411).dp, (0*heightDp/914).dp), verticalAlignment = Alignment.CenterVertically){
                        if (counter<4){
                            Icon(painter = painterResource(icon), contentDescription="Trophy", modifier = Modifier.size((60*widthDp/411).dp, (60*heightDp/914).dp), tint = fore_color)
                            Spacer(Modifier.fillMaxHeight().size((20*widthDp/411).dp, (20*heightDp/914).dp))
                        }
                        Text( a.toUpperCase(), color = fore_color, fontWeight = FontWeight.Bold)
                        Text("${b[0]}         ${b[1]}",color = fore_color, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right)

                    }
                    counter+=1
                }


            }
        }

    }
    if (screen_no==5) {

        Scaffold(topBar = {
            Column {
                TopAppBar(title = {
                    IconButton(onClick = {Soundplayer.click(Context1, sounds); screen_no = last_open.last(); last_open.removeLast() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = fore_color
                        )
                    }
                },  colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = contrast_color))
            }
        }, modifier = Modifier.fillMaxSize()) { innerPadding ->
            val scrollState = rememberScrollState()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(back_color)
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding((20*widthDp/411).dp, (20*heightDp/914).dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var playerList = loadPlayerNames(context = LocalContext.current)
                    var playerName by remember { mutableStateOf("") }

                    if (add_player) {
                        OutlinedTextField(
                            value = playerName,
                            onValueChange = { playerName = it },
                            label = { Text("Enter player name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height((20*heightDp/914).dp))
                    }

                    for (i in playerList) {
                        IconButton(
                            onClick = { Soundplayer.click(Context1, sounds);last_open.addLast(5); user1=i; playerList.toMutableList().remove(i); if (np==2 || nt==2) {screen_no=6}; else{ screen_no=8} },
                            modifier = Modifier
                                .clip(RoundedCornerShape((50*widthDp/411).dp))
                                .background(fore_color)
                                .size((200*widthDp/411).dp, (80*heightDp/914).dp)
                        ) {
                            Text(i, color = contrast_color, fontSize = 25.sp,
                                fontFamily = FontFamily(Font(R.font.galgony, FontWeight.Normal)))
                        }
                        Spacer(modifier = Modifier.height((20*heightDp/914).dp))
                    }

                    var context = LocalContext.current
                    IconButton(
                        onClick = {Soundplayer.click(Context1, sounds);
                            if (add_player && (playerName != "")) {
                                savePlayerNames(context, playerName)
                                playerName = ""
                                last_open.addLast(5)
                            }
                            add_player = !add_player
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape((50*widthDp/411).dp))
                            .background(bccollor2)
                            .size((200*widthDp/411).dp, (80*heightDp/914).dp)
                    ) {
                        if (add_player) {
                            Icon(imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Add", tint = fore_color)
                        } else {
                            Icon(imageVector = Icons.Default.AddCircle,
                                contentDescription = "Add", tint = fore_color)
                        }
                    }
                    Spacer(modifier = Modifier.height((100*heightDp/914).dp))
                }
            }
        }
    }
    if (screen_no==6){
        Scaffold(topBar = {
            Column {
                TopAppBar(title = {
                    IconButton(onClick = { screen_no = last_open.last(); last_open.removeLast() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = fore_color
                        )
                    }
                },  colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = contrast_color))
            }
        }, modifier = Modifier.fillMaxSize()) { innerPadding ->
            val scrollState = rememberScrollState()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(back_color)
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding((20*widthDp/411).dp, (20*heightDp/914).dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var playerList = loadPlayerNames(context = LocalContext.current).toMutableList()
                    var playerName by remember { mutableStateOf("") }
                    playerList.remove(user1)

                    if (add_player) {
                        OutlinedTextField(
                            value = playerName,
                            onValueChange = { playerName = it },
                            label = { Text("Enter player name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height((20*heightDp/914).dp))
                    }

                    for (i in playerList) {
                        IconButton(
                            onClick = { Soundplayer.click(Context1, sounds);last_open.addLast(5); user2=i; if (nt==2) {screen_no=9} else {screen_no=7} },
                            modifier = Modifier
                                .clip(RoundedCornerShape((50*widthDp/411).dp))
                                .background(fore_color)
                                .size((200*widthDp/411).dp, (80*heightDp/914).dp)
                        ) {
                            Text(i, color = contrast_color, fontSize = 25.sp,
                                fontFamily = FontFamily(Font(R.font.galgony, FontWeight.Normal)))
                        }
                        Spacer(modifier = Modifier.height((20*heightDp/914).dp))
                    }

                    var context = LocalContext.current
                    IconButton(
                        onClick = {Soundplayer.click(Context1, sounds);
                            if (add_player && (playerName != "")) {
                                savePlayerNames(context, playerName)
                                playerName = ""
                                last_open.addLast(5)
                            }
                            add_player = !add_player

                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape((50*widthDp/411).dp))
                            .background(bccollor2)
                            .size((200*widthDp/411).dp, (80*heightDp/914).dp)
                    ) {
                        if (add_player) {
                            Icon(imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Add", tint = fore_color)
                        } else {
                            Icon(imageVector = Icons.Default.AddCircle,
                                contentDescription = "Add", tint = fore_color)
                        }
                    }
                    Spacer(modifier = Modifier.height((100*heightDp/914).dp))
                }
            }
        }
    }
    if (screen_no==7){
        Column(
            modifier = Modifier
                .fillMaxSize().background(contrast_color)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("CHOOSE THE GRID SIZE", textAlign = TextAlign.Center, color = fore_color, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            for (j in 4..6) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Absolute.Center){
                for (i in 4..6) {
                    IconButton(onClick = {Soundplayer.click(Context1, sounds);rows=j; cols=i; grid1= generateGrid(rows,cols,enable_tiles); grid2=
                        generateGrid(rows, cols,enable_tiles);  if (np==1) screen_no=12;else if (rounds==1 ) screen_no=10; else if (rounds>1) screen_no=11}, modifier=Modifier.height((180*heightDp/914).dp).width((110+21*(i-4)).dp).clip(RectangleShape).background(contrast_color) ) {
                        Row(Modifier.padding((10*widthDp/411).dp, (10*heightDp/914).dp).fillMaxSize(), horizontalArrangement = Arrangement.Center) {
                            for (k in 1..i) {
                                Column(Modifier.padding((5*widthDp/411).dp, (5*heightDp/914).dp)) {
                                    for (l in 1..j) {
                                        Box(
                                            modifier = Modifier
                                                .size((10*widthDp/411).dp, (10*heightDp/914).dp)
                                                .background(
                                                    if ((i + j) % 2 == 0) {
                                                        Color.Blue
                                                    } else {
                                                        Color.Green
                                                    }
                                                ) 
                                        )
                                        Spacer(modifier = Modifier.height((10*heightDp/914).dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width((1*widthDp/411).dp))
                            }
                        }
                    }
                }
                    Spacer(modifier = Modifier.fillMaxHeight().width((20*widthDp/411).dp))
            }
            }
        }

        }
    if (screen_no==8){
        Scaffold (topBar = {
            Column(modifier = Modifier.background(contrast_color)) {
                TopAppBar(title = {
                    IconButton(onClick = { Soundplayer.click(Context1, sounds);screen_no = last_open.last(); last_open.removeLast() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = fore_color
                        )
                    }
                },  colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = contrast_color))
            }
        }, modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.fillMaxSize().background(back_color).padding((20*widthDp/411).dp, (20*heightDp/914).dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))
                Text(
                    "CHOOSE DIFFICULTY",
                    fontSize = 30.sp,
                    color = fore_color,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))
                IconButton(
                    onClick = {Soundplayer.click(Context1, sounds);difficulty=0; screen_no = 7; last_open.addLast(8) },
                    modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp)).background(bccolor1)
                        .size((200*widthDp/411).dp, (80*heightDp/914).dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "EASY",
                            color = fccolor1,
                            fontSize = 25.sp,
                            fontFamily = FontFamily(Font(R.font.macondo, FontWeight.Normal))
                        )

                    }
                }
                Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))
                IconButton(
                    onClick = {Soundplayer.click(Context1, sounds);difficulty=1; screen_no = 7; last_open.addLast(8) },
                    modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp)).background(bccollor2)
                        .size((200*widthDp/411).dp, (80*heightDp/914).dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "MEDIUM",
                            color = fccolor2,
                            fontSize = 25.sp,
                            fontFamily = FontFamily(Font(R.font.macondo, FontWeight.Normal))
                        )

                    }
                }
                Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))
                IconButton(
                    onClick = {Soundplayer.click(Context1, sounds);difficulty=2; screen_no = 7; last_open.addLast(8) },
                    modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp)).background(bccolor3)
                        .size((200*widthDp/411).dp, (80*heightDp/914).dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "HARD",
                            color = fccolor,
                            fontSize = 25.sp,
                            fontFamily = FontFamily(Font(R.font.macondo, FontWeight.Normal))
                        )

                    }
                }
                Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))
            }


        }

    }
    if (screen_no==9){
        Scaffold (topBar = {
            Column(modifier = Modifier.background(contrast_color)) {
                TopAppBar(title = {
                    IconButton(onClick = { screen_no = last_open.last(); last_open.removeLast() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = fore_color
                        )
                    }
                },  colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = contrast_color))
            }
        }, modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.fillMaxSize().background(back_color).padding((20*widthDp/411).dp, (20*heightDp/914).dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))
                Text(
                    "NO. OF ROUNDS",
                    fontSize = 30.sp,
                    color = fore_color,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))
                IconButton(
                    onClick = {Soundplayer.click(Context1, sounds);rounds=3; screen_no = 7; last_open.addLast(8) },
                    modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp)).background(bccolor1)
                        .size((200*widthDp/411).dp, (80*heightDp/914).dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "3",
                            color = fccolor1,
                            fontSize = 25.sp,
                            fontFamily = FontFamily(Font(R.font.macondo, FontWeight.Normal))
                        )

                    }
                }
                Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))
                IconButton(
                    onClick = {Soundplayer.click(Context1, sounds);rounds=5; screen_no = 7; last_open.addLast(8) },
                    modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp)).background(bccollor2)
                        .size((200*widthDp/411).dp, (80*heightDp/914).dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("5",
                            color = fccolor2,
                            fontSize = 25.sp,
                            fontFamily = FontFamily(Font(R.font.macondo, FontWeight.Normal))
                        )

                    }
                }
                Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))
                IconButton(
                    onClick = {Soundplayer.click(Context1, sounds);rounds=7; screen_no = 7; last_open.addLast(8) },
                    modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp)).background(bccolor3)
                        .size((200*widthDp/411).dp, (80*heightDp/914).dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "7",
                            color = fccolor,
                            fontSize = 25.sp,
                            fontFamily = FontFamily(Font(R.font.macondo, FontWeight.Normal))
                        )

                    }
                }
                Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))
            }


        }

    }


    if (screen_no==10) {

        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                Spacer(modifier = Modifier.height((15*heightDp/914).dp))
                Row(
                    modifier = Modifier.fillMaxWidth().height((60*heightDp/914).dp)
                        .background(if (u1mode == 1) Color.Red; else Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Spacer(modifier = Modifier.width((30*widthDp/411).dp))
                    if (cannons1 == 0) {
                        umode1 = 2
                        u1mode = 2
                        umode2 = 1
                        u2mode = 1
                    }
                    if (cannons2 == 0) {
                        umode1 = 1
                        u1mode = 1
                        umode2 = 2
                        u2mode = 2
                    }

                    IconButton(
                        onClick = {Soundplayer.click(Context1, sounds);
                            if (umode1 == 0 || u1turn != u2turn) {
                            }; else if (u1mode == 1) {
                                u1mode = 2; umode1 = 2;
                            } else {
                                u1mode = 1; umode1 = 1
                            }
                        }, modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp))
                            .background(
                                if (u1mode == 1 && umode1 != 0) Color(
                                    211,
                                    110,
                                    110,
                                    255
                                ) else Color.DarkGray
                            )
                            .size((100*widthDp/411).dp, (30*heightDp/914).dp)
                    ) {
                        if (u1mode == 1) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.compass),
                                    contentDescription = "Check",
                                    tint = Color.Black
                                )
                                Spacer(modifier = Modifier.fillMaxHeight().width((10*widthDp/411).dp))

                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {


                                Icon(
                                    painter = painterResource(R.drawable.electric),
                                    contentDescription = "Check",
                                    tint = Color.White
                                )
                            }
                        }

                    }
                    Spacer(modifier = Modifier.width((20*widthDp/411).dp))
                    if (umode1 == 0) {
                        Text("⅄OꓶԀƎꓷ", color = Color.White, fontWeight = FontWeight.Bold)
                    } else if (u1mode == 1) {
                        Text("ꓘƆⱯꓕꓕⱯ", color = Color.White, fontWeight = FontWeight.Bold)
                    } else {
                        Text("ꓷNƎℲƎꓷ", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width((30*widthDp/411).dp))
                    for (i in 0..(2 - cannons1)) {
                        Box(
                            modifier = Modifier.height((30*heightDp/914).dp).width((30*widthDp/411).dp).clip(CircleShape)
                                .background(Color.Black)
                        ) {}
                        Spacer(modifier = Modifier.width((10*widthDp/411).dp))
                    }
                    for (i in 0..cannons1 - 1) {

                        Box(
                            modifier = Modifier.height((30*heightDp/914).dp).width((30*widthDp/411).dp).clip(CircleShape)
                                .background(if (u1mode == 1 && umode1 != 0) Color.White else Color.Black)
                        ) {}
                        Spacer(modifier = Modifier.width((10*widthDp/411).dp))
                    }


                }

                grid(grid1, grid2, refresh,sounds, widthDp, heightDp)

                Row(
                    modifier = Modifier.fillMaxWidth().height((60*heightDp/914).dp)
                        .background(if (u2mode == 1) Color.Red; else Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width((30*widthDp/411).dp))
                    for (i in 0..cannons2 - 1) {

                        Box(
                            modifier = Modifier.height((30*heightDp/914).dp).width((30*widthDp/411).dp).clip(CircleShape)
                                .background(if (u2mode == 1) Color.White else Color.Black)
                        ) {}
                        Spacer(modifier = Modifier.width((10*widthDp/411).dp))
                    }
                    for (i in 0..(2 - cannons2)) {
                        Box(
                            modifier = Modifier.height((30*heightDp/914).dp).width((30*widthDp/411).dp).clip(CircleShape)
                                .background(Color.Black)
                        ) {}
                        Spacer(modifier = Modifier.width((10*widthDp/411).dp))
                    }
                    Spacer(modifier = Modifier.width((20*widthDp/411).dp))
                    if (umode2 == 0) {
                        Text("DEPLOY", color = Color.White, fontWeight = FontWeight.Bold)
                    } else if (u2mode == 1) {
                        Text("ATTACK", color = Color.White, fontWeight = FontWeight.Bold)
                    } else {
                        Text("DEFEND", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width((20*widthDp/411).dp))
                    IconButton(
                        onClick = {Soundplayer.click(Context1, sounds);
                            if (umode2 == 0 || u1turn == u2turn) {
                            }; else if (u2mode == 1) {
                                u2mode = 2; umode2 = 2;
                            } else {
                                u2mode = 1; umode2 = 1; }
                        }, modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp))
                            .background(if (u2mode == 1) Color(211, 110, 110, 255) else Color.Black)
                            .size((100*widthDp/411).dp, (30*heightDp/914).dp)
                    ) {
                        if (u2mode == 1) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {

                                Spacer(modifier = Modifier.fillMaxHeight().width((10*widthDp/411).dp))
                                Icon(
                                    painter = painterResource(R.drawable.compass),
                                    contentDescription = "Check",
                                    tint = Color.Black
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.electric),
                                    contentDescription = "Check",
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.fillMaxHeight().width((10*widthDp/411).dp))

                            }
                        }

                    }

                }

            }
        }
        if ( refresh.value) {
            var score1 = 0
            var score2 = 0
            var ship11 = 0
            var ship12 = 0
            var ship13 = 0
            var ship14 = 0
            var ship21 = 0
            var ship22 = 0
            var ship23 = 0
            var ship24 = 0
            for (i in u1hitships) {
                if (i.id == 1) {
                    ship21 = 1
                }
                if (i.id == 2) {
                    ship22 = 1
                }
                if (i.id == 3) {
                    ship23 = 1
                }
                if (i.id == 4) {
                    ship24 = 1
                }
            }
            for (i in u2hitships) {
                if (i.id == 1) {
                    ship11 = 1
                }
                if (i.id == 2) {
                    ship12 = 1
                }
                if (i.id == 3) {
                    ship13 = 1
                }
                if (i.id == 4) {
                    ship14 = 1
                }
            }
            score1 = ship11 * 150 + ship12 * 450 + ship13 * 300 + ship14 * 450
            score2 = ship21 * 150 + ship22 * 450 + ship23 * 300 + ship24 * 450

            var winner = user1
            var loser = user2
            var wscore = score1
            var lscore = score2

            Column(
                modifier = Modifier.fillMaxSize().background(Color(0, 0, 0, 100))
                    ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier=Modifier.clip(RoundedCornerShape((30*widthDp/411).dp)).size((400*widthDp/411).dp, (400*heightDp/914).dp).background(Color.Cyan)) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(

                            modifier = Modifier.size((400*widthDp/411).dp, (300*heightDp/914).dp).clip(RoundedCornerShape((30*widthDp/411).dp))
                                .background(Color.White)
                        ) {
                            Column(


                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Spacer(modifier = Modifier.fillMaxWidth().height((20*heightDp/914).dp))
                                if (score2 == score1) {
                                    Row(modifier.fillMaxWidth().background(Color(227,182,57,255)), horizontalArrangement = Arrangement.Center) {
                                        Text(
                                            "IT'S A TIE",
                                            fontSize = 30.sp,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    winner=""
                                    Spacer(modifier = Modifier.fillMaxWidth().height((20*heightDp/914).dp))

                                    Text(
                                        "$user1          $score1".toUpperCase(),
                                        color = fccolor1,
                                        fontSize = 25.sp,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.galgony,
                                                FontWeight.Bold
                                            )
                                        )
                                    )


                                    Spacer(modifier = Modifier.fillMaxWidth().height((30*heightDp/914).dp))
                                    Text(
                                        if (np==1) "BOT          $score2"; else "$user2          $score2".toUpperCase(),
                                        color = fccolor1,
                                        fontSize = 25.sp,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.galgony,
                                                FontWeight.Normal
                                            )
                                        )
                                    )
                                    Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))

                                } else {
                                    if (score2 > score1) {
                                        winner = user2
                                        loser = user1
                                        wscore = score2
                                        lscore = score1
                                    }
                                    if (winner==""){
                                        winner="BOT"
                                    }
                                    Row(modifier.fillMaxWidth().background(Color(227,182,57,255)), horizontalArrangement = Arrangement.Center) {
                                        Text(
                                            "THE WINNER IS ${winner.toUpperCase()}",
                                            fontSize = 30.sp,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                                        )
                                    }
                                    Spacer(modifier = Modifier.fillMaxWidth().height((20*heightDp/914).dp))

                                    Text(
                                        "$user1          $score1".toUpperCase(),
                                        color = fccolor1,
                                        fontSize = 25.sp,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.galgony,
                                                FontWeight.Bold
                                            )
                                        )
                                    )


                                    Spacer(modifier = Modifier.fillMaxWidth().height((30*heightDp/914).dp))
                                    Text(
                                        if (np==1) "BOT          $score2"; else "$user2          $score2".toUpperCase().toUpperCase(),
                                        color = fccolor1,
                                        fontSize = 25.sp,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.galgony,
                                                FontWeight.Normal
                                            )
                                        )
                                    )
                                    Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.fillMaxWidth().height((20*heightDp/914).dp))
                        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                            if (np!=1) {
                                IconButton(
                                    onClick = {Soundplayer.click(Context1, sounds);
                                        if (winner != "") score_update(
                                            Context1,
                                            winner
                                        );screen_no = 20;
                                    },
                                    modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp))
                                        .background(Color.Black)
                                        .size((150*widthDp/411).dp, (60*heightDp/914).dp)
                                ) {
                                    Text(
                                        "PLAY AGAIN",
                                        textAlign = TextAlign.Center,
                                        color = Color.White, fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width((20*widthDp/411).dp))
                            }
                        IconButton(
                            onClick = {Soundplayer.click(Context1, sounds);if (winner!="" && np!=1) score_update(Context1, winner); if (np==1 && score1>score2) bot_update(Context1, user1);screen_no = 0; },
                            modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp))
                                .background(Color.White)
                                .size((150*widthDp/411).dp, (60*heightDp/914).dp)
                        ) {
                            Text(
                                "HOME",
                                textAlign = TextAlign.Center,
                                color = Color.Black, fontWeight = FontWeight.Bold
                            )
                        }
                            }

                    }
                }
            }
            val context = LocalContext.current
            CaptureScreenshotOnGameEnd( captureTrigger = true) { bitmap ->
                saveBitmapToStorage(context, bitmap) // write it to storage
            }


        }
    }
    if (screen_no==11){
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                Spacer(modifier = Modifier.height((15*heightDp/914).dp))
                Row(
                    modifier = Modifier.fillMaxWidth().height((60*heightDp/914).dp)
                        .background(if (u1mode == 1) Color.Red; else Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Spacer(modifier = Modifier.width((30*widthDp/411).dp))
                    if (cannons1 == 0) {
                        umode1 = 2
                        u1mode = 2
                        umode2 = 1
                        u2mode = 1
                    }
                    if (cannons2 == 0) {
                        umode1 = 1
                        u1mode = 1
                        umode2 = 2
                        u2mode = 2
                    }

                    IconButton(
                        onClick = {Soundplayer.click(Context1, sounds);
                            if (umode1 == 0 || u1turn != u2turn) {
                            }; else if (u1mode == 1) {
                                u1mode = 2; umode1 = 2;
                            } else {
                                u1mode = 1; umode1 = 1
                            }
                        }, modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp))
                            .background(
                                if (u1mode == 1 && umode1 != 0) Color(
                                    211,
                                    110,
                                    110,
                                    255
                                ) else Color.DarkGray
                            )
                            .size((100*widthDp/411).dp, (30*heightDp/914).dp)
                    ) {
                        if (u1mode == 1) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.compass),
                                    contentDescription = "Check",
                                    tint = Color.Black
                                )
                                Spacer(modifier = Modifier.fillMaxHeight().width((10*widthDp/411).dp))

                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {


                                Icon(
                                    painter = painterResource(R.drawable.electric),
                                    contentDescription = "Check",
                                    tint = Color.White
                                )
                            }
                        }

                    }
                    Spacer(modifier = Modifier.width((20*widthDp/411).dp))
                    if (umode1 == 0) {
                        Text("⅄OꓶԀƎꓷ", color = Color.White, fontWeight = FontWeight.Bold)
                    } else if (u1mode == 1) {
                        Text("ꓘƆⱯꓕꓕⱯ", color = Color.White, fontWeight = FontWeight.Bold)
                    } else {
                        Text("ꓷNƎℲƎꓷ", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width((30*widthDp/411).dp))
                    for (i in 0..(2 - cannons1)) {
                        Box(
                            modifier = Modifier.height((30*heightDp/914).dp).width((30*widthDp/411).dp).clip(CircleShape)
                                .background(Color.Black)
                        ) {}
                        Spacer(modifier = Modifier.width((10*widthDp/411).dp))
                    }
                    for (i in 0..cannons1 - 1) {

                        Box(
                            modifier = Modifier.height((30*heightDp/914).dp).width((30*widthDp/411).dp).clip(CircleShape)
                                .background(if (u1mode == 1 && umode1 != 0) Color.White else Color.Black)
                        ) {}
                        Spacer(modifier = Modifier.width((10*widthDp/411).dp))
                    }


                }

                grid(grid1, grid2, refresh,sounds, widthDp, heightDp)

                Row(
                    modifier = Modifier.fillMaxWidth().height((60*heightDp/914).dp)
                        .background(if (u2mode == 1) Color.Red; else Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width((30*widthDp/411).dp))
                    for (i in 0..cannons2 - 1) {

                        Box(
                            modifier = Modifier.height((30*heightDp/914).dp).width((30*widthDp/411).dp).clip(CircleShape)
                                .background(if (u2mode == 1) Color.White else Color.Black)
                        ) {}
                        Spacer(modifier = Modifier.width((10*widthDp/411).dp))
                    }
                    for (i in 0..(2 - cannons2)) {
                        Box(
                            modifier = Modifier.height((30*heightDp/914).dp).width((30*widthDp/411).dp).clip(CircleShape)
                                .background(Color.Black)
                        ) {}
                        Spacer(modifier = Modifier.width((10*widthDp/411).dp))
                    }
                    Spacer(modifier = Modifier.width((20*widthDp/411).dp))
                    if (umode2 == 0) {
                        Text("DEPLOY", color = Color.White, fontWeight = FontWeight.Bold)
                    } else if (u2mode == 1) {
                        Text("ATTACK", color = Color.White, fontWeight = FontWeight.Bold)
                    } else {
                        Text("DEFEND", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width((20*widthDp/411).dp))
                    IconButton(
                        onClick = {Soundplayer.click(Context1, sounds);
                            if (umode2 == 0 || u1turn == u2turn) {
                            }; else if (u2mode == 1) {
                                u2mode = 2; umode2 = 2;
                            } else {
                                u2mode = 1; umode2 = 1; }
                        }, modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp))
                            .background(if (u2mode == 1) Color(211, 110, 110, 255) else Color.Black)
                            .size((100*widthDp/411).dp, (30*heightDp/914).dp)
                    ) {
                        if (u2mode == 1) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {

                                Spacer(modifier = Modifier.fillMaxHeight().width((10*widthDp/411).dp))
                                Icon(
                                    painter = painterResource(R.drawable.compass),
                                    contentDescription = "Check",
                                    tint = Color.Black
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.electric),
                                    contentDescription = "Check",
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.fillMaxHeight().width((10*widthDp/411).dp))

                            }
                        }

                    }

                }

            }
        }
        if (refresh.value) {
            println(rounds)
            var score1 = 0
            var score2 = 0
            var ship11 = 0
            var ship12 = 0
            var ship13 = 0
            var ship14 = 0
            var ship21 = 0
            var ship22 = 0
            var ship23 = 0
            var ship24 = 0
            for (i in u1hitships) {
                if (i.id == 1) {
                    ship21 = 1
                }
                if (i.id == 2) {
                    ship22 = 1
                }
                if (i.id == 3) {
                    ship23 = 1
                }
                if (i.id == 4) {
                    ship24 = 1
                }
            }
            for (i in u2hitships) {
                if (i.id == 1) {
                    ship11 = 1
                }
                if (i.id == 2) {
                    ship12 = 1
                }
                if (i.id == 3) {
                    ship13 = 1
                }
                if (i.id == 4) {
                    ship14 = 1
                }
            }
            score1 = ship11 * 150 + ship12 * 450 + ship13 * 300 + ship14 * 450
            score2 = ship21 * 150 + ship22 * 450 + ship23 * 300 + ship24 * 450

            var winner = user1
            var loser = user2
            var wscore = score1
            var lscore = score2
            if (score2 > score1) {
                winner = user2
                loser = user1
                wscore = score2
                lscore = score1
                tourn2[rounds]=1
            }
            else{
                tourn1[rounds]=1
            }

            Column(
                modifier = Modifier.fillMaxSize().background(Color(0, 0, 0, 100))
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                    var pause by remember { mutableStateOf(false) }
                    LaunchedEffect(cannons2) {
                        if (cannons1 == 0 && cannons2 == 0 && rounds!=0){
                            pause=true
                            delay(2000)
                            screen_no = 21
                            pause=false

                        }

                    }
                    LaunchedEffect(cannons1) {
                        if (cannons1 == 0 && cannons2 == 0 && rounds!=0){
                            pause=true
                            delay(2000)
                            screen_no = 21
                            pause=false

                        }

                    }
                if (rounds==0) {
                    var context = LocalContext.current
                    var lifecycleOwner = LocalLifecycleOwner.current
                    var resultView = LocalView.current
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape((30*widthDp/411).dp)).size((400*widthDp/411).dp, (400*heightDp/914).dp)
                            .background(Color.Cyan)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Box(

                                modifier = Modifier.size((400*widthDp/411).dp, (300*heightDp/914).dp)
                                    .clip(RoundedCornerShape((30*widthDp/411).dp))
                                    .background(Color.White)
                            ) {
                                var score11= (1..7).sumOf { tourn1[it] ?: 0 }
                                var score22= (1..7).sumOf { tourn2[it] ?: 0 }

                                Column(


                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Spacer(modifier = Modifier.fillMaxWidth().height((20*heightDp/914).dp))

                                    if (score11 == score22) {
                                        Row(
                                            modifier.fillMaxWidth()
                                                .background(Color(227, 182, 57, 255)),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                "IT'S A TIE",
                                                fontSize = 30.sp,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        winner = ""
                                        Spacer(modifier = Modifier.fillMaxWidth().height((20*heightDp/914).dp))

                                        Text(
                                            "$user1          $score1".toUpperCase(),
                                            color = fccolor1,
                                            fontSize = 25.sp,
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.galgony,
                                                    FontWeight.Bold
                                                )
                                            )
                                        )


                                        Spacer(modifier = Modifier.fillMaxWidth().height((30*heightDp/914).dp))
                                        Text(
                                            "$user2          $score2".toUpperCase(),
                                            color = fccolor1,
                                            fontSize = 25.sp,
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.galgony,
                                                    FontWeight.Normal
                                                )
                                            )
                                        )
                                        Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))

                                    } else {
                                        if (score22 > score11) {
                                            winner = user2
                                            loser = user1
                                            wscore = score2
                                            lscore = score1
                                        }
                                        else{
                                            winner=user1
                                        }
                                        Row(
                                            modifier.fillMaxWidth()
                                                .background(Color(227, 182, 57, 255)),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                "THE WINNER IS ${winner.toUpperCase()}",
                                                fontSize = 30.sp,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                                            )
                                        }
                                        Spacer(modifier = Modifier.fillMaxWidth().height((20*heightDp/914).dp))

                                        Text(
                                            "$user1          $score11".toUpperCase(),
                                            color = fccolor1,
                                            fontSize = 25.sp,
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.galgony,
                                                    FontWeight.Bold
                                                )
                                            )
                                        )


                                        Spacer(modifier = Modifier.fillMaxWidth().height((30*heightDp/914).dp))
                                        Text(
                                            "$user2          $score22".toUpperCase(),
                                            color = fccolor1,
                                            fontSize = 25.sp,
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.galgony,
                                                    FontWeight.Normal
                                                )
                                            )
                                        )
                                        Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.fillMaxWidth().height((20*heightDp/914).dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                IconButton(
                                    onClick = { Soundplayer.click(Context1, sounds);

                                        val bitmap = resultView.drawToBitmap()
                                        saveBitmapToStorage(context, bitmap)
                                        for (i in tourn1.values){
                                            if (i==1){
                                                score_update(Context1, user1)
                                            }

                                        }
                                        for (i in tourn2.values){
                                            if (i==1){
                                                score_update(Context1, user2)
                                            }

                                        };screen_no = 0; },
                                    modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp))
                                        .background(Color.White)
                                        .size((150*widthDp/411).dp, (60*heightDp/914).dp)
                                ) {
                                    Text(
                                        "HOME",
                                        textAlign = TextAlign.Center,
                                        color = Color.Black, fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                        }
                        context = LocalContext.current
                        lifecycleOwner = LocalLifecycleOwner.current
                        resultView = LocalView.current
                    }


                }
                 else   if (pause){
                        Box(

                            modifier = Modifier.size((400*widthDp/411).dp, (300*heightDp/914).dp)
                                .clip(RoundedCornerShape((30*widthDp/411).dp))
                                .background(Color.White)
                        ) {
                            Column(


                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Spacer(modifier = Modifier.fillMaxWidth().height((20*heightDp/914).dp))
                                if (score2 == score1) {
                                    Row(
                                        modifier.fillMaxWidth()
                                            .background(Color(227, 182, 57, 255)),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            "IT'S A TIE",
                                            fontSize = 30.sp,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    winner = ""
                                    Spacer(modifier = Modifier.fillMaxWidth().height((20*heightDp/914).dp))

                                    Text(
                                        "$user1          $score1".toUpperCase(),
                                        color = fccolor1,
                                        fontSize = 25.sp,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.galgony,
                                                FontWeight.Bold
                                            )
                                        )
                                    )


                                    Spacer(modifier = Modifier.fillMaxWidth().height((30*heightDp/914).dp))
                                    Text(
                                        "$user2          $score2".toUpperCase(),
                                        color = fccolor1,
                                        fontSize = 25.sp,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.galgony,
                                                FontWeight.Normal
                                            )
                                        )
                                    )
                                    Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))

                                } else {
                                    if (score2 > score1) {
                                        winner = user2
                                        loser = user1
                                        wscore = score2
                                        lscore = score1
                                    }
                                    Row(
                                        modifier.fillMaxWidth()
                                            .background(Color(227, 182, 57, 255)),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            "THE WINNER IS ${winner.toUpperCase()}",
                                            fontSize = 30.sp,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold , textAlign = TextAlign.Center
                                        )
                                    }
                                    Spacer(modifier = Modifier.fillMaxWidth().height((20*heightDp/914).dp))

                                    Text(
                                        "$user1          $score1".toUpperCase(),
                                        color = fccolor1,
                                        fontSize = 25.sp,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.galgony,
                                                FontWeight.Bold
                                            )
                                        )
                                    )


                                    Spacer(modifier = Modifier.fillMaxWidth().height((30*heightDp/914).dp))
                                    Text(
                                        "$user2          $score2".toUpperCase(),
                                        color = fccolor1,
                                        fontSize = 25.sp,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.galgony,
                                                FontWeight.Normal
                                            )
                                        )
                                    )
                                    Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))
                                }
                            }
                        }



                }

            }

        }

    }
    if (screen_no==12){
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                Spacer(modifier = Modifier.height((15*heightDp/914).dp))
                Row(
                    modifier = Modifier.fillMaxWidth().height((60*heightDp/914).dp)
                        .background(if (u1mode == 1) Color.Red; else Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Spacer(modifier = Modifier.width((30*widthDp/411).dp))
                    if (cannons1 == 0) {
                        umode1 = 2
                        u1mode = 2
                        umode2 = 1
                        u2mode = 1
                    }
                    if (cannons2 == 0) {
                        umode1 = 1
                        u1mode = 1
                        umode2 = 2
                        u2mode = 2
                    }

                    IconButton(
                        onClick = {Soundplayer.click(Context1, sounds);
                            if (umode1 == 0 || u1turn != u2turn) {
                            }; else if (u1mode == 1) {
                                u1mode = 2; umode1 = 2;
                            } else {
                                u1mode = 1; umode1 = 1
                            }
                        }, modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp))
                            .background(
                                if (u1mode == 1 && umode1 != 0) Color(
                                    211,
                                    110,
                                    110,
                                    255
                                ) else Color.DarkGray
                            )
                            .size((100*widthDp/411).dp, (30*heightDp/914).dp)
                    ) {
                        if (u1mode == 1) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.compass),
                                    contentDescription = "Check",
                                    tint = Color.Black
                                )
                                Spacer(modifier = Modifier.fillMaxHeight().width((10*widthDp/411).dp))

                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {


                                Icon(
                                    painter = painterResource(R.drawable.electric),
                                    contentDescription = "Check",
                                    tint = Color.White
                                )
                            }
                        }

                    }
                    Spacer(modifier = Modifier.width((20*widthDp/411).dp))
                    if (umode1 == 0) {
                        Text("⅄OꓶԀƎꓷ", color = Color.White, fontWeight = FontWeight.Bold)
                    } else if (u1mode == 1) {
                        Text("ꓘƆⱯꓕꓕⱯ", color = Color.White, fontWeight = FontWeight.Bold)
                    } else {
                        Text("ꓷNƎℲƎꓷ", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width((30*widthDp/411).dp))
                    for (i in 0..(2 - cannons1)) {
                        Box(
                            modifier = Modifier.height((30*heightDp/914).dp).width((30*widthDp/411).dp).clip(CircleShape)
                                .background(Color.Black)
                        ) {}
                        Spacer(modifier = Modifier.width((10*widthDp/411).dp))
                    }
                    for (i in 0..cannons1 - 1) {

                        Box(
                            modifier = Modifier.height((30*heightDp/914).dp).width((30*widthDp/411).dp).clip(CircleShape)
                                .background(if (u1mode == 1 && umode1 != 0) Color.White else Color.Black)
                        ) {}
                        Spacer(modifier = Modifier.width((10*widthDp/411).dp))
                    }


                }

                grid(grid1, grid2, refresh,sounds, widthDp, heightDp)

                Row(
                    modifier = Modifier.fillMaxWidth().height((60*heightDp/914).dp)
                        .background(if (u2mode == 1) Color.Red; else Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width((30*widthDp/411).dp))
                    for (i in 0..cannons2 - 1) {

                        Box(
                            modifier = Modifier.height((30*heightDp/914).dp).width((30*widthDp/411).dp).clip(CircleShape)
                                .background(if (u2mode == 1) Color.White else Color.Black)
                        ) {}
                        Spacer(modifier = Modifier.width((10*widthDp/411).dp))
                    }
                    for (i in 0..(2 - cannons2)) {
                        Box(
                            modifier = Modifier.height((30*heightDp/914).dp).width((30*widthDp/411).dp).clip(CircleShape)
                                .background(Color.Black)
                        ) {}
                        Spacer(modifier = Modifier.width((10*widthDp/411).dp))
                    }
                    Spacer(modifier = Modifier.width((20*widthDp/411).dp))
                    if (umode2 == 0) {
                        Text("DEPLOY", color = Color.White, fontWeight = FontWeight.Bold)
                    } else if (u2mode == 1) {
                        Text("ATTACK", color = Color.White, fontWeight = FontWeight.Bold)
                    } else {
                        Text("DEFEND", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width((20*widthDp/411).dp))
                    IconButton(
                        onClick = {Soundplayer.click(Context1, sounds);
                            if (umode2 == 0 || u1turn == u2turn) {
                            }; else if (u2mode == 1) {
                                u2mode = 2; umode2 = 2;
                            } else {
                                u2mode = 1; umode2 = 1; }
                        }, modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp))
                            .background(if (u2mode == 1) Color(211, 110, 110, 255) else Color.Black)
                            .size((100*widthDp/411).dp, (30*heightDp/914).dp)
                    ) {
                        if (u2mode == 1) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {

                                Spacer(modifier = Modifier.fillMaxHeight().width((10*widthDp/411).dp))
                                Icon(
                                    painter = painterResource(R.drawable.compass),
                                    contentDescription = "Check",
                                    tint = Color.Black
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.electric),
                                    contentDescription = "Check",
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.fillMaxHeight().width((10*widthDp/411).dp))

                            }
                        }

                    }

                }

            }
        }
        if (refresh.value) {
            var score1 = 0
            var score2 = 0
            var ship11 = 0
            var ship12 = 0
            var ship13 = 0
            var ship14 = 0
            var ship21 = 0
            var ship22 = 0
            var ship23 = 0
            var ship24 = 0
            for (i in u1hitships) {
                if (i.id == 1) {
                    ship21 = 1
                }
                if (i.id == 2) {
                    ship22 = 1
                }
                if (i.id == 3) {
                    ship23 = 1
                }
                if (i.id == 4) {
                    ship24 = 1
                }
            }
            for (i in u2hitships) {
                if (i.id == 1) {
                    ship11 = 1
                }
                if (i.id == 2) {
                    ship12 = 1
                }
                if (i.id == 3) {
                    ship13 = 1
                }
                if (i.id == 4) {
                    ship14 = 1
                }
            }
            score1 = ship11 * 150 + ship12 * 450 + ship13 * 300 + ship14 * 450
            score2 = ship21 * 150 + ship22 * 450 + ship23 * 300 + ship24 * 450

            var winner = user1
            var loser = user2
            var wscore = score1
            var lscore = score2

            Column(
                modifier = Modifier.fillMaxSize().background(Color(0, 0, 0, 100))
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier=Modifier.clip(RoundedCornerShape((30*widthDp/411).dp)).size((400*widthDp/411).dp, (400*heightDp/914).dp).background(Color.Cyan)) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(

                            modifier = Modifier.size((400*widthDp/411).dp, (300*heightDp/914).dp).clip(RoundedCornerShape((30*widthDp/411).dp))
                                .background(Color.White)
                        ) {
                            Column(


                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Spacer(modifier = Modifier.fillMaxWidth().height((20*heightDp/914).dp))
                                if (score2 == score1) {
                                    Row(modifier.fillMaxWidth().background(Color(227,182,57,255)), horizontalArrangement = Arrangement.Center) {
                                        Text(
                                            "IT'S A TIE",
                                            fontSize = 30.sp,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    winner=""
                                    Spacer(modifier = Modifier.fillMaxWidth().height((20*heightDp/914).dp))

                                    Text(
                                        "$user1          $score1".toUpperCase(),
                                        color = fccolor1,
                                        fontSize = 25.sp,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.galgony,
                                                FontWeight.Bold
                                            )
                                        )
                                    )


                                    Spacer(modifier = Modifier.fillMaxWidth().height((30*heightDp/914).dp))
                                    Text(
                                        if (np==1) "BOT          $score2"; else "$user2          $score2".toUpperCase(),
                                        color = fccolor1,
                                        fontSize = 25.sp,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.galgony,
                                                FontWeight.Normal
                                            )
                                        )
                                    )
                                    Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))

                                } else {
                                    if (score2 > score1) {
                                        winner = user2
                                        loser = user1
                                        wscore = score2
                                        lscore = score1
                                    }
                                    if (winner==""){
                                        winner="BOT"
                                    }
                                    Row(modifier.fillMaxWidth().background(Color(227,182,57,255)), horizontalArrangement = Arrangement.Center) {
                                        Text(
                                            "THE WINNER IS ${winner.toUpperCase()}",
                                            fontSize = 30.sp,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                                        )
                                    }
                                    Spacer(modifier = Modifier.fillMaxWidth().height((20*heightDp/914).dp))

                                    Text(
                                        "$user1          $score1".toUpperCase(),
                                        color = fccolor1,
                                        fontSize = 25.sp,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.galgony,
                                                FontWeight.Bold
                                            )
                                        )
                                    )


                                    Spacer(modifier = Modifier.fillMaxWidth().height((30*heightDp/914).dp))
                                    Text(
                                        if (np==1) "BOT          $score2"; else "$user2          $score2".toUpperCase().toUpperCase(),
                                        color = fccolor1,
                                        fontSize = 25.sp,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.galgony,
                                                FontWeight.Normal
                                            )
                                        )
                                    )
                                    Spacer(modifier = Modifier.fillMaxWidth().height((50*heightDp/914).dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.fillMaxWidth().height((20*heightDp/914).dp))
                        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){

                            IconButton(
                                onClick = {Soundplayer.click(Context1, sounds);if (winner!="" && np!=1) score_update(Context1, winner); if (np==1 && score1>score2) bot_update(Context1, user1);screen_no = 0; },
                                modifier = Modifier.clip(RoundedCornerShape((50*widthDp/411).dp))
                                    .background(Color.White)
                                    .size((150*widthDp/411).dp, (60*heightDp/914).dp)
                            ) {
                                Text(
                                    "HOME",
                                    textAlign = TextAlign.Center,
                                    color = Color.Black, fontWeight = FontWeight.Bold
                                )
                            }
                        }

                    }
                }
            }
            val context = LocalContext.current
            CaptureScreenshotOnGameEnd( captureTrigger = true) { bitmap ->
                saveBitmapToStorage(context, bitmap) // write it to storage
            }


        }
    }
    if (screen_no==20){

        umode1=0
        umode2=0
        refresh.value=false
        cannons1=3;
        cannons2=3;
        u1turn=1;
        u2turn=1;
        u1ships= mutableMapOf<Int, Shipposition>()
        u1shipstrial= mutableMapOf<Int,Shipposition>()
        u2ships= mutableMapOf<Int, Shipposition>()
        u2shipstrial= mutableMapOf<Int,Shipposition>()
        u1hitships= mutableListOf<Shipposition>()
        u2hitships= mutableListOf<Shipposition>()
        u1attackedtiles= mutableListOf<AttackedTile>()
        u2attackedtiles= mutableListOf<AttackedTile>()
        u1mode=1;
        u2mode=1;
        screen_no=10
    }
    if (screen_no==21){
        rounds-=1
        umode1=0
        umode2=0
        refresh.value=false
        if (rounds!=0) {
            cannons1 = 3;
            cannons2 = 3;
        }
        u1turn=1;
        u2turn=1;
        u1ships= mutableMapOf<Int, Shipposition>()
        u1shipstrial= mutableMapOf<Int,Shipposition>()
        u2ships= mutableMapOf<Int, Shipposition>()
        u2shipstrial= mutableMapOf<Int,Shipposition>()
        u1hitships= mutableListOf<Shipposition>()
        u2hitships= mutableListOf<Shipposition>()
        u1attackedtiles= mutableListOf<AttackedTile>()
        u2attackedtiles= mutableListOf<AttackedTile>()
        u1mode=1;
        u2mode=1;
        grid1= generateGrid(rows,cols,enable_tiles);
        grid2= generateGrid(rows, cols,enable_tiles)

        screen_no=11

    }
    if (screen_no==30){
        Scaffold (topBar = {
            Column(modifier = Modifier.background(contrast_color)) {
                TopAppBar(title = {
                    IconButton(onClick = { screen_no = last_open.last(); last_open.removeLast() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = fore_color
                        )
                    }
                },  colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = contrast_color))
            }
        }, modifier = Modifier.fillMaxSize()) { innerPadding ->
            val context = LocalContext.current
            val imageFiles = remember { loadMatchHistory(context) }

            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(imageFiles) { file ->
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Match Screenshot",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((200*heightDp/914).dp)
                            .padding((8*widthDp/411).dp, (8*heightDp/914).dp),
                        contentScale = ContentScale.Crop

                    )
                }
            }
        }
    }

}


fun loadMatchHistory(context: Context): List<File> {
    val dir = File(context.filesDir, "match_history")
    return dir.listFiles()?.sortedByDescending { it.lastModified() }?.toList() ?: emptyList()
}

fun saveBitmapToStorage(context: Context, bitmap: Bitmap) {
    val filename = "match_${System.currentTimeMillis()}.png"
    val dir = File(context.filesDir, "match_history")
    if (!dir.exists()) dir.mkdirs()
    val file = File(dir, filename)

    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }
}



var counter=1
@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun grid(grid1:List<Tile>, grid2:List<Tile>, refresh: MutableState<Boolean>, sounds: Boolean, widthDp: Float, heightDp: Float) {
    val tiles1 = remember { grid1 }
    val tiles2 = remember { grid2 }
    val tileSizex = if (cols == 4)  with(LocalDensity.current) { (68.5713*widthDp/411).dp.toPx() }; else if (cols == 5) with(LocalDensity.current) { (54.857*widthDp/411).dp.toPx() }; else with(LocalDensity.current) { (45.7142*widthDp/411).dp.toPx() }
    val tileSizey = if (rows == 4) with(LocalDensity.current) { (68.5713*heightDp/914).dp.toPx() }; else if (rows == 5) with(LocalDensity.current) { (54.857*heightDp/914).dp.toPx() }; else with(LocalDensity.current) { (45.7142*heightDp/914).dp.toPx() }
    val dpValue = with(LocalDensity.current) { 180f.toDp() }

    val gap = 4f
    var ship1drag by remember { mutableStateOf(false) }
    var ship2drag by remember { mutableStateOf(false) }
    var ship3drag by remember { mutableStateOf(false) }
    var ship4drag by remember { mutableStateOf(false) }
    var ship1drag1 by remember { mutableStateOf(false) }
    var ship2drag1 by remember { mutableStateOf(false) }
    var ship3drag1 by remember { mutableStateOf(false) }
    var ship4drag1 by remember { mutableStateOf(false) }
    var selectedtile by remember { mutableStateOf(selectedTile(Offset(-1f,-1f),-1, -1, -1, -1)) }

    var tile1: MutableList<Snapping> = mutableListOf<Snapping>()
    var tile2= mutableListOf<Snapping>()
    var opacityu1= mutableListOf(1f,1f,1f,1f);
    var opacityu2= mutableListOf(1f,1f,1f,1f);
    var firecannon by remember { mutableStateOf(false) }
    var firecannon1 by remember { mutableStateOf(false) }
    var autou1 by remember { mutableStateOf(false) }
    var autou2 by remember { mutableStateOf(false) }
    
    var showDialog by remember { mutableStateOf(0) }
    if (showDialog==1){

        MyDialogDemo("Invalid Positioning")

    }
    if (showDialog==2){
        MyDialogDemo("Overlap between Ships")

    }
    if (showDialog==3){
        MyDialogDemo("Ship on Attacked tile")

    }
    LaunchedEffect(showDialog) {
        if (showDialog!=0) {
            delay(2000)
            showDialog = 0
        }
        }
    LaunchedEffect (autou1){
        if (autou1){
            delay(500)
            ship1drag1=true
            ship2drag1=true
            ship3drag1=true
            ship4drag1=true
            delay(500)
            autou1=false
        }
    }
    LaunchedEffect (autou2){
        if (autou2){
            delay(500)
            ship1drag=true
            ship2drag=true
            ship3drag=true
            ship4drag=true
            delay(500)
            autou2=false
        }
    }
    if (u1turn == u2turn && umode1==2){

        ship1drag1=true
        ship2drag1=true
        ship3drag1=true
        ship4drag1=true
    }
    else if (u1turn != u2turn && umode2==2){
        ship1drag=true
        ship2drag=true
        ship3drag=true
        ship4drag=true
    }

    else if ((umode1==1 || umode2==1) && (umode1!=0 && umode2!=0)){
        ship1drag1=false
        ship2drag1=false
        ship3drag1=false
        ship4drag1=false
        ship1drag=false
        ship2drag=false
        ship3drag=false
        ship4drag=false
    }
    for (i in u2hitships){
        if (i.id==1){
            ship1drag=true
            opacityu2[0]=0.5f
        }
        if (i.id==2){
            ship2drag=true
            opacityu2[1]=0.5f
        }
        if (i.id==3){
            ship3drag=true
            opacityu2[2]=0.5f
        }
        if (i.id==4){
            ship4drag=true
            opacityu2[3]=0.5f
        }
    }
    for (i in u1hitships){
        if (i.id==1){
            ship1drag1=true
            opacityu1[0]=0.5f
        }
        if (i.id==2){
            ship2drag1=true
            opacityu1[1]=0.5f
        }
        if (i.id==3){
            ship3drag1=true
            opacityu1[2]=0.5f
        }
        if (i.id==4){
            ship4drag1=true
            opacityu1[3]=0.5f
        }
    }
    val cannonBallVisible = remember { mutableStateOf(false) }
    var visible1= remember { mutableStateOf(255) }
    val durationMillis = 1000
    val steps = 100
    val delayPerStep = durationMillis / steps
    val context1=LocalContext.current
    LaunchedEffect(firecannon) {
        if (firecannon) {
            if (sounds) {
                Soundplayer.fireball(context = context1)
            }
            cannonBallVisible.value=true
            repeat(steps) {
                visible1.value -=  (255 / steps).toInt()
                delay(delayPerStep.toLong())
            }
            visible1.value = 0
            delay(100)
            cannonBallVisible.value = false
            u2turn+=1
            ship1drag=!ship1drag
            selectedtile=selectedTile(Offset(-1f,-1f),-1, -1, -1, -1)
            cannons2 -= 1
            firecannon = false




        }
    }
    val cannonBallVisible1 = remember { mutableStateOf(false) }
    var visible2= remember { mutableStateOf(255) }

    val durationMillis1 = 1000
    val steps1 = 100
    val delayPerStep1 = durationMillis1 / steps1

    LaunchedEffect(firecannon1) {
        if (firecannon1) {
            if (sounds) {
                Soundplayer.fireball(context = context1)
            }
            cannonBallVisible1.value=true
            repeat(steps1) {
                visible2.value -=  (255 / steps1).toInt()
                delay(delayPerStep1.toLong())

            }
            visible2.value = 0
            delay(100)
            cannonBallVisible1.value = false
            u1turn+=1
            ship1drag1=!ship1drag1
            selectedtile=selectedTile(Offset(-1f,-1f),-1, -1, -1, -1)
            cannons1 -= 1
            firecannon1 = false





        }
    }
    LaunchedEffect(u1turn) {
        if (u1turn != u2turn && np == 1 && umode2!=0) {
            if (cannons2!=0){
            if (difficulty == 0) {

                var attackedtile = botchooseattack(difficulty, tiles1)
                selectedtile = selectedTile(
                    Offset(0f, 0f),
                    attackedtile.x,
                    attackedtile.y,
                    attackedtile.width,
                    attackedtile.height
                )
                u2attackedtiles.addLast(AttackedTile(attackedtile.x, attackedtile.y))
                firecannon = true
                for (i in u1ships.values) {
                    if (i.size == 3) {
                        if (i.rot == 0) {
                            if (attackedtile.y == i.y && abs(attackedtile.x - i.x) < 2) {
                                u1hitships.addLast(i)
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y))
                                u2attackedtiles.addLast(AttackedTile(i.x - 1, i.y))
                                u2attackedtiles.addLast(AttackedTile(i.x + 1, i.y))
                            }
                        } else if (i.rot == 1) {
                            if (attackedtile.x == i.x && abs(attackedtile.y - i.y) < 2) {
                                u1hitships.addLast(i)
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y))
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y + 1))
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y - 1))
                            }
                        }
                    } else if (i.size == 2) {
                        if (i.rot == 0) {
                            if (attackedtile.y == i.y && attackedtile.x - i.x < 2) {
                                u1hitships.addLast(i)
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y))
                                u2attackedtiles.addLast(AttackedTile(i.x + 1, i.y))
                            }
                        } else if (i.rot == 1) {
                            if (attackedtile.x == i.x && attackedtile.y - i.y < 2) {
                                u1hitships.addLast(i)
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y))
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y + 1))
                            }
                        }
                    } else if (i.size == 1) {

                        if (attackedtile.y == i.y && attackedtile.x == i.x) {
                            u1hitships.addLast(i)
                        }


                    }
                }

            }
            if (difficulty == 1) {

                var attackedtile = botchooseattack(difficulty, tiles1)
                selectedtile = selectedTile(
                    Offset(0f, 0f),
                    attackedtile.x,
                    attackedtile.y,
                    attackedtile.width,
                    attackedtile.height
                )
                u2attackedtiles.addLast(AttackedTile(attackedtile.x, attackedtile.y))
                firecannon = true
                for (i in u1ships.values) {
                    if (i.size == 3) {
                        if (i.rot == 0) {
                            if (attackedtile.y == i.y && abs(attackedtile.x - i.x) < 2) {
                                u1hitships.addLast(i)
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y))
                                u2attackedtiles.addLast(AttackedTile(i.x - 1, i.y))
                                u2attackedtiles.addLast(AttackedTile(i.x + 1, i.y))
                            }
                        } else if (i.rot == 1) {
                            if (attackedtile.x == i.x && abs(attackedtile.y - i.y) < 2) {
                                u1hitships.addLast(i)
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y))
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y + 1))
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y - 1))
                            }
                        }
                    } else if (i.size == 2) {
                        if (i.rot == 0) {
                            if (attackedtile.y == i.y && attackedtile.x - i.x < 2) {
                                u1hitships.addLast(i)
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y))
                                u2attackedtiles.addLast(AttackedTile(i.x + 1, i.y))
                            }
                        } else if (i.rot == 1) {
                            if (attackedtile.x == i.x && attackedtile.y - i.y < 2) {
                                u1hitships.addLast(i)
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y))
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y + 1))
                            }
                        }
                    } else if (i.size == 1) {

                        if (attackedtile.y == i.y && attackedtile.x == i.x) {
                            u1hitships.addLast(i)
                        }


                    }
                }

            }
            if (difficulty == 2) {

                var attackedtile = botchooseattack(difficulty, tiles1)
                selectedtile = selectedTile(
                    Offset(0f, 0f),
                    attackedtile.x,
                    attackedtile.y,
                    attackedtile.width,
                    attackedtile.height
                )
                u2attackedtiles.addLast(AttackedTile(attackedtile.x, attackedtile.y))
                firecannon = true
                for (i in u1ships.values) {
                    if (i.size == 3) {
                        if (i.rot == 0) {
                            if (attackedtile.y == i.y && abs(attackedtile.x - i.x) < 2) {
                                u1hitships.addLast(i)
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y))
                                u2attackedtiles.addLast(AttackedTile(i.x - 1, i.y))
                                u2attackedtiles.addLast(AttackedTile(i.x + 1, i.y))
                            }
                        } else if (i.rot == 1) {
                            if (attackedtile.x == i.x && abs(attackedtile.y - i.y) < 2) {
                                u1hitships.addLast(i)
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y))
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y + 1))
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y - 1))
                            }
                        }
                    } else if (i.size == 2) {
                        if (i.rot == 0) {
                            if (attackedtile.y == i.y && attackedtile.x - i.x < 2) {
                                u1hitships.addLast(i)
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y))
                                u2attackedtiles.addLast(AttackedTile(i.x + 1, i.y))
                            }
                        } else if (i.rot == 1) {
                            if (attackedtile.x == i.x && attackedtile.y - i.y < 2) {
                                u1hitships.addLast(i)
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y))
                                u2attackedtiles.addLast(AttackedTile(i.x, i.y + 1))
                            }
                        }
                    } else if (i.size == 1) {

                        if (attackedtile.y == i.y && attackedtile.x == i.x) {
                            u1hitships.addLast(i)
                        }


                    }
                }
            }

            }
            else{
                botplacedships(tile1, tileSizex, tileSizey, opacityu2)
                u2turn+=1

            }
        }
    }
    Box(
        modifier = Modifier.width((1400*widthDp/411).dp).height((380*heightDp/914).dp).background(Color(99, 205, 191, 255)),
        contentAlignment = Alignment.Center
    ) {


        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var boxPosition by remember { mutableStateOf(Offset.Zero) }
            var canvasPosition by remember { mutableStateOf(Offset.Zero) }

            Box(
                modifier = Modifier.fillMaxWidth().height((280*heightDp/914).dp)
                    .onGloballyPositioned { coordinates ->
                        val topLeft = coordinates.positionInRoot()
                        val size = coordinates.size.toSize() 
                        boxPosition = topLeft + Offset(size.width / 2, size.height / 2)
                    }.pointerInput(Unit) {
                        detectTapGestures { tapOffset ->
            
                            val clickedTile = tiles1.find { tile ->
                                val left = tile.x * tileSizex + gap+tileSizex
                                val top = tile.y * tileSizey + gap
                                val right = left + tile.width*tileSizex - 2 * gap+tileSizex
                                val bottom = top + tile.height*tileSizey - 2 * gap
                                tapOffset.x in left..right && tapOffset.y in top..bottom
                            }
                            if (clickedTile != null) {
                                if ((selectedtile.x!= clickedTile.x || selectedtile.y!=clickedTile.y) && u1turn!=u2turn && cannons2!=0){
                                    var c1=0
                                    for (i in u2attackedtiles) {
                                        if (i.x==selectedtile.x && i.y==selectedtile.y) c1=1

                                    }
                                    if (c1==0){
                                        selectedtile = selectedTile(
                                            tapOffset,
                                            clickedTile.x,
                                            clickedTile.y,
                                            clickedTile.width,
                                            clickedTile.height
                                        )
                                    }
                                }
                                else if (u1turn!=u2turn && cannons2!=0){

                                    firecannon=true
                                    u2attackedtiles.addLast(AttackedTile(clickedTile.x,clickedTile.y))
                                    for (i in u1ships.values){
                                        if (i.size==3){
                                            if (i.rot==0){
                                                if (clickedTile.y==i.y && abs(clickedTile.x-i.x)<2){
                                                    u1hitships.addLast(i)
                                                    u2attackedtiles.addLast(AttackedTile(i.x,i.y))
                                                    u2attackedtiles.addLast(AttackedTile(i.x-1,i.y))
                                                    u2attackedtiles.addLast(AttackedTile(i.x+1,i.y))
                                                }
                                            }
                                            else if (i.rot==1){
                                                if (clickedTile.x==i.x && abs(clickedTile.y-i.y)<2){
                                                    u1hitships.addLast(i)
                                                    u2attackedtiles.addLast(AttackedTile(i.x,i.y))
                                                    u2attackedtiles.addLast(AttackedTile(i.x,i.y+1))
                                                    u2attackedtiles.addLast(AttackedTile(i.x,i.y-1))
                                                }
                                            }
                                        }
                                        else if (i.size==2){
                                            if (i.rot==0){
                                                if (clickedTile.y==i.y && clickedTile.x-i.x<2){
                                                    u1hitships.addLast(i)
                                                    u2attackedtiles.addLast(AttackedTile(i.x,i.y))
                                                    u2attackedtiles.addLast(AttackedTile(i.x+1,i.y))
                                                }
                                            }
                                            else if (i.rot==1){
                                                if (clickedTile.x==i.x && clickedTile.y-i.y<2){
                                                    u1hitships.addLast(i)
                                                    u2attackedtiles.addLast(AttackedTile(i.x,i.y))
                                                    u2attackedtiles.addLast(AttackedTile(i.x,i.y+1))
                                                }
                                            }
                                        }
                                        else if (i.size==1){

                                            if (clickedTile.y==i.y && clickedTile.x==i.x){
                                                u1hitships.addLast(i)
                                            }


                                        }
                                    }




                                }

                            }
                        }}, contentAlignment = Alignment.Center
            ) {

                Canvas(
                    modifier = Modifier
                        .size(
                            (280*widthDp/411).dp, (280*heightDp/914).dp
                        )
                        .onGloballyPositioned { coordinates ->
                            canvasPosition = coordinates.positionInRoot()

                        }
                ) {

                    tiles1.forEach { tile ->
                        var attacked=0

                        for (i in u2attackedtiles){
                            if (i.x == tile.x && i.y == tile.y){
                                attacked =1
                            }
                        }
                        if (tile.x==selectedtile.x && tile.y==selectedtile.y){
                            attacked=2
                        }
                        val topLeft = Offset(
                            x = tile.x * tileSizex + gap,
                            y = tile.y * tileSizey + gap
                        )
                        val size = Size(
                            width = tile.width * tileSizex - gap * 2,
                            height = tile.height * tileSizey - gap * 2
                        )
                        if (attacked==0) {
                            drawRect(color = Color.Gray, topLeft = topLeft, size = size)
                        }
                        if (attacked==2) {
                            if (cannonBallVisible.value) {
                                drawRect(color = Color(255,0,0,visible1.value), topLeft = topLeft, size = size)
                            }
                            else {
                                drawRect(color = Color.DarkGray, topLeft = topLeft, size = size)
                            }
                        }





                        if (tile.width == tile.height) {
                            val tileCenterInCanvas =
                                topLeft + Offset(size.width / 2, size.height / 2)
                            val tileCenterInGlobal = canvasPosition + tileCenterInCanvas
                            val tileCenterRelativeToBox = tileCenterInGlobal - boxPosition
                            val snap1 = Snapping(
                                x = tileCenterRelativeToBox.x,
                                y = tileCenterRelativeToBox.y,
                                width = tile.width,
                                height = tile.height,
                                row = tile.y,
                                col = tile.x
                            )
                            tile2.addLast(snap1)
                        } else if (tile.width == 2) {
                            val tileCenterInCanvas1 =
                                topLeft + Offset(size.width / 4, size.height / 2)
                            val tileCenterInGlobal1 = canvasPosition + tileCenterInCanvas1
                            val tileCenterRelativeToBox1 = tileCenterInGlobal1 - boxPosition
                            val tileCenterInCanvas2 =
                                topLeft + Offset(3 * size.width / 4, size.height / 2)
                            val tileCenterInGlobal2 = canvasPosition + tileCenterInCanvas2
                            val tileCenterRelativeToBox2 = tileCenterInGlobal2 - boxPosition
                            val snap1 = Snapping(
                                x = tileCenterRelativeToBox1.x,
                                y = tileCenterRelativeToBox1.y,
                                width = tile.width,
                                height = tile.height,
                                row = tile.y,
                                col = tile.x
                            )
                            val snap2 = Snapping(
                                x = tileCenterRelativeToBox2.x,
                                y = tileCenterRelativeToBox2.y,
                                width = tile.width,
                                height = tile.height,
                                row = tile.y,
                                col = tile.x
                            )
                            tile2.addLast(snap1)
                            tile2.addLast(snap2)
                        } else if (tile.height == 2) {
                            val tileCenterInCanvas1 =
                                topLeft + Offset(size.width / 2, size.height / 4)
                            val tileCenterInGlobal1 = canvasPosition + tileCenterInCanvas1
                            val tileCenterRelativeToBox1 = tileCenterInGlobal1 - boxPosition
                            val tileCenterInCanvas2 =
                                topLeft + Offset(3 * size.width / 2, 3 * size.height / 4)
                            val tileCenterInGlobal2 = canvasPosition + tileCenterInCanvas2
                            val tileCenterRelativeToBox2 = tileCenterInGlobal2 - boxPosition
                            val snap1 = Snapping(
                                x = tileCenterRelativeToBox1.x,
                                y = tileCenterRelativeToBox1.y,
                                width = tile.width,
                                height = tile.height,
                                row = tile.y,
                                col = tile.x
                            )
                            val snap2 = Snapping(
                                x = tileCenterRelativeToBox2.x,
                                y = tileCenterRelativeToBox2.y,
                                width = tile.width,
                                height = tile.height,
                                row = tile.y,
                                col = tile.x
                            )
                            tile2.addLast(snap1)
                            tile2.addLast(snap2)
                        }

                    }
                }
                if (ship1drag1) {

                    var offsetX by remember { mutableStateOf(0.01f) }
                    var offsetY by remember { mutableStateOf(0.01f) }
                    var rot1 by remember { mutableStateOf(0) }
                    if (umode1!=0 || autou1){
                        println(u1ships)
                        for (i in u1ships.values){
                            if (i.id==1){
                                offsetX=i.xcoord
                                offsetY=i.ycoord
                                rot1=i.rot
                            }
                        }
                    }
                    IconButton(
                        onClick = {Soundplayer.rotate(context1, sounds);if (opacityu1[0]!=0.5f) rot1 = 1 - rot1; counter = 1 },
                        modifier = Modifier
                            .size((130*widthDp/411).dp, (130*heightDp/914).dp)
                            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragEnd = {
                                        Soundplayer.pop(context1, sounds)
                                        var min = 10000.toDouble()

                                        var minx = 0.001f
                                        var miny = 0.001f
                                        var closest = Snapping(-1f, -1f, 0, 0, -1, -1)
                                        for (i in tile2) {

                                            if (abs(i.x - offsetX).pow(2) + abs(i.y - offsetY).pow(
                                                    2
                                                ) < min && (((0 < i.col && i.col < cols - 1 && rot1 === 0) || (i.width > 1 && rot1 === 0)) || ((0 < i.row && i.row < cols - 1 && rot1 === 1) || (i.height > 1 && rot1 === 1)))
                                            ) {
                                                closest = i
                                            }

                                        }
                                        if (closest.height == 2 && rot1 == 0) {
                                            rot1 = 1
                                        } else if (closest.width == 2 && rot1 == 1) {
                                            rot1 = 0
                                        }
                                        for (i in tile2) {

                                            if (rot1 == 0 && (i.col == closest.col - 1 || i.col == closest.col + 1) && i.row == closest.row) {
                                                if (i.height < 2) {
                                                    minx = closest.x
                                                    miny = closest.y
                                                } else {
                                                    minx = 0.01f
                                                    miny = 0.01f
                                                    rot1 = 0
                                                }
                                            } else if (rot1 == 1 && (i.row == closest.row - 1 || i.row == closest.row + 1) && i.col == closest.col) {
                                                if (i.width < 2) {
                                                    minx = closest.x
                                                    miny = closest.y
                                                } else {
                                                    minx = 0.01f
                                                    miny = 0.01f
                                                    rot1 = 0
                                                }
                                            }
                                        }

                                        if (opacityu1[0]!=0.5f) {
                                            offsetX = minx
                                            offsetY = miny
                                            u1shipstrial[1]=
                                                Shipposition(
                                                    1,
                                                    3,
                                                    closest.col,
                                                    closest.row,
                                                    rot1, offsetX, offsetY
                                                )

                                        }
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        if (abs(offsetX) < 300f && opacityu1[0]!=0.5f) {
                                            offsetX += dragAmount.x
                                        }
                                        if (abs(offsetY) < 300f && opacityu1[0]!=0.5f) {
                                            offsetY += dragAmount.y
                                        }
                                    }

                                )
                            }
                    )
                    {
                        Image(painter = painterResource(R.drawable.ship2),
                            contentDescription = "Ship",
                            modifier = Modifier.size((130*widthDp/411).dp, (60*heightDp/914).dp).graphicsLayer {
                                rotationZ = (90f * rot1); transformOrigin =
                                TransformOrigin(0.5f, 0.5f); clip = false; alpha=opacityu1[0]
                            })
                    }
                }
                if (ship2drag1) {
                    var offsetX by remember { mutableStateOf(0.01f) }
                    var offsetY by remember { mutableStateOf(0.01f) }
                    var rot by remember { mutableStateOf(0) }

                    if (umode1!=0 || autou1){
                        for (i in u1ships.values){
                            if (i.id==2){
                                offsetX=i.xcoord
                                offsetY=i.ycoord
                                rot=i.rot
                            }
                        }
                    }
                    IconButton(
                        onClick = {Soundplayer.rotate(context1, sounds);if (opacityu1[1]!=0.5f) rot = 1 - rot; counter = 1 },
                        modifier = Modifier
                            .size((60*widthDp/411).dp, (60*heightDp/914).dp)
                            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragEnd = {
                                        Soundplayer.pop(context1, sounds)
                                        var min = 10000.toDouble()

                                        var minx = 0.01f
                                        var miny = 0.01f
                                        var closest = Snapping(-1f, -1f, 0, 0, -1, -1)
                                        for (i in tile2) {

                                            if (abs(i.x - offsetX).pow(2) + abs(i.y - offsetY).pow(
                                                    2
                                                ) < min
                                            ) {
                                                closest = i
                                                minx = closest.x
                                                miny = closest.y
                                            }

                                        }
                                        if (closest.height == 2 && rot == 0) {
                                            rot = 1
                                        } else if (closest.width == 2 && rot == 1) {
                                            rot = 0
                                        }



                                        if (opacityu1[1]!=0.5f) {
                                            offsetX = minx
                                            offsetY = miny
                                            u1shipstrial[2]=
                                                Shipposition(
                                                    2,
                                                    1,
                                                    closest.col,
                                                    closest.row,
                                                    rot, offsetX, offsetY
                                                )

                                        }

                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        if (abs(offsetX) < 300f && opacityu1[1]!=0.5f) {
                                            offsetX += dragAmount.x
                                        }
                                        if (abs(offsetY) < 300f && opacityu1[1]!=0.5f) {
                                            offsetY += dragAmount.y
                                        }
                                    }

                                )
                            }
                    )
                    {
                        Image(painter = painterResource(R.drawable.ship3),
                            contentDescription = "Ship",
                            modifier = Modifier.size((60*widthDp/411).dp, (60*heightDp/914).dp).graphicsLayer {
                                rotationZ = (90f * rot); transformOrigin =
                                TransformOrigin(0.5f, 0.5f); clip = false; alpha=opacityu1[1]
                            })
                    }
                }
                if (ship3drag1) {
                    var offsetX by remember { mutableStateOf(0.01f) }
                    var offsetY by remember { mutableStateOf(0.01f) }
                    var rot2 by remember { mutableStateOf(0) }
                    if (umode1!=0  || autou1){
                        for (i in u1ships.values){
                            if (i.id==3){
                                offsetX=i.xcoord
                                offsetY=i.ycoord
                                rot2=i.rot
                            }
                        }
                    }
                    IconButton(
                        onClick = {Soundplayer.click(context1, sounds);if (opacityu1[2]!=0.5f) rot2 = 1 - rot2; counter = 1 },
                        modifier = Modifier
                            .size((100*widthDp/411).dp, (100*heightDp/914).dp)
                            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragEnd = {
                                        Soundplayer.pop(context1, sounds)
                                        val shipOffsetInBox = Offset(offsetX, offsetY)
                                        var min = 10000.toDouble()

                                        var minx = 0.01f
                                        var miny = 0.01f
                                        var closest = Snapping(-1f, -1f, 0, 0, -1, -1)
                                        for (i in tile2) {

                                            if ((abs(i.x - offsetX).pow(2) + abs(i.y - offsetY).pow(
                                                    2
                                                ) < min)
                                            ) {

                                                if (((-1 < i.col && i.col < cols - 1 && rot2 == 0) || (i.width == 2 && rot2 == 0)) || ((0 < i.row && i.row < cols && rot2 == 1) || (i.height > 1 && rot2 == 1))) {
                                                    closest = i

                                                }
                                            }

                                        }
                                        if (closest.height == 2 && rot2 == 0) {
                                            rot2 = 1
                                        } else if (closest.width == 2 && rot2 == 1) {
                                            rot2 = 0
                                        }
                                        for (i in tile2) {

                                            if (rot2 == 0 && ((i.col == closest.col + 1 && i.row == closest.row) || i.width == 2)) {
                                                if (i.height == 1) {
                                                    minx = closest.x
                                                    miny = closest.y
                                                } else {

                                                    minx = 0.01f
                                                    miny = 0.01f
                                                    rot2 = 0

                                                }
                                            } else if (rot2 == 1 && ((i.row == closest.row - 1 && i.col == closest.col) || i.height == 2)) {
                                                if (i.width == 1) {
                                                    minx = closest.x
                                                    miny = closest.y
                                                } else {

                                                    minx = 0.01f
                                                    miny = 0.01f
                                                    rot2 = 0
                                                }
                                            }

                                        }
                                        if (opacityu1[2]!=0.5f) {
                                            offsetX = minx
                                            offsetY = miny
                                            u1shipstrial[3]=
                                                Shipposition(
                                                    3,
                                                    2,
                                                    closest.col,
                                                    closest.row,
                                                    rot2, offsetX, offsetY
                                                )

                                        }
                                       
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        if (abs(offsetX) < 300f && opacityu1[2]!=0.5f) {
                                            offsetX += dragAmount.x
                                        }
                                        if (abs(offsetY) < 300f && opacityu1[2]!=0.5f) {
                                            offsetY += dragAmount.y
                                        }
                                    }

                                )
                            }
                    )
                    {
                        Image(painter = painterResource(R.drawable.ship1),
                            contentDescription = "Ship",
                            modifier = Modifier.size((100*widthDp/411).dp, (60*heightDp/914).dp).graphicsLayer {
                                rotationZ = (90f * rot2); transformOrigin =
                                TransformOrigin(0.5f, 0.5f); clip = false; alpha=opacityu1[2]
                            })
                    }
                }
                if (ship4drag1) {
                    var offsetX by remember { mutableStateOf(0.01f) }
                    var offsetY by remember { mutableStateOf(0.01f) }
                    var rot3 by remember { mutableStateOf(0) }
                    if (umode1!=0  || autou1){
                        for (i in u1ships.values){
                            if (i.id==4){
                                offsetX=i.xcoord
                                offsetY=i.ycoord
                                rot3=i.rot
                            }
                        }
                    }
                    IconButton(
                        onClick = {Soundplayer.rotate(context1, sounds);if (opacityu1[3]!=0.5f) rot3 = 1 - rot3; counter = 1 },
                        modifier = Modifier
                            .size((60*widthDp/411).dp, (60*heightDp/914).dp)
                            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragEnd = {
                                        Soundplayer.pop(context1, sounds)
                                        var min = 10000.toDouble()

                                        var minx = 0.01f
                                        var miny = 0.01f
                                        var closest = Snapping(-1f, -1f, 0, 0, -1, -1)
                                        for (i in tile2) {

                                            if (abs(i.x - offsetX).pow(2) + abs(i.y - offsetY).pow(
                                                    2
                                                ) < min
                                            ) {
                                                closest = i
                                                minx = closest.x
                                                miny = closest.y
                                            }

                                        }
                                        if (closest.height == 2 && rot3 == 0) {
                                            rot3 = 1
                                        } else if (closest.width == 2 && rot3 == 1) {
                                            rot3 = 0
                                        }



                                        if (opacityu1[3]!=0.5f) {
                                            offsetX = minx
                                            offsetY = miny
                                            u1shipstrial[4]=
                                                Shipposition(
                                                    4,
                                                    1,
                                                    closest.col,
                                                    closest.row,
                                                    rot3, offsetX, offsetY
                                                )

                                        }
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        if (abs(offsetX) < 300f && opacityu1[3]!=0.5f) {
                                            offsetX += dragAmount.x
                                        }
                                        if (abs(offsetY) < 300f && opacityu1[3]!=0.5f) {
                                            offsetY += dragAmount.y
                                        }
                                    }

                                )
                            }
                    )
                    {
                        Image(painter = painterResource(R.drawable.ship3),
                            contentDescription = "Ship",
                            modifier = Modifier.size((60*widthDp/411).dp, (60*heightDp/914).dp).graphicsLayer {
                                rotationZ = (90f * rot3); transformOrigin =
                                TransformOrigin(0.5f, 0.5f); clip = false; alpha=opacityu1[3]
                            })
                    }
                }
            }


            Row(
                modifier = Modifier.height((60*heightDp/914).dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                IconButton(
                    onClick = { if (!ship4drag1) ship4drag1 = true },
                    modifier = Modifier.height((50*heightDp/914).dp).width((50*widthDp/411).dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ship3),
                        contentDescription = "Big Ship",
                        modifier = Modifier.size((50*widthDp/411).dp, (50*heightDp/914).dp).graphicsLayer { scaleY = -1f }
                    )
                }
                IconButton(
                    onClick = { if (!ship3drag1) ship3drag1 = true },
                    modifier = Modifier.height((50*heightDp/914).dp).width((100*widthDp/411).dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ship1),
                        contentDescription = "Big Ship",
                        modifier = Modifier.size((100*widthDp/411).dp, (100*heightDp/914).dp).graphicsLayer { scaleY = -1f }
                    )
                }

                IconButton(
                    onClick = { if (!ship2drag1) ship2drag1 = true },
                    modifier = Modifier.height((50*heightDp/914).dp).width((50*widthDp/411).dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ship3),
                        contentDescription = "Big Ship",
                        modifier = Modifier.size((50*widthDp/411).dp, (50*heightDp/914).dp).graphicsLayer { scaleY = -1f }
                    )
                }

                IconButton(
                    onClick = { if (!ship1drag1) ship1drag1 = true },
                    modifier = Modifier.height((60*heightDp/914).dp).width((125*widthDp/411).dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ship2),
                        contentDescription = "Big Ship",
                        modifier = Modifier.width((150*widthDp/411).dp).graphicsLayer { scaleY = -1f }
                    )
                }

            }


        }

        if ((u1turn == u2turn && umode1%2!=0) || (u1turn!= u2turn && umode2%2==0)){
            Box(
                modifier = Modifier.width((1400*widthDp/411).dp).height((380*heightDp/914).dp).background(Color(0, 0, 0, 250)),
                contentAlignment = Alignment.Center
            ) {

                if (u1turn == u2turn && umode1%2!=0){
                    Image(painter = painterResource( R.drawable.cannon), contentDescription = "Cannon")
                }
                else{

                    var text = "FORTIFY"
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        if (umode1 == 0) {
                            text = "DEPLOY"
                        }
                        Box(
                            modifier = Modifier.background(Color.Gray).fillMaxWidth().height((30*heightDp/914).dp)
                        ) {

                        }
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape((25*widthDp/411).dp))
                                .background(Color.DarkGray).fillMaxWidth().height((130*heightDp/914).dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "$text",
                                    fontSize = 40.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    "Drag and drop ships to place them",
                                    fontSize = 15.sp,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                    if (ship1drag && ship2drag && ship3drag && ship4drag) {
                                        IconButton(
                                            onClick = {Soundplayer.click(context1, sounds);
                                                val x1start = u2shipstrial[1]?.xcoord ?: -1f
                                                val y1start = u2shipstrial[1]?.ycoord ?: -1f
                                                val x2start = u2shipstrial[2]?.xcoord ?: -1f
                                                val y2start = u2shipstrial[2]?.ycoord ?: -1f
                                                val x3start = u2shipstrial[3]?.xcoord ?: -1f
                                                val y3start = u2shipstrial[3]?.ycoord ?: -1f
                                                val x4start = u2shipstrial[4]?.xcoord ?: -1f
                                                val y4start = u2shipstrial[4]?.ycoord ?: -1f

                                                if (x1start == -1f || x2start == -1f || x3start == -1f || x4start == -1f || y1start == -1f || y2start == -1f || y3start == -1f || y4start == -1f || u2shipstrial[1]?.xcoord == 0.01f || u2shipstrial[2]?.xcoord == 0.01f || u2shipstrial[3]?.xcoord == 0.01f || u2shipstrial[4]?.xcoord == 0.01f || u2shipstrial[1]?.ycoord == 0.01f || u2shipstrial[2]?.ycoord == 0.01f || u2shipstrial[3]?.ycoord == 0.01f || u2shipstrial[4]?.ycoord == 0.01f) {
                                                    showDialog = 1
                                                } else if (u2shipstrial[1] == null || u2shipstrial[2] == null || u2shipstrial[3] == null || u2shipstrial[4] == null) {
                                                    showDialog = 1
                                                } else {
                                                    if (u2shipstrial[1]?.rot == 0) {
                                                        for (i in (x1start - 3 * tileSizex / 2).toInt()..(x1start + 3 * tileSizex / 2).toInt()) {
                                                            if (i < (x2start + tileSizex / 2) && i > (x2start - tileSizex / 2) && y1start == y2start) {
                                                                showDialog = 2
                                                            }
                                                            if ((u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizex) && i > (x3start - tileSizex) && y1start == y3start) || (u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && (u2shipstrial[3]!!.y - u2shipstrial[1]!!.y < 2))) {
                                                                showDialog = 2

                                                            }
                                                            if (i < (x4start + tileSizex / 2) && i > (x4start - tileSizex / 2) && y1start == y4start) {
                                                                showDialog = 2
                                                            }
                                                        }
                                                    } else if (u2shipstrial[1]?.rot == 1) {
                                                        for (i in (y1start - 3 * tileSizey / 2).toInt()..(y1start + 3 * tileSizey / 2).toInt()) {
                                                            if (i < (y2start + tileSizey / 2) && i > (y2start - tileSizey / 2) && x1start == x2start) {
                                                                showDialog = 2
                                                            }
                                                            if ((u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && x1start == x3start) || (u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizey) && i > (x3start - tileSizey) && (u2shipstrial[3]!!.x - u2shipstrial[1]!!.x < 2))) {
                                                                showDialog = 2

                                                            }
                                                            if (i < (y4start + tileSizey / 2) && i > (y4start - tileSizey / 2) && x1start == x4start) {
                                                                showDialog = 2
                                                            }
                                                        }
                                                    }
                                                    if (u2shipstrial[2]?.rot == 0) {
                                                        for (i in (x2start - tileSizex / 2).toInt()..(x2start + tileSizex / 2).toInt()) {
                                                            if ((u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizex) && i > (x3start - tileSizex) && y2start == y3start) || (u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && (u2shipstrial[3]!!.y - u2shipstrial[2]!!.y < 2))) {
                                                                showDialog = 2

                                                            }
                                                            if (i < (x4start + tileSizex / 2) && i > (x4start - tileSizex / 2) && y1start == y4start) {
                                                                showDialog = 2
                                                            }
                                                        }
                                                    } else if (u2shipstrial[2]?.rot == 1) {
                                                        for (i in (y2start - tileSizey / 2).toInt()..(y2start + tileSizey / 2).toInt()) {
                                                            if ((u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && x2start == x3start) || (u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizey) && i > (x3start - tileSizey) && (u2shipstrial[3]!!.x - u2shipstrial[2]!!.x < 2))) {
                                                                showDialog = 2

                                                            }
                                                            if (i < (y4start + tileSizey / 2) && i > (y4start - tileSizey / 2) && x2start == x4start) {
                                                                showDialog = 2
                                                            }
                                                        }
                                                    }

                                                    if (u2shipstrial[4]?.rot == 0) {
                                                        for (i in (x4start - tileSizex / 2).toInt()..(x4start + tileSizex / 2).toInt()) {
                                                            if ((u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizex) && i > (x3start - tileSizex) && y4start == y3start) || (u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && (u2shipstrial[3]!!.y - u2shipstrial[4]!!.y < 2))) {
                                                                showDialog = 2

                                                            }

                                                        }
                                                    } else if (u2shipstrial[4]?.rot == 1) {
                                                        for (i in (y4start - tileSizey / 2).toInt()..(y4start + tileSizey / 2).toInt()) {
                                                            if ((u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && x4start == x3start) || (u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizey) && i > (x3start - tileSizey) && (u2shipstrial[3]!!.x - u2shipstrial[4]!!.x < 2))) {
                                                                showDialog = 2

                                                            }
                                                        }
                                                    }
                                                    for (i in u1attackedtiles) {
                                                        if ((opacityu2[0] == 1f && i.x == u2shipstrial[1]!!.x && i.y == u2shipstrial[1]!!.y) || (opacityu2[1] == 1f && i.x == u2shipstrial[2]!!.x && i.y == u2shipstrial[2]!!.y) || (opacityu2[2] == 1f && i.x == u2shipstrial[3]!!.x && i.y == u2shipstrial[3]!!.y) || (opacityu2[3] == 1f && i.x == u2shipstrial[4]!!.x && i.y == u2shipstrial[4]!!.y) || (opacityu2[0] == 1f && (i.x == u2shipstrial[1]!!.x - 1 || i.x == u2shipstrial[1]!!.x + 1) && u2shipstrial[1]!!.rot == 0 && i.y == u2shipstrial[1]!!.y) || (opacityu2[2] == 1f && i.x == u2shipstrial[3]!!.x + 1 && u2shipstrial[3]!!.rot == 0 && i.y == u2shipstrial[1]!!.y) || (opacityu2[0] == 1f && (i.y == u2shipstrial[1]!!.y - 1 || i.y == u2shipstrial[1]!!.y + 1) && u2shipstrial[1]!!.rot == 1 && i.x == u2shipstrial[1]!!.x) || (opacityu2[2] == 1f && i.y == u2shipstrial[3]!!.y + 1 && u2shipstrial[3]!!.rot == 1 && i.x == u2shipstrial[1]!!.x)) {
                                                            showDialog = 3
                                                        }
                                                    }
                                                    if (showDialog != 0) {
                                                        Soundplayer.error(context1, sounds)
                                                        for (i in u2ships.values) {
                                                            u2shipstrial[i.id] = i
                                                        }
                                                    }
                                                    if (showDialog == 0) {
                                                        u2turn += 1; if (umode2 == 0) {
                                                            umode2 = 1
                                                        }; for (i in u2shipstrial.values) {
                                                            u2ships[i.id] = i
                                                        };ship1drag = false
                                                    }
                                                }
                                            },
                                            modifier = Modifier.background(Color.Cyan)
                                                .size((100*widthDp/411).dp, (30*heightDp/914).dp)
                                        ) {
                                            Text(
                                                "SAVE",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center
                                            )
                                        }

                                    }
                                    if (umode2==0) {
                                        Spacer(Modifier.width((20*widthDp/411).dp))
                                        IconButton(
                                            onClick = {Soundplayer.click(context1, sounds);
                                                autobot(
                                                    2,
                                                    tile1,
                                                    tileSizex,
                                                    tileSizey,
                                                    opacityu2
                                                ); autou2 = true;
                                            },
                                            modifier = Modifier.background(Color.Cyan)
                                                .size((100*widthDp/411).dp, (30*heightDp/914).dp)
                                        ) {
                                            Text(
                                                "AUTO",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }

                        }
                    }
                }

            }
        }
    }
        Box(
            modifier = Modifier.width((1400*widthDp/411).dp).height((380*heightDp/914).dp).background(Color(99, 205, 191, 255)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Row(
                    modifier = Modifier.height((65*heightDp/914).dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {

                    IconButton(
                        onClick = { counter = 1; if (!ship1drag) ship1drag = true },
                        modifier = Modifier.height((60*heightDp/914).dp).width((125*widthDp/411).dp),

                        ) {
                        Image(
                            painter = painterResource(R.drawable.ship2),
                            contentDescription = "Big Ship", modifier = Modifier.width((150*widthDp/411).dp)
                        )
                    }


                    IconButton(
                        onClick = { counter = 1; if (!ship2drag) ship2drag = true },
                        modifier = Modifier.height((50*heightDp/914).dp).width((50*widthDp/411).dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ship3),
                            contentDescription = "Big Ship", modifier = Modifier.size((50*widthDp/411).dp, (50*heightDp/914).dp)
                        )
                    }
                    IconButton(
                        onClick = { counter = 1; if (!ship3drag) ship3drag = true },
                        modifier = Modifier.height((50*heightDp/914).dp).width((100*widthDp/411).dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ship1),
                            contentDescription = "Big Ship", modifier = Modifier.size((100*widthDp/411).dp, (100*heightDp/914).dp)
                        )
                    }

                    IconButton(
                        onClick = { counter = 1; if (!ship4drag) ship4drag = true },
                        modifier = Modifier.height((50*heightDp/914).dp).width((50*widthDp/411).dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ship3),
                            contentDescription = "Big Ship", modifier = Modifier.size((50*widthDp/411).dp, (50*heightDp/914).dp)
                        )
                    }
                }
                var boxPosition by remember { mutableStateOf(Offset.Zero) }
                var canvasPosition by remember { mutableStateOf(Offset.Zero) }

                Box(
                    modifier = Modifier.fillMaxWidth().height((280*heightDp/914).dp)
                        .onGloballyPositioned { coordinates ->
                            val topLeft = coordinates.positionInRoot()
                            val size = coordinates.size.toSize() 
                            boxPosition = topLeft + Offset(size.width / 2, size.height / 2)
                        }.pointerInput(Unit) {
                            detectTapGestures { tapOffset ->
                                val clickedTile = tiles2.find { tile ->
                                    val left = tile.x * tileSizex + gap + tileSizex
                                    val top = tile.y * tileSizey + gap
                                    val right = left + tile.width * tileSizex - 2 * gap + tileSizex
                                    val bottom = top + tile.height * tileSizey - 2 * gap
                                    tapOffset.x in left..right && tapOffset.y in top..bottom
                                }
                                if (clickedTile != null) {
                                    if ((selectedtile.x!= clickedTile.x || selectedtile.y!=clickedTile.y) && u1turn==u2turn && cannons1!=0){
                                        var c1=0
                                        for (i in u1attackedtiles) {
                                            if (i.x==selectedtile.x && i.y==selectedtile.y) c1=1

                                        }
                                        if (c1==0){
                                            selectedtile = selectedTile(
                                                tapOffset,
                                                clickedTile.x,
                                                clickedTile.y,
                                                clickedTile.width,
                                                clickedTile.height
                                            )
                                        }

                                    }

                                    else if (u1turn == u2turn && cannons1 != 0) {

                                        firecannon1=true
                                        u1attackedtiles.addLast(
                                            AttackedTile(
                                                clickedTile.x,
                                                clickedTile.y
                                            )
                                        )
                                        for (i in u2ships.values) {
                                            if (i.size == 3) {
                                                if (i.rot == 0) {
                                                    if (clickedTile.y == i.y && abs(clickedTile.x - i.x) < 2) {
                                                        u2hitships.addLast(i)
                                                        u1attackedtiles.addLast(
                                                            AttackedTile(
                                                                i.x,
                                                                i.y
                                                            )
                                                        )
                                                        u1attackedtiles.addLast(
                                                            AttackedTile(
                                                                i.x - 1,
                                                                i.y
                                                            )
                                                        )
                                                        u1attackedtiles.addLast(
                                                            AttackedTile(
                                                                i.x + 1,
                                                                i.y
                                                            )
                                                        )
                                                    }
                                                } else if (i.rot == 1) {
                                                    if (clickedTile.x == i.x && abs(clickedTile.y - i.y) < 2) {
                                                        u2hitships.addLast(i)
                                                        u1attackedtiles.addLast(
                                                            AttackedTile(
                                                                i.x,
                                                                i.y
                                                            )
                                                        )
                                                        u1attackedtiles.addLast(
                                                            AttackedTile(
                                                                i.x,
                                                                i.y + 1
                                                            )
                                                        )
                                                        u1attackedtiles.addLast(
                                                            AttackedTile(
                                                                i.x,
                                                                i.y - 1
                                                            )
                                                        )
                                                    }
                                                }
                                            } else if (i.size == 2) {
                                                if (i.rot == 0) {
                                                    if (clickedTile.y == i.y && clickedTile.x - i.x < 2) {
                                                        u2hitships.addLast(i)
                                                        u1attackedtiles.addLast(
                                                            AttackedTile(
                                                                i.x,
                                                                i.y
                                                            )
                                                        )
                                                        u1attackedtiles.addLast(
                                                            AttackedTile(
                                                                i.x + 1,
                                                                i.y
                                                            )
                                                        )
                                                    }
                                                } else if (i.rot == 1) {
                                                    if (clickedTile.x == i.x && clickedTile.y - i.y < 2) {
                                                        u2hitships.addLast(i)
                                                        u1attackedtiles.addLast(
                                                            AttackedTile(
                                                                i.x,
                                                                i.y
                                                            )
                                                        )
                                                        u1attackedtiles.addLast(
                                                            AttackedTile(
                                                                i.x,
                                                                i.y + 1
                                                            )
                                                        )
                                                    }
                                                }
                                            } else if (i.size == 1) {

                                                if (clickedTile.y == i.y && clickedTile.x == i.x) {
                                                    u2hitships.addLast(i)
                                                }


                                            }
                                        }


                                    }

                                }
                            }
                        }, contentAlignment = Alignment.Center
                ) {

                    Canvas(
                        modifier = Modifier
                            .size(
                                (280*widthDp/411).dp, (280*heightDp/914).dp
                            )
                            .onGloballyPositioned { coordinates ->
                                canvasPosition = coordinates.positionInRoot()

                            }
                    ) {
                        tiles2.forEach { tile ->
                            var attacked = 0

                            for (i in u1attackedtiles) {
                                if (i.x == tile.x && i.y == tile.y) {
                                    attacked = 1
                                }
                            }
                            if (tile.x==selectedtile.x && tile.y==selectedtile.y){
                                attacked=2
                            }
                            val topLeft = Offset(
                                x = tile.x * tileSizex + gap,
                                y = tile.y * tileSizey + gap
                            )
                            val size = Size(
                                width = tile.width * tileSizex - gap * 2,
                                height = tile.height * tileSizey - gap * 2
                            )
                            if (attacked == 0) {
                                drawRect(color = Color.Gray, topLeft = topLeft, size = size)
                            }
                            if (attacked==2) {

                                if (cannonBallVisible1.value){
                                    drawRect(color = Color(255,0,0,visible2.value), topLeft = topLeft, size = size)
                                }
                                else {
                                    drawRect(color = Color.DarkGray, topLeft = topLeft, size = size)
                                }
                            }



                            if (tile.width == tile.height) {
                                val tileCenterInCanvas =
                                    topLeft + Offset(size.width / 2, size.height / 2)
                                val tileCenterInGlobal = canvasPosition + tileCenterInCanvas
                                val tileCenterRelativeToBox = tileCenterInGlobal - boxPosition
                                val snap1 = Snapping(
                                    x = tileCenterRelativeToBox.x,
                                    y = tileCenterRelativeToBox.y,
                                    width = tile.width,
                                    height = tile.height,
                                    row = tile.y,
                                    col = tile.x
                                )
                                tile1.addLast(snap1)
                            } else if (tile.width == 2) {
                                val tileCenterInCanvas1 =
                                    topLeft + Offset(size.width / 4, size.height / 2)
                                val tileCenterInGlobal1 = canvasPosition + tileCenterInCanvas1
                                val tileCenterRelativeToBox1 = tileCenterInGlobal1 - boxPosition
                                val tileCenterInCanvas2 =
                                    topLeft + Offset(3 * size.width / 4, size.height / 2)
                                val tileCenterInGlobal2 = canvasPosition + tileCenterInCanvas2
                                val tileCenterRelativeToBox2 = tileCenterInGlobal2 - boxPosition
                                val snap1 = Snapping(
                                    x = tileCenterRelativeToBox1.x,
                                    y = tileCenterRelativeToBox1.y,
                                    width = tile.width,
                                    height = tile.height,
                                    row = tile.y,
                                    col = tile.x
                                )
                                val snap2 = Snapping(
                                    x = tileCenterRelativeToBox2.x,
                                    y = tileCenterRelativeToBox2.y,
                                    width = tile.width,
                                    height = tile.height,
                                    row = tile.y,
                                    col = tile.x
                                )
                                tile1.addLast(snap1)
                                tile1.addLast(snap2)
                            } else if (tile.height == 2) {
                                val tileCenterInCanvas1 =
                                    topLeft + Offset(size.width / 2, size.height / 4)
                                val tileCenterInGlobal1 = canvasPosition + tileCenterInCanvas1
                                val tileCenterRelativeToBox1 = tileCenterInGlobal1 - boxPosition
                                val tileCenterInCanvas2 =
                                    topLeft + Offset(3 * size.width / 2, 3 * size.height / 4)
                                val tileCenterInGlobal2 = canvasPosition + tileCenterInCanvas2
                                val tileCenterRelativeToBox2 = tileCenterInGlobal2 - boxPosition
                                val snap1 = Snapping(
                                    x = tileCenterRelativeToBox1.x,
                                    y = tileCenterRelativeToBox1.y,
                                    width = tile.width,
                                    height = tile.height,
                                    row = tile.y,
                                    col = tile.x
                                )
                                val snap2 = Snapping(
                                    x = tileCenterRelativeToBox2.x,
                                    y = tileCenterRelativeToBox2.y,
                                    width = tile.width,
                                    height = tile.height,
                                    row = tile.y,
                                    col = tile.x
                                )
                                tile1.addLast(snap1)
                                tile1.addLast(snap2)
                            }

                        }
                    }

                    if (ship1drag) {

                        var offsetX by remember { mutableStateOf(0.01f) }
                        var offsetY by remember { mutableStateOf(0.01f) }
                        var rot1 by remember { mutableStateOf(0) }
                        if (umode2 != 0  || autou2) {
                            for (i in u2ships.values) {
                                if (i.id == 1) {
                                    offsetX = i.xcoord
                                    offsetY = i.ycoord
                                    rot1 = i.rot
                                }
                            }
                        }
                        IconButton(
                            onClick = {Soundplayer.rotate(context1, sounds); if (opacityu2[0] != 0.5f) rot1 = 1 - rot1; counter = 1 },
                            modifier = Modifier
                                .size((130*widthDp/411).dp, (130*heightDp/914).dp)
                                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragEnd = {
                                            Soundplayer.pop(context1, sounds)
                                            var min = 10000.toDouble()

                                            var minx = 0.001f
                                            var miny = 0.001f
                                            var closest = Snapping(-1f, -1f, 0, 0, -1, -1)
                                            for (i in tile1) {

                                                if (abs(i.x - offsetX).pow(2) + abs(i.y - offsetY).pow(
                                                        2
                                                    ) < min && (((0 < i.col && i.col < cols - 1 && rot1 === 0) || (i.width > 1 && rot1 === 0)) || ((0 < i.row && i.row < cols - 1 && rot1 === 1) || (i.height > 1 && rot1 === 1)))
                                                ) {
                                                    closest = i
                                                }

                                            }
                                            if (closest.height == 2 && rot1 == 0) {
                                                rot1 = 1
                                            } else if (closest.width == 2 && rot1 == 1) {
                                                rot1 = 0
                                            }
                                            for (i in tile1) {

                                                if (rot1 == 0 && (i.col == closest.col - 1 || i.col == closest.col + 1) && i.row == closest.row) {
                                                    if (i.height < 2) {
                                                        minx = closest.x
                                                        miny = closest.y
                                                    } else {
                                                        minx = 0.01f
                                                        miny = 0.01f
                                                        rot1 = 0
                                                    }
                                                } else if (rot1 == 1 && (i.row == closest.row - 1 || i.row == closest.row + 1) && i.col == closest.col) {
                                                    if (i.width < 2) {
                                                        minx = closest.x
                                                        miny = closest.y
                                                    } else {
                                                        minx = 0.01f
                                                        miny = 0.01f
                                                        rot1 = 0
                                                    }
                                                }
                                            }

                                            if (opacityu2[0] != 0.5f) {
                                                offsetX = minx
                                                offsetY = miny
                                                u2shipstrial[1] =
                                                    Shipposition(
                                                        1,
                                                        3,
                                                        closest.col,
                                                        closest.row,
                                                        rot1, offsetX, offsetY
                                                    )

                                            }
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            if (abs(offsetX) < 300f && opacityu2[0] != 0.5f) {
                                                offsetX += dragAmount.x
                                            }
                                            if (abs(offsetY) < 300f && opacityu2[0] != 0.5f) {
                                                offsetY += dragAmount.y
                                            }
                                        }

                                    )
                                }
                        )
                        {
                            Image(painter = painterResource(R.drawable.ship2),
                                contentDescription = "Ship",
                                modifier = Modifier.size((130*widthDp/411).dp, (60*heightDp/914).dp).graphicsLayer {
                                    rotationZ = (90f * rot1); transformOrigin =
                                    TransformOrigin(0.5f, 0.5f); clip = false; alpha = opacityu2[0]
                                })
                        }
                    }
                    if (ship2drag) {
                        var offsetX by remember { mutableStateOf(0.01f) }
                        var offsetY by remember { mutableStateOf(0.01f) }
                        var rot by remember { mutableStateOf(0) }

                        if (umode2 != 0|| autou2) {
                            for (i in u2ships.values) {
                                if (i.id == 2) {
                                    offsetX = i.xcoord
                                    offsetY = i.ycoord
                                    rot = i.rot
                                }
                            }
                        }
                        IconButton(
                            onClick = {Soundplayer.rotate(context1, sounds); if (opacityu2[1] != 0.5f) rot = 1 - rot; counter = 1 },
                            modifier = Modifier
                                .size((60*widthDp/411).dp, (60*heightDp/914).dp)
                                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragEnd = {
                                            Soundplayer.pop(context1, sounds)
                                            var min = 10000.toDouble()

                                            var minx = 0.01f
                                            var miny = 0.01f
                                            var closest = Snapping(-1f, -1f, 0, 0, -1, -1)
                                            for (i in tile1) {

                                                if (abs(i.x - offsetX).pow(2) + abs(i.y - offsetY).pow(
                                                        2
                                                    ) < min
                                                ) {
                                                    closest = i
                                                    minx = closest.x
                                                    miny = closest.y
                                                }

                                            }
                                            if (closest.height == 2 && rot == 0) {
                                                rot = 1
                                            } else if (closest.width == 2 && rot == 1) {
                                                rot = 0
                                            }



                                            if (opacityu2[1] != 0.5f) {
                                                offsetX = minx
                                                offsetY = miny
                                                u2shipstrial[2] =
                                                    Shipposition(
                                                        2,
                                                        1,
                                                        closest.col,
                                                        closest.row,
                                                        rot, offsetX, offsetY
                                                    )

                                            }

                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            if (abs(offsetX) < 300f && opacityu2[1] != 0.5f) {
                                                offsetX += dragAmount.x
                                            }
                                            if (abs(offsetY) < 300f && opacityu2[1] != 0.5f) {
                                                offsetY += dragAmount.y
                                            }
                                        }

                                    )
                                }
                        )
                        {
                            Image(painter = painterResource(R.drawable.ship3),
                                contentDescription = "Ship",
                                modifier = Modifier.size((60*widthDp/411).dp, (60*heightDp/914).dp).graphicsLayer {
                                    rotationZ = (90f * rot); transformOrigin =
                                    TransformOrigin(0.5f, 0.5f); clip = false; alpha = opacityu2[1]
                                })
                        }
                    }
                    if (ship3drag) {
                        var offsetX by remember { mutableStateOf(0.01f) }
                        var offsetY by remember { mutableStateOf(0.01f) }
                        var rot2 by remember { mutableStateOf(0) }
                        if (umode2 != 0 || autou2) {
                            for (i in u2ships.values) {
                                if (i.id == 3) {
                                    offsetX = i.xcoord
                                    offsetY = i.ycoord
                                    rot2 = i.rot
                                }
                            }
                        }
                        IconButton(
                            onClick = {Soundplayer.rotate(context1, sounds); if (opacityu2[2] != 0.5f) rot2 = 1 - rot2; counter = 1 },
                            modifier = Modifier
                                .size((100*widthDp/411).dp, (100*heightDp/914).dp)
                                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragEnd = {
                                            Soundplayer.pop(context1, sounds)
                                            val shipOffsetInBox = Offset(offsetX, offsetY)
                                            var min = 10000.toDouble()

                                            var minx = 0.01f
                                            var miny = 0.01f
                                            var closest = Snapping(-1f, -1f, 0, 0, -1, -1)
                                            for (i in tile1) {

                                                if ((abs(i.x - offsetX).pow(2) + abs(i.y - offsetY).pow(
                                                        2
                                                    ) < min)
                                                ) {

                                                    if (((-1 < i.col && i.col < cols - 1 && rot2 == 0) || (i.width == 2 && rot2 == 0)) || ((0 < i.row && i.row < cols && rot2 == 1) || (i.height > 1 && rot2 == 1))) {
                                                        closest = i

                                                    }
                                                }

                                            }
                                            if (closest.height == 2 && rot2 == 0) {
                                                rot2 = 1
                                            } else if (closest.width == 2 && rot2 == 1) {
                                                rot2 = 0
                                            }
                                            for (i in tile1) {

                                                if (rot2 == 0 && ((i.col == closest.col + 1 && i.row == closest.row) || i.width == 2)) {
                                                    if (i.height == 1) {
                                                        minx = closest.x
                                                        miny = closest.y
                                                    } else {

                                                        minx = 0.01f
                                                        miny = 0.01f
                                                        rot2 = 0

                                                    }
                                                } else if (rot2 == 1 && ((i.row == closest.row - 1 && i.col == closest.col) || i.height == 2)) {
                                                    if (i.width == 1) {
                                                        minx = closest.x
                                                        miny = closest.y
                                                    } else {

                                                        minx = 0.01f
                                                        miny = 0.01f
                                                        rot2 = 0
                                                    }
                                                }

                                            }
                                            if (opacityu2[2] != 0.5f) {
                                                offsetX = minx
                                                offsetY = miny
                                                u2shipstrial[3] =
                                                    Shipposition(
                                                        3,
                                                        2,
                                                        closest.col,
                                                        closest.row,
                                                        rot2, offsetX, offsetY
                                                    )

                                            }
                                    
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            if (abs(offsetX) < 300f && opacityu2[2] != 0.5f) {
                                                offsetX += dragAmount.x
                                            }
                                            if (abs(offsetY) < 300f && opacityu2[2] != 0.5f) {
                                                offsetY += dragAmount.y
                                            }
                                        }

                                    )
                                }
                        )
                        {
                            Image(painter = painterResource(R.drawable.ship1),
                                contentDescription = "Ship",
                                modifier = Modifier.size((100*widthDp/411).dp, (60*heightDp/914).dp).graphicsLayer {
                                    rotationZ = (90f * rot2); transformOrigin =
                                    TransformOrigin(0.5f, 0.5f); clip = false; alpha = opacityu2[2]
                                })
                        }
                    }
                    if (ship4drag) {
                        var offsetX by remember { mutableStateOf(0.01f) }
                        var offsetY by remember { mutableStateOf(0.01f) }
                        var rot3 by remember { mutableStateOf(0) }
                        if (umode2 != 0 || autou2) {
                            for (i in u2ships.values) {
                                if (i.id == 4) {
                                    offsetX = i.xcoord
                                    offsetY = i.ycoord
                                    rot3 = i.rot
                                }
                            }
                        }
                        IconButton(
                            onClick = {Soundplayer.rotate(context1, sounds); if (opacityu2[3] != 0.5f) rot3 = 1 - rot3; counter = 1 },
                            modifier = Modifier
                                .size((60*widthDp/411).dp, (60*heightDp/914).dp)
                                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragEnd = {
                                            Soundplayer.pop(context1, sounds)
                                            var min = 10000.toDouble()

                                            var minx = 0.01f
                                            var miny = 0.01f
                                            var closest = Snapping(-1f, -1f, 0, 0, -1, -1)
                                            for (i in tile1) {

                                                if (abs(i.x - offsetX).pow(2) + abs(i.y - offsetY).pow(
                                                        2
                                                    ) < min
                                                ) {
                                                    closest = i
                                                    minx = closest.x
                                                    miny = closest.y
                                                }

                                            }
                                            if (closest.height == 2 && rot3 == 0) {
                                                rot3 = 1
                                            } else if (closest.width == 2 && rot3 == 1) {
                                                rot3 = 0
                                            }



                                            if (opacityu2[3] != 0.5f) {
                                                offsetX = minx
                                                offsetY = miny
                                                u2shipstrial[4] =
                                                    Shipposition(
                                                        4,
                                                        1,
                                                        closest.col,
                                                        closest.row,
                                                        rot3, offsetX, offsetY
                                                    )

                                            }

                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            if (abs(offsetX) < 300f && opacityu2[3] != 0.5f) {
                                                offsetX += dragAmount.x
                                            }
                                            if (abs(offsetY) < 300f && opacityu2[3] != 0.5f) {
                                                offsetY += dragAmount.y
                                            }
                                        }

                                    )
                                }
                        )
                        {
                            Image(painter = painterResource(R.drawable.ship3),
                                contentDescription = "Ship",
                                modifier = Modifier.size((60*widthDp/411).dp, (60*heightDp/914).dp).graphicsLayer {
                                    rotationZ = (90f * rot3); transformOrigin =
                                    TransformOrigin(0.5f, 0.5f); clip = false; alpha = opacityu2[3]
                                })
                        }
                    }
                }
            }
            if ((u1turn != u2turn && umode2 % 2 != 0) || (u1turn == u2turn && umode1 % 2 == 0)) {
                Box(
                    modifier = Modifier.size((1400*widthDp/411).dp, (380*heightDp/914).dp)
                        .background(Color(0, 0, 0, 250)),
                    contentAlignment = Alignment.Center
                ) {
                    if (u1turn != u2turn && umode2 % 2 != 0) {
                        Image(
                            painter = painterResource(R.drawable.cannon),
                            contentDescription = "Cannon",
                            modifier = Modifier.graphicsLayer { rotationZ = 180f })
                    }
                    else{

                    var text = "⅄ℲIꓕꓤOℲ"
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        if (umode1 == 0) {
                            text = "⅄OꓶԀƎꓷ"
                        }
                        Box(
                            modifier = Modifier.background(Color.Gray).fillMaxWidth().height((30*heightDp/914).dp)
                        ) {

                        }
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape((25*widthDp/411).dp))
                                .background(Color.DarkGray).fillMaxWidth().height((130*heightDp/914).dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                    if (umode2==0) {

                                        IconButton(
                                            onClick = {Soundplayer.click(context1, sounds);
                                                autobot(
                                                    1,
                                                    tile1,
                                                    tileSizex,
                                                    tileSizey,
                                                    opacityu1
                                                ); autou1 = true;
                                            },
                                            modifier = Modifier.background(Color.Cyan)
                                                .size((100*widthDp/411).dp, (30*heightDp/914).dp)
                                        ) {
                                            Text(
                                                "OꓕꓵⱯ",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                        Spacer(Modifier.width((20*widthDp/411).dp))
                                    }
                                    if (ship1drag1 && ship2drag1 && ship3drag1 && ship4drag1) {

                                        IconButton(
                                            onClick = {
                                                val x1start = u1shipstrial[1]?.xcoord ?: -1f
                                                val y1start = u1shipstrial[1]?.ycoord ?: -1f
                                                val x2start = u1shipstrial[2]?.xcoord ?: -1f
                                                val y2start = u1shipstrial[2]?.ycoord ?: -1f
                                                val x3start = u1shipstrial[3]?.xcoord ?: -1f
                                                val y3start = u1shipstrial[3]?.ycoord ?: -1f
                                                val x4start = u1shipstrial[4]?.xcoord ?: -1f
                                                val y4start = u1shipstrial[4]?.ycoord ?: -1f

                                                if (x1start == -1f || x2start == -1f || x3start == -1f || x4start == -1f || y1start == -1f || y2start == -1f || y3start == -1f || y4start == -1f || u1shipstrial[1]?.xcoord == 0.01f || u1shipstrial[2]?.xcoord == 0.01f || u1shipstrial[3]?.xcoord == 0.01f || u1shipstrial[4]?.xcoord == 0.01f || u1shipstrial[1]?.ycoord == 0.01f || u1shipstrial[2]?.ycoord == 0.01f || u1shipstrial[3]?.ycoord == 0.01f || u1shipstrial[4]?.ycoord == 0.01f) {
                                                    showDialog = 1

                                                } else if (u1shipstrial[1] == null || u1shipstrial[2] == null || u1shipstrial[3] == null || u1shipstrial[4] == null) {
                                                    showDialog = 1
                                                } else {
                                                    if (u1shipstrial[1]?.rot == 0) {
                                                        for (i in (x1start - 3 * tileSizex / 2).toInt()..(x1start + 3 * tileSizex / 2).toInt()) {
                                                            if (i < (x2start + tileSizex / 2) && i > (x2start - tileSizex / 2) && y1start == y2start) {
                                                                showDialog = 2

                                                            }
                                                            if ((u1shipstrial[3]?.rot == 0 && i < (x3start + tileSizex) && i > (x3start - tileSizex) && y1start == y3start) || (u1shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && (u1shipstrial[3]!!.y - u1shipstrial[1]!!.y < 2))) {
                                                                showDialog = 2

                                                            }
                                                            if (i < (x4start + tileSizex / 2) && i > (x4start - tileSizex / 2) && y1start == y4start) {
                                                                showDialog = 2

                                                            }
                                                        }
                                                    } else if (u1shipstrial[1]?.rot == 1) {
                                                        for (i in (y1start - 3 * tileSizey / 2).toInt()..(y1start + 3 * tileSizey / 2).toInt()) {
                                                            if (i < (y2start + tileSizey / 2) && i > (y2start - tileSizey / 2) && x1start == x2start) {
                                                                showDialog = 2

                                                            }
                                                            if ((u1shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && x1start == x3start) || (u1shipstrial[3]?.rot == 0 && i < (x3start + tileSizey) && i > (x3start - tileSizey) && (u1shipstrial[3]!!.x - u1shipstrial[1]!!.x < 2))) {
                                                                showDialog = 2


                                                            }
                                                            if (i < (y4start + tileSizey / 2) && i > (y4start - tileSizey / 2) && x1start == x4start) {
                                                                showDialog = 2

                                                            }
                                                        }
                                                    }
                                                    if (u1shipstrial[2]?.rot == 0) {
                                                        for (i in (x2start - tileSizex / 2).toInt()..(x2start + tileSizex / 2).toInt()) {
                                                            if ((u1shipstrial[3]?.rot == 0 && i < (x3start + tileSizex) && i > (x3start - tileSizex) && y2start == y3start) || (u1shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && (u1shipstrial[3]!!.y - u1shipstrial[2]!!.y < 2))) {
                                                                showDialog = 2

                                                            }
                                                            if (i < (x4start + tileSizex / 2) && i > (x4start - tileSizex / 2) && y1start == y4start) {
                                                                showDialog = 2

                                                            }
                                                        }
                                                    } else if (u1shipstrial[2]?.rot == 1) {
                                                        for (i in (y2start - tileSizey / 2).toInt()..(y2start + tileSizey / 2).toInt()) {
                                                            if ((u1shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && x2start == x3start) || (u1shipstrial[3]?.rot == 0 && i < (x3start + tileSizey) && i > (x3start - tileSizey) && (u1shipstrial[3]!!.x - u1shipstrial[2]!!.x < 2))) {
                                                                showDialog = 2


                                                            }
                                                            if (i < (y4start + tileSizey / 2) && i > (y4start - tileSizey / 2) && x2start == x4start) {
                                                                showDialog = 2

                                                            }
                                                        }
                                                    }

                                                    if (u1shipstrial[4]?.rot == 0) {
                                                        for (i in (x4start - tileSizex / 2).toInt()..(x4start + tileSizex / 2).toInt()) {
                                                            if ((u1shipstrial[3]?.rot == 0 && i < (x3start + tileSizex) && i > (x3start - tileSizex) && y4start == y3start) || (u1shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && (u1shipstrial[3]!!.y - u1shipstrial[4]!!.y < 2))) {
                                                                showDialog = 2

                                                            }

                                                        }
                                                    } else if (u1shipstrial[4]?.rot == 1) {
                                                        for (i in (y4start - tileSizey / 2).toInt()..(y4start + tileSizey / 2).toInt()) {
                                                            if ((u1shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && x4start == x3start) || (u1shipstrial[3]?.rot == 0 && i < (x3start + tileSizey) && i > (x3start - tileSizey) && (u1shipstrial[3]!!.x - u1shipstrial[4]!!.x < 2))) {
                                                                showDialog = 2


                                                            }
                                                        }
                                                    }
                                                    for (i in u2attackedtiles) {
                                                        if ((opacityu1[0] == 1f && i.x == u1shipstrial[1]!!.x && i.y == u1shipstrial[1]!!.y) || (opacityu1[1] == 1f && i.x == u1shipstrial[2]!!.x && i.y == u1shipstrial[2]!!.y) || (opacityu1[2] == 1f && i.x == u1shipstrial[3]!!.x && i.y == u1shipstrial[3]!!.y) || (opacityu1[3] == 1f && i.x == u1shipstrial[4]!!.x && i.y == u1shipstrial[4]!!.y) || (opacityu1[0] == 1f && (i.x == u1shipstrial[1]!!.x - 1 || i.x == u1shipstrial[1]!!.x + 1) && u1shipstrial[1]!!.rot == 0 && i.y == u1shipstrial[1]!!.y) || (opacityu1[2] == 1f && i.x == u1shipstrial[3]!!.x + 1 && u1shipstrial[3]!!.rot == 0 && i.y == u1shipstrial[1]!!.y) || (opacityu1[0] == 1f && (i.y == u1shipstrial[1]!!.y - 1 || i.y == u1shipstrial[1]!!.y + 1) && u1shipstrial[1]!!.rot == 1 && i.x == u1shipstrial[1]!!.x) || (opacityu1[2] == 1f && i.y == u1shipstrial[3]!!.y + 1 && u1shipstrial[3]!!.rot == 1 && i.x == u1shipstrial[1]!!.x)) {
                                                            showDialog = 3
                                                        }
                                                    }
                                                    if (showDialog != 0) {
                                                        Soundplayer.error(context1, sounds)

                                                        for (i in u1ships.values) {
                                                            u1shipstrial[i.id] = i
                                                        }
                                                    }
                                                    if (showDialog == 0) {
                                                        Soundplayer.click(context1, sounds)
                                                        u1turn += 1; if (umode1 == 0) {
                                                            umode1 = 1
                                                        }; for (i in u1shipstrial.values) {
                                                            u1ships[i.id] = i
                                                        };if (np == 1 && umode2 == 0) {
                                                            botplacedships(
                                                                tile1,
                                                                tileSizex,
                                                                tileSizey,
                                                                opacityu2
                                                            )
                                                            u2turn += 1
                                                        };ship1drag1 = false
                                                    }
                                                }
                                            },
                                            modifier = Modifier.background(Color.Cyan)
                                                .size((100*widthDp/411).dp, (30*heightDp/914).dp)
                                        ) {
                                            Text(
                                                "ƎꓥⱯS",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                                    Text(
                                        "ɯǝɥʇ ǝɔɐןd oʇ sdı̣ɥs doɹp puɐ ƃɐɹꓷ",
                                        fontSize = 15.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        "$text",
                                        fontSize = 40.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        textAlign = TextAlign.Center
                                    )
                                }

                            }
                        }
                    }
                }

            }
            if (cannons1==0 && cannons2==0){
                refresh.value=true

            }
        }
}
@Composable
fun MyDialogDemo(text: String):Int {
    var showDialog by remember { mutableStateOf(true) }


        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false;  }) {
                Surface(
                    shape = RoundedCornerShape((12).dp),
                    color = Color.White,
                    tonalElevation = 8.dp
                ) {
                    Column(modifier = Modifier.padding((16).dp, (16).dp)) {
                        Text(text)
                        Spacer(modifier = Modifier.height((8).dp))
                        Button(onClick = { showDialog = false }) {
                            Text("Close", color=Color.Black)
                        }
                    }
                }
            }
        }
    return 0


}
fun botplacedships(tile1: MutableList<Snapping>, tileSizex: Float, tileSizey: Float, opacityu2: List<Float> ) {
    for (id in 1..4){
        var tile=tile1.random()
        var rot= Random.nextInt(2)
        u2shipstrial[id]= Shipposition(id, if (id==1) 3; else if (id==3) 2; else 1,tile.col,tile.row,rot,tile.x,tile.y)
    }
    var showDialog=0
    val x1start = u2shipstrial[1]?.xcoord ?: -1f
    val y1start = u2shipstrial[1]?.ycoord ?: -1f
    val x2start = u2shipstrial[2]?.xcoord ?: -1f
    val y2start = u2shipstrial[2]?.ycoord ?: -1f
    val x3start = u2shipstrial[3]?.xcoord ?: -1f
    val y3start = u2shipstrial[3]?.ycoord ?: -1f
    val x4start = u2shipstrial[4]?.xcoord ?: -1f
    val y4start = u2shipstrial[4]?.ycoord ?: -1f

    if (x1start == -1f || x2start == -1f || x3start == -1f || x4start == -1f || y1start == -1f || y2start == -1f || y3start == -1f || y4start == -1f || u2shipstrial[1]?.xcoord == 0.01f || u2shipstrial[2]?.xcoord == 0.01f || u2shipstrial[3]?.xcoord == 0.01f || u2shipstrial[4]?.xcoord == 0.01f || u2shipstrial[1]?.ycoord == 0.01f || u2shipstrial[2]?.ycoord == 0.01f || u2shipstrial[3]?.ycoord == 0.01f || u2shipstrial[4]?.ycoord == 0.01f) {
        showDialog = 1
    } else if (u2shipstrial[1] == null || u2shipstrial[2] == null || u2shipstrial[3] == null || u2shipstrial[4] == null) {
        showDialog = 1
    } else {
        if (u2shipstrial[1]?.rot == 0) {
            if (u2shipstrial[1]?.x==0 || u2shipstrial[1]?.x==(cols-1)){
                showDialog=1
            }
            for (i in (x1start - 3 * tileSizex / 2).toInt()..(x1start + 3 * tileSizex / 2).toInt()) {
                if (i < (x2start + tileSizex / 2) && i > (x2start - tileSizex / 2) && y1start == y2start) {
                    showDialog = 2
                }
                if ((u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizex) && i > (x3start - tileSizex) && y1start == y3start) || (u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && (u2shipstrial[3]!!.y - u2shipstrial[1]!!.y < 2))) {
                    showDialog = 2

                }
                if (i < (x4start + tileSizex / 2) && i > (x4start - tileSizex / 2) && y1start == y4start) {
                    showDialog = 2
                }
            }
        } else if (u2shipstrial[1]?.rot == 1) {
            if (u2shipstrial[1]?.y==0 || u2shipstrial[1]?.y==(rows-1)){
                showDialog=1
            }
            for (i in (y1start - 3 * tileSizey / 2).toInt()..(y1start + 3 * tileSizey / 2).toInt()) {
                if (i < (y2start + tileSizey / 2) && i > (y2start - tileSizey / 2) && x1start == x2start) {
                    showDialog = 2
                }
                if ((u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && x1start == x3start) || (u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizey) && i > (x3start - tileSizey) && (u2shipstrial[3]!!.x - u2shipstrial[1]!!.x < 2))) {
                    showDialog = 2

                }
                if (i < (y4start + tileSizey / 2) && i > (y4start - tileSizey / 2) && x1start == x4start) {
                    showDialog = 2
                }
            }
        }
        if (u2shipstrial[3]?.rot==0){
            if (u2shipstrial[3]?.x==(cols-1)){
                showDialog=1
            }
        }
        else if (u2shipstrial[3]?.rot==1){
            if (u2shipstrial[3]?.x==(rows-1)){
                showDialog=1
            }
        }
        if (u2shipstrial[2]?.rot == 0) {
            for (i in (x2start - tileSizex / 2).toInt()..(x2start + tileSizex / 2).toInt()) {
                if ((u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizex) && i > (x3start - tileSizex) && y2start == y3start) || (u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && (u2shipstrial[3]!!.y - u2shipstrial[2]!!.y < 2))) {
                    showDialog = 2

                }
                if (i < (x4start + tileSizex / 2) && i > (x4start - tileSizex / 2) && y1start == y4start) {
                    showDialog = 2
                }
            }
        } else if (u2shipstrial[2]?.rot == 1) {
            for (i in (y2start - tileSizey / 2).toInt()..(y2start + tileSizey / 2).toInt()) {
                if ((u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && x2start == x3start) || (u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizey) && i > (x3start - tileSizey) && (u2shipstrial[3]!!.x - u2shipstrial[2]!!.x < 2))) {
                    showDialog = 2

                }
                if (i < (y4start + tileSizey / 2) && i > (y4start - tileSizey / 2) && x2start == x4start) {
                    showDialog = 2
                }
            }
        }
        if (u2shipstrial[4]?.rot == 0) {
            for (i in (x4start - tileSizex / 2).toInt()..(x4start + tileSizex / 2).toInt()) {
                if ((u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizex) && i > (x3start - tileSizex) && y4start == y3start) || (u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && (u2shipstrial[3]!!.y - u2shipstrial[4]!!.y < 2))) {
                    showDialog = 2

                }

            }
        } else if (u2shipstrial[4]?.rot == 1) {
            for (i in (y4start - tileSizey / 2).toInt()..(y4start + tileSizey / 2).toInt()) {
                if ((u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && x4start == x3start) || (u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizey) && i > (x3start - tileSizey) && (u2shipstrial[3]!!.x - u2shipstrial[4]!!.x < 2))) {
                    showDialog = 2

                }
            }
        }
        for (i in u1attackedtiles) {
            if ((opacityu2[0] == 1f && i.x == u2shipstrial[1]!!.x && i.y == u2shipstrial[1]!!.y) || (opacityu2[1] == 1f && i.x == u2shipstrial[2]!!.x && i.y == u2shipstrial[2]!!.y) || (opacityu2[2] == 1f && i.x == u2shipstrial[3]!!.x && i.y == u2shipstrial[3]!!.y) || (opacityu2[3] == 1f && i.x == u2shipstrial[4]!!.x && i.y == u2shipstrial[4]!!.y) || (opacityu2[0] == 1f && (i.x == u2shipstrial[1]!!.x - 1 || i.x == u2shipstrial[1]!!.x + 1) && u2shipstrial[1]!!.rot == 0 && i.y == u2shipstrial[1]!!.y) || (opacityu2[2] == 1f && i.x == u2shipstrial[3]!!.x + 1 && u2shipstrial[3]!!.rot == 0 && i.y == u2shipstrial[1]!!.y) || (opacityu2[0] == 1f && (i.y == u2shipstrial[1]!!.y - 1 || i.y == u2shipstrial[1]!!.y + 1) && u2shipstrial[1]!!.rot == 1 && i.x == u2shipstrial[1]!!.x) || (opacityu2[2] == 1f && i.y == u2shipstrial[3]!!.y + 1 && u2shipstrial[3]!!.rot == 1 && i.x == u2shipstrial[1]!!.x)) {
                showDialog = 3
            }
        }

    }
    if (showDialog==0){
        if (umode2 == 0) {
            umode2 = 1
        }; for (i in u2shipstrial.values) {
            u2ships[i.id] = i
        };


    }
    else{
        botplacedships(tile1,tileSizex,tileSizey,opacityu2)
    }
}
fun autobot(playerno: Int, tile1: MutableList<Snapping>, tileSizex: Float, tileSizey: Float, opacityu2: List<Float>) {
    if (playerno==2) {
        for (id in 1..4) {
            var tile = tile1.random()
            var rot = Random.nextInt(2)
            u2shipstrial[id] = Shipposition(
                id,
                if (id == 1) 3; else if (id == 3) 2; else 1,
                tile.col,
                tile.row,
                rot,
                tile.x,
                tile.y
            )
        }
        var showDialog = 0
        val x1start = u2shipstrial[1]?.xcoord ?: -1f
        val y1start = u2shipstrial[1]?.ycoord ?: -1f
        val x2start = u2shipstrial[2]?.xcoord ?: -1f
        val y2start = u2shipstrial[2]?.ycoord ?: -1f
        val x3start = u2shipstrial[3]?.xcoord ?: -1f
        val y3start = u2shipstrial[3]?.ycoord ?: -1f
        val x4start = u2shipstrial[4]?.xcoord ?: -1f
        val y4start = u2shipstrial[4]?.ycoord ?: -1f

        if (x1start == -1f || x2start == -1f || x3start == -1f || x4start == -1f || y1start == -1f || y2start == -1f || y3start == -1f || y4start == -1f || u2shipstrial[1]?.xcoord == 0.01f || u2shipstrial[2]?.xcoord == 0.01f || u2shipstrial[3]?.xcoord == 0.01f || u2shipstrial[4]?.xcoord == 0.01f || u2shipstrial[1]?.ycoord == 0.01f || u2shipstrial[2]?.ycoord == 0.01f || u2shipstrial[3]?.ycoord == 0.01f || u2shipstrial[4]?.ycoord == 0.01f) {
            showDialog = 1
        } else if (u2shipstrial[1] == null || u2shipstrial[2] == null || u2shipstrial[3] == null || u2shipstrial[4] == null) {
            showDialog = 1
        } else {
            if (u2shipstrial[1]?.rot == 0) {
                if (u2shipstrial[1]?.x==0 || u2shipstrial[1]?.x==(cols-1)){
                    showDialog=1
                }
                for (i in (x1start - 3 * tileSizex / 2).toInt()..(x1start + 3 * tileSizex / 2).toInt()) {
                    if (i < (x2start + tileSizex / 2) && i > (x2start - tileSizex / 2) && y1start == y2start) {
                        showDialog = 2
                    }
                    if ((u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizex) && i > (x3start - tileSizex) && y1start == y3start) || (u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && (u2shipstrial[3]!!.y - u2shipstrial[1]!!.y < 2))) {
                        showDialog = 2

                    }
                    if (i < (x4start + tileSizex / 2) && i > (x4start - tileSizex / 2) && y1start == y4start) {
                        showDialog = 2
                    }
                }
            } else if (u2shipstrial[1]?.rot == 1) {
                if (u2shipstrial[1]?.y==0 || u2shipstrial[1]?.y==(rows-1)){
                    showDialog=1
                }
                for (i in (y1start - 3 * tileSizey / 2).toInt()..(y1start + 3 * tileSizey / 2).toInt()) {
                    if (i < (y2start + tileSizey / 2) && i > (y2start - tileSizey / 2) && x1start == x2start) {
                        showDialog = 2
                    }
                    if ((u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && x1start == x3start) || (u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizey) && i > (x3start - tileSizey) && (u2shipstrial[3]!!.x - u2shipstrial[1]!!.x < 2))) {
                        showDialog = 2

                    }
                    if (i < (y4start + tileSizey / 2) && i > (y4start - tileSizey / 2) && x1start == x4start) {
                        showDialog = 2
                    }
                }
            }
            if (u2shipstrial[3]?.rot==0){
                if (u2shipstrial[3]?.x==(cols-1)){
                    showDialog=1
                }
            }
            else if (u2shipstrial[3]?.rot==1){
                if (u2shipstrial[3]?.x==(rows-1)){
                    showDialog=1
                }
            }
            if (u2shipstrial[2]?.rot == 0) {
                for (i in (x2start - tileSizex / 2).toInt()..(x2start + tileSizex / 2).toInt()) {
                    if ((u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizex) && i > (x3start - tileSizex) && y2start == y3start) || (u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && (u2shipstrial[3]!!.y - u2shipstrial[2]!!.y < 2))) {
                        showDialog = 2

                    }
                    if (i < (x4start + tileSizex / 2) && i > (x4start - tileSizex / 2) && y1start == y4start) {
                        showDialog = 2
                    }
                }
            } else if (u2shipstrial[2]?.rot == 1) {
                for (i in (y2start - tileSizey / 2).toInt()..(y2start + tileSizey / 2).toInt()) {
                    if ((u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && x2start == x3start) || (u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizey) && i > (x3start - tileSizey) && (u2shipstrial[3]!!.x - u2shipstrial[2]!!.x < 2))) {
                        showDialog = 2

                    }
                    if (i < (y4start + tileSizey / 2) && i > (y4start - tileSizey / 2) && x2start == x4start) {
                        showDialog = 2
                    }
                }
            }
            if (u2shipstrial[4]?.rot == 0) {
                for (i in (x4start - tileSizex / 2).toInt()..(x4start + tileSizex / 2).toInt()) {
                    if ((u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizex) && i > (x3start - tileSizex) && y4start == y3start) || (u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && (u2shipstrial[3]!!.y - u2shipstrial[4]!!.y < 2))) {
                        showDialog = 2

                    }

                }
            } else if (u2shipstrial[4]?.rot == 1) {
                for (i in (y4start - tileSizey / 2).toInt()..(y4start + tileSizey / 2).toInt()) {
                    if ((u2shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && x4start == x3start) || (u2shipstrial[3]?.rot == 0 && i < (x3start + tileSizey) && i > (x3start - tileSizey) && (u2shipstrial[3]!!.x - u2shipstrial[4]!!.x < 2))) {
                        showDialog = 2

                    }
                }
            }
            for (i in u1attackedtiles) {
                if ((opacityu2[0] == 1f && i.x == u2shipstrial[1]!!.x && i.y == u2shipstrial[1]!!.y) || (opacityu2[1] == 1f && i.x == u2shipstrial[2]!!.x && i.y == u2shipstrial[2]!!.y) || (opacityu2[2] == 1f && i.x == u2shipstrial[3]!!.x && i.y == u2shipstrial[3]!!.y) || (opacityu2[3] == 1f && i.x == u2shipstrial[4]!!.x && i.y == u2shipstrial[4]!!.y) || (opacityu2[0] == 1f && (i.x == u2shipstrial[1]!!.x - 1 || i.x == u2shipstrial[1]!!.x + 1) && u2shipstrial[1]!!.rot == 0 && i.y == u2shipstrial[1]!!.y) || (opacityu2[2] == 1f && i.x == u2shipstrial[3]!!.x + 1 && u2shipstrial[3]!!.rot == 0 && i.y == u2shipstrial[1]!!.y) || (opacityu2[0] == 1f && (i.y == u2shipstrial[1]!!.y - 1 || i.y == u2shipstrial[1]!!.y + 1) && u2shipstrial[1]!!.rot == 1 && i.x == u2shipstrial[1]!!.x) || (opacityu2[2] == 1f && i.y == u2shipstrial[3]!!.y + 1 && u2shipstrial[3]!!.rot == 1 && i.x == u2shipstrial[1]!!.x)) {
                    showDialog = 3
                }
            }

        }
        if (showDialog == 0) {
             for (i in u2shipstrial.values) {
                u2ships[i.id] = i
            };


        } else {
            autobot(2,tile1, tileSizex, tileSizey, opacityu2)
        }
    }
    else if (playerno==1) {
        for (id in 1..4) {
            var tile = tile1.random()
            var rot = Random.nextInt(2)
            u1shipstrial[id] = Shipposition(
                id,
                if (id == 1) 3; else if (id == 3) 2; else 1,
                tile.col,
                tile.row,
                rot,
                tile.x,
                tile.y
            )
        }
        var showDialog = 0
        val x1start = u1shipstrial[1]?.xcoord ?: -1f
        val y1start = u1shipstrial[1]?.ycoord ?: -1f
        val x2start = u1shipstrial[2]?.xcoord ?: -1f
        val y2start = u1shipstrial[2]?.ycoord ?: -1f
        val x3start = u1shipstrial[3]?.xcoord ?: -1f
        val y3start = u1shipstrial[3]?.ycoord ?: -1f
        val x4start = u1shipstrial[4]?.xcoord ?: -1f
        val y4start = u1shipstrial[4]?.ycoord ?: -1f

        if (x1start == -1f || x2start == -1f || x3start == -1f || x4start == -1f || y1start == -1f || y2start == -1f || y3start == -1f || y4start == -1f || u1shipstrial[1]?.xcoord == 0.01f || u1shipstrial[2]?.xcoord == 0.01f || u1shipstrial[3]?.xcoord == 0.01f || u1shipstrial[4]?.xcoord == 0.01f || u1shipstrial[1]?.ycoord == 0.01f || u1shipstrial[2]?.ycoord == 0.01f || u1shipstrial[3]?.ycoord == 0.01f || u1shipstrial[4]?.ycoord == 0.01f) {
            showDialog = 1
        } else if (u1shipstrial[1] == null || u1shipstrial[2] == null || u1shipstrial[3] == null || u1shipstrial[4] == null) {
            showDialog = 1
        } else {
            if (u1shipstrial[1]?.rot == 0) {
                if (u1shipstrial[1]?.x==(cols-1) || (u1shipstrial[1]?.x==0)){
                    showDialog=1
                }
                for (i in (x1start - 3 * tileSizex / 2).toInt()..(x1start + 3 * tileSizex / 2).toInt()) {
                    if (i < (x2start + tileSizex / 2) && i > (x2start - tileSizex / 2) && y1start == y2start) {
                        showDialog = 2
                    }
                    if ((u1shipstrial[3]?.rot == 0 && i < (x3start + tileSizex) && i > (x3start - tileSizex) && y1start == y3start) || (u1shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && (u1shipstrial[3]!!.y - u1shipstrial[1]!!.y < 2))) {
                        showDialog = 2

                    }
                    if (i < (x4start + tileSizex / 2) && i > (x4start - tileSizex / 2) && y1start == y4start) {
                        showDialog = 2
                    }
                }
            } else if (u1shipstrial[1]?.rot == 1) {
                if (u1shipstrial[1]?.y==(rows-1) || (u1shipstrial[1]?.y==0)){
                    showDialog=1
                }
                for (i in (y1start - 3 * tileSizey / 2).toInt()..(y1start + 3 * tileSizey / 2).toInt()) {
                    if (i < (y2start + tileSizey / 2) && i > (y2start - tileSizey / 2) && x1start == x2start) {
                        showDialog = 2
                    }
                    if ((u1shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && x1start == x3start) || (u1shipstrial[3]?.rot == 0 && i < (x3start + tileSizey) && i > (x3start - tileSizey) && (u1shipstrial[3]!!.x - u1shipstrial[1]!!.x < 2))) {
                        showDialog = 2

                    }
                    if (i < (y4start + tileSizey / 2) && i > (y4start - tileSizey / 2) && x1start == x4start) {
                        showDialog = 2
                    }
                }
            }
            if (u1shipstrial[3]?.rot==0){
                if (u1shipstrial[3]?.x==(cols-1)){
                    showDialog=1
                }
            }
            else if (u1shipstrial[3]?.rot==1){
                if (u1shipstrial[3]?.x==(rows-1)){
                    showDialog=1
                }
            }
            if (u1shipstrial[2]?.rot == 0) {
                for (i in (x2start - tileSizex / 2).toInt()..(x2start + tileSizex / 2).toInt()) {
                    if ((u1shipstrial[3]?.rot == 0 && i < (x3start + tileSizex) && i > (x3start - tileSizex) && y2start == y3start) || (u1shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && (u1shipstrial[3]!!.y - u1shipstrial[2]!!.y < 2))) {
                        showDialog = 2

                    }
                    if (i < (x4start + tileSizex / 2) && i > (x4start - tileSizex / 2) && y1start == y4start) {
                        showDialog = 2
                    }
                }
            } else if (u1shipstrial[2]?.rot == 1) {
                for (i in (y2start - tileSizey / 2).toInt()..(y2start + tileSizey / 2).toInt()) {
                    if ((u1shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && x2start == x3start) || (u1shipstrial[3]?.rot == 0 && i < (x3start + tileSizey) && i > (x3start - tileSizey) && (u1shipstrial[3]!!.x - u1shipstrial[2]!!.x < 2))) {
                        showDialog = 2

                    }
                    if (i < (y4start + tileSizey / 2) && i > (y4start - tileSizey / 2) && x2start == x4start) {
                        showDialog = 2
                    }
                }
            }
            if (u1shipstrial[4]?.rot == 0) {
                for (i in (x4start - tileSizex / 2).toInt()..(x4start + tileSizex / 2).toInt()) {
                    if ((u1shipstrial[3]?.rot == 0 && i < (x3start + tileSizex) && i > (x3start - tileSizex) && y4start == y3start) || (u1shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && (u1shipstrial[3]!!.y - u1shipstrial[4]!!.y < 2))) {
                        showDialog = 2

                    }

                }
            } else if (u1shipstrial[4]?.rot == 1) {
                for (i in (y4start - tileSizey / 2).toInt()..(y4start + tileSizey / 2).toInt()) {
                    if ((u1shipstrial[3]?.rot == 1 && i < (y3start + tileSizey) && i > (y3start - tileSizey) && x4start == x3start) || (u1shipstrial[3]?.rot == 0 && i < (x3start + tileSizey) && i > (x3start - tileSizey) && (u1shipstrial[3]!!.x - u1shipstrial[4]!!.x < 2))) {
                        showDialog = 2


                    }
                }
            }
            for (i in u2attackedtiles) {
                if ((opacityu2[0] == 1f && i.x == u1shipstrial[1]!!.x && i.y == u1shipstrial[1]!!.y) || (opacityu2[1] == 1f && i.x == u1shipstrial[2]!!.x && i.y == u1shipstrial[2]!!.y) || (opacityu2[2] == 1f && i.x == u1shipstrial[3]!!.x && i.y == u1shipstrial[3]!!.y) || (opacityu2[3] == 1f && i.x == u1shipstrial[4]!!.x && i.y == u1shipstrial[4]!!.y) || (opacityu2[0] == 1f && (i.x == u1shipstrial[1]!!.x - 1 || i.x == u1shipstrial[1]!!.x + 1) && u1shipstrial[1]!!.rot == 0 && i.y == u1shipstrial[1]!!.y) || (opacityu2[2] == 1f && i.x == u1shipstrial[3]!!.x + 1 && u1shipstrial[3]!!.rot == 0 && i.y == u1shipstrial[1]!!.y) || (opacityu2[0] == 1f && (i.y == u1shipstrial[1]!!.y - 1 || i.y == u1shipstrial[1]!!.y + 1) && u1shipstrial[1]!!.rot == 1 && i.x == u1shipstrial[1]!!.x) || (opacityu2[2] == 1f && i.y == u1shipstrial[3]!!.y + 1 && u1shipstrial[3]!!.rot == 1 && i.x == u1shipstrial[1]!!.x)) {
                    showDialog = 3
                }
            }

        }
        if (showDialog == 0) {
            for (i in u1shipstrial.values) {
                u1ships[i.id] = i
            };


        } else {
            autobot(1,tile1, tileSizex, tileSizey,opacityu2)
        }
    }
}
fun botchooseattack(difficulty: Int, tile: List<Tile>): Tile{
    if (difficulty==0){
        var tile1 = tile.toMutableList()

        for (i in u1ships.values){
            for (j in tile){
                if (i.x==j.x && i.y==j.y){

                    tile1.addLast(j)

                }
            }
        }
        for (k in u2attackedtiles){
            for (i in tile){
                if (k.x==i.x && k.y == i.y){
                    tile1.remove(i)
                }
            }
        }
        return tile1.random()
    }
    if (difficulty==1){
        var tile1 = tile.toMutableList()

        for (i in u1ships.values){
            for (j in tile){
                if (i.x==j.x && i.y==j.y){
                    if (i.size==3 && i.rot==0){
                        tile1.addLast(j)
                        tile1.addLast(Tile(j.x-1, j.y, j.width,j.height))
                        tile1.addLast(Tile(j.x+1, j.y, j.width,j.height))
                    }
                    else if (i.size==3 && i.rot==1){
                        tile1.addLast(j)
                        tile1.addLast(Tile(j.x, j.y-1, j.width,j.height))
                        tile1.addLast(Tile(j.x, j.y+1, j.width,j.height))
                    }
                    if (i.size==2 && i.rot==0){
                        tile1.addLast(j)
                        tile1.addLast(Tile(j.x+1, j.y, j.width,j.height))
                    }
                    else if (i.size==2 && i.rot==1){
                        tile1.addLast(j)
                        tile1.addLast(Tile(j.x, j.y+1, j.width,j.height))
                    }
                    else{
                        tile1.addLast(j)
                        tile1.addLast(j)
                    }
                }
            }
        }
        for (k in u2attackedtiles){
            for (i in tile){
                if (k.x==i.x && k.y == i.y){
                    tile1.remove(i)
                }
            }
        }
        println(tile1)

        return tile1.random()
    }
    if (difficulty==2){
        var tile1 = tile.toMutableList()

        for (i in u1ships.values){
            for (j in tile){
                if (i.x==j.x && i.y==j.y){
                    if (i.size==3 && i.rot==0){
                        tile1.addLast(j)
                        tile1.addLast(j)
                        tile1.addLast(Tile(j.x-1, j.y, j.width,j.height))
                        tile1.addLast(Tile(j.x+1, j.y, j.width,j.height))
                    }
                    else if (i.size==3 && i.rot==1){
                        tile1.addLast(j)
                        tile1.addLast(j)
                        tile1.addLast(Tile(j.x, j.y-1, j.width,j.height))
                        tile1.addLast(Tile(j.x, j.y+1, j.width,j.height))
                    }
                    if (i.size==2 && i.rot==0){
                        tile1.addLast(j)
                        tile1.addLast(j)
                        tile1.addLast(Tile(j.x+1, j.y, j.width,j.height))
                        tile1.addLast(Tile(j.x+1, j.y, j.width,j.height))
                    }
                    else if (i.size==2 && i.rot==1){
                        tile1.addLast(j)
                        tile1.addLast(j)
                        tile1.addLast(Tile(j.x, j.y+1, j.width,j.height))
                        tile1.addLast(Tile(j.x, j.y+1, j.width,j.height))
                    }
                    else{
                        tile1.addLast(j)
                        tile1.addLast(j)
                        tile1.addLast(j)
                        tile1.addLast(j)
                    }
                }
            }
        }
        for (k in u2attackedtiles){
            for (i in tile){
                if (k.x==i.x && k.y == i.y){
                    tile1.remove(i)
                }
            }
        }
        println(tile1)

        return tile1.random()


    }

    return tile.random()

}
fun generateGrid(rows: Int, cols: Int,enable_tiles: Boolean): List<Tile> {

    val occupied = Array(rows) { BooleanArray(cols) }
    val tiles = mutableListOf<Tile>()

    val combinedTileCount = if (enable_tiles)  (1..3).random() else 0
    var placedCombined = 0
    val positions = mutableListOf<Pair<Int, Int>>()
    for (y in 0 until rows) {
        for (x in 0 until cols) {
            positions.add(x to y)
        }
    }
    positions.shuffle()
    fun canPlace(x: Int, y: Int, w: Int, h: Int): Boolean {
        if (x + w > cols || y + h > rows) return false
        for (dy in 0 until h) {
            for (dx in 0 until w) {
                if (occupied[y + dy][x + dx]) return false
            }
        }
        return true
    }

    for ((x, y) in positions) {
        if (occupied[y][x]) continue

        var width = 1
        var height = 1

        if (placedCombined < combinedTileCount) {
            val types = listOf("1x2", "2x1").shuffled()
            for (type in types) {
                if (type == "1x2" && canPlace(x, y, 2, 1)) {
                    width = 2
                    placedCombined++
                    break
                } else if (type == "2x1" && canPlace(x, y, 1, 2)) {
                    height = 2
                    placedCombined++
                    break
                }
            }
        }

        for (dy in 0 until height) {
            for (dx in 0 until width) {
                occupied[y + dy][x + dx] = true
            }
        }

        tiles.add(Tile(x, y, width, height))
    }

    return tiles


}

@Composable
fun CaptureScreenshotOnGameEnd(captureTrigger: Boolean, onCaptured: (Bitmap) -> Unit) {
    val context= LocalContext.current
    val view = LocalView.current

    LaunchedEffect(captureTrigger) {
        delay(20)
        if (captureTrigger) {
            withFrameNanos {
                val bitmap = view.drawToBitmap()
                onCaptured(bitmap)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
fun savePlayerNames(context: Context, names: String) {
    val fileOutput = context.openFileOutput("players.txt", Context.MODE_APPEND)
    val scoreOutput = context.openFileOutput("scores.txt", Context.MODE_APPEND)
    val botscoreOutput = context.openFileOutput("bot.txt", Context.MODE_APPEND)
    fileOutput.write("$names\n".toByteArray())
    scoreOutput.write("0\n".toByteArray())
    botscoreOutput.write("0\n".toByteArray())
}


fun loadPlayerNames(context: Context): List<String> {
    return try {
        val fileInput = context.openFileInput("players.txt")
        fileInput.bufferedReader().readLines()
    } catch (e: Exception) {
        emptyList()

    }
}
fun readsettings(context: Context): List<String> {
    return try {
        val fileInput = context.openFileInput("settings.txt")
        fileInput.bufferedReader().readLines()
    } catch (e: Exception) {
        val scoreOutput = context.openFileOutput("settings.txt", Context.MODE_PRIVATE)
        scoreOutput.write("true\ntrue\ntrue\n100\ntrue\n".toByteArray())
        listOf("true", "true", "true", "100", "true")
    }
}
fun resetsettings(context: Context) {
    val scoreOutput = context.openFileOutput("settings.txt", Context.MODE_PRIVATE)
    scoreOutput.write("true\ntrue\ntrue\n100\ntrue\n".toByteArray())
  
}
fun editsettings(context: Context, number: Int, vol: Float) {
    val fileInput = context.openFileInput("settings.txt")
    var input=fileInput.bufferedReader().readLines().toMutableList()
    if (number!=3){
        input[number]=(!input[number].toBoolean()).toString()
    }
    else{
        input[number]=vol.toString()
    }
    val scoreOutput = context.openFileOutput("settings.txt", Context.MODE_PRIVATE)
    for (i in input){
        scoreOutput.write((i+"\n").toByteArray())
    }
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
fun scoreList(context: Context): Map<String,List<String>> {
    return try {
        val fileInput = context.openFileInput("players.txt")
        val fileInput2 = context.openFileInput("scores.txt")
        val fileInput3 = context.openFileInput("bot.txt")
        val key=fileInput.bufferedReader().readLines()
        val value1=fileInput2.bufferedReader().readLines()
        val value2=fileInput3.bufferedReader().readLines()
        var value= mutableListOf(listOf("",""))

        for (i in 0..value1.size-1){

            value.addLast(listOf(value1[i],value2[i]))

        }
        value.removeFirst()
        key.zip(value).toMap().toList().sortedByDescending  { it.second.first().toInt() }.toMap()
    } catch (e: Exception) {
        val key = listOf("")
        val value = listOf( listOf(""))
        
        key.zip(value).toMap()
    }
}
fun score_update(context: Context, winner: String){
    return try {
        val fileInput = context.openFileInput("players.txt")
        val fileInput2 = context.openFileInput("scores.txt")
        val key=fileInput.bufferedReader().readLines()
        val value1=fileInput2.bufferedReader().readLines()
        var map= key.zip(value1).toMap()
        var map1=map.toMutableMap()
        val fileOutput= context.openFileOutput("scores.txt",Context.MODE_PRIVATE)
        map1[winner]=(map1[winner]?.toInt()?.plus(1)).toString()
        for (i in map1.values){
            fileOutput.write("$i\n".toByteArray())
        }
    } catch (e: Exception) {
        val key = listOf("")
        val value = listOf( listOf(""))
     

    }
}
fun bot_update(context: Context, winner: String){
    return try {
        val fileInput = context.openFileInput("players.txt")
        val fileInput2 = context.openFileInput("bot.txt")
        val key=fileInput.bufferedReader().readLines()
        val value1=fileInput2.bufferedReader().readLines()
        var map= key.zip(value1).toMap()
        var map1=map.toMutableMap()
        val fileOutput= context.openFileOutput("bot.txt",Context.MODE_PRIVATE)
        map1[winner]=(map1[winner]?.toInt()?.plus(1)).toString()
        for (i in map1.values){
            fileOutput.write("$i\n".toByteArray())
        }
    } catch (e: Exception) {
        val key = listOf("")
        val value = listOf( listOf(""))


    }
}
fun clear_history(context: Context){
    clearAllScreenshots(context)
    val fileInput=context.openFileInput("scores.txt")
    var input1=fileInput.bufferedReader().readLines().toMutableList()
    val fileInput2=context.openFileInput("bot.txt")
    var input2=fileInput2.bufferedReader().readLines().toMutableList()
    val fileOutput= context.openFileOutput("scores.txt",Context.MODE_PRIVATE)
    val fileOutput2= context.openFileOutput("bot.txt",Context.MODE_PRIVATE)
    for (i in 0..input1.size-1){
        input1[i]="0\n"
        input2[i]="0\n"
        fileOutput.write(input1[i].toByteArray())
        fileOutput2.write(input2[i].toByteArray())
    }

}
fun clearAllScreenshots(context: Context) {
    val dir = File(context.filesDir, "match_history")
    dir.listFiles()?.forEach { file ->
        println(file.name)
        if (file.name.startsWith("match_") && file.name.endsWith(".png")) {
            file.delete()
        }
    }
}

fun clear_players(context: Context) {
    val fileOutput = context.openFileOutput("scores.txt", Context.MODE_PRIVATE)
    val fileOutput2 = context.openFileOutput("bot.txt", Context.MODE_PRIVATE)
    val fileOutput3 = context.openFileOutput("players.txt", Context.MODE_PRIVATE)

}
@Composable
fun VolumeSlider(context: Context,
    volume: Float,
    onVolumeChange: (Float) -> Unit, widthDp: Float, heightDp: Float
) {
    Column(
        modifier = Modifier.padding((16*widthDp/411).dp, (16*heightDp/914).dp)
    ) {
        Text(text = "Volume: ${(volume * 100).toInt()}%", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height((8*heightDp/914).dp))

        Slider(
            value = volume,
            onValueChange = onVolumeChange,
            valueRange = 0f..1f,
            steps = 9,
            modifier = Modifier.fillMaxWidth()
        )
    }
    editsettings(context = context,3,volume )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BattleShipsTheme {
        Greeting("Android")
    }
}
