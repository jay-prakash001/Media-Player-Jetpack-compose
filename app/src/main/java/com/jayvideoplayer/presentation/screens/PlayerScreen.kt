package com.jayvideoplayer.presentation.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import androidx.palette.graphics.Palette
import com.jayvideoplayer.R
import com.jayvideoplayer.presentation.utils.Duration
import com.jayvideoplayer.presentation.utils.PlayerController
import com.jayvideoplayer.presentation.utils.Video
import com.jayvideoplayer.presentation.utils.getSongThumbnail
import com.jayvideoplayer.presentation.viewModel.MyViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(navController: NavHostController, viewModel: MyViewModel) {
    ExoPlayerView(viewModel = viewModel, navController = navController)

}


@Composable
fun ExoPlayerView(viewModel: MyViewModel, navController: NavHostController) {

    val exoPlayer = viewModel.expoPlayer.collectAsState().value
    val configuration = LocalConfiguration.current
    var modifier by remember { mutableStateOf(Modifier.fillMaxSize()) }
    var showTitle by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        exoPlayer.setMediaItem(viewModel.uri.value)
        exoPlayer.exoPlayer.prepare()
        exoPlayer.exoPlayer.playWhenReady = true
        exoPlayer.exoPlayer.seekTo(viewModel.currentPosition.value)


    }
    var showFileName by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(configuration.orientation) {
        showTitle = true
        coroutineScope.launch {
            delay(2000)
            showTitle = false
        }
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            showFileName = false
            modifier = Modifier.fillMaxSize()
        } else {
            showFileName = true
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        }
    }

    DisposableEffect(Unit) {

        onDispose {
            exoPlayer.exoPlayer.pause()

            if (viewModel.currentPosition.value < exoPlayer.exoPlayer.currentPosition && exoPlayer.exoPlayer.currentPosition > 0) {
                viewModel.currentPosition.value = exoPlayer.exoPlayer.currentPosition

            }

            viewModel.expoPlayer.value.isPlaying.value = false
            viewModel.expoPlayer.value.setMediaItem(0, viewModel.audioList.value, false)

        }

    }


    Column(modifier = Modifier
        .fillMaxSize()
        .clickable {
            showTitle = true
            coroutineScope.launch {
                delay(2000)
                showTitle = false
            }
        }) {



        val context = LocalContext.current
        Box(modifier = modifier.clickable {
            Toast.makeText(context, "PlayerView clicked!", Toast.LENGTH_SHORT).show()
        }) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer.exoPlayer
                    }
                },
                modifier = modifier
            )

            if (showTitle) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable {
                            showTitle = true
                            coroutineScope.launch {
                                delay(2000)
                                showTitle = false
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    IconButton(onClick = {
                        navController.navigateUp()

                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "go back "
                        )
                    }
                    Text(
                        text = viewModel.findSong(viewModel.uri.collectAsState().value).title.toString()
                            .capitalize(Locale.ROOT)
                    )
                }
            }
        }


        val videos = viewModel.videoList.collectAsState().value
        LazyColumn(modifier = Modifier.padding(5.dp)) {

            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "go back "
                        )
                    }
                    Text(
                        text = viewModel.findSong(viewModel.uri.collectAsState().value).title.toString()
                            .capitalize(Locale.ROOT)
                    )
                }
            }
            items(videos) {

                Video(
                    video = it,
                    viewModel = viewModel,
                    modifier = if (it.id == viewModel.findSong(viewModel.uri.collectAsState().value).id) {
                        Modifier.border(
                            2.dp, MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(14.dp)
                        )
                    } else {
                        Modifier
                    }
                ) {
                    viewModel.uri.value = it.path
                    exoPlayer.setMediaItem(viewModel.uri.value)
                    viewModel.currentPosition.value = 0

                }


            }

        }
    }
}


@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AudioPlayerScreen(
    modifier: Modifier = Modifier,
    index: Int = 0,
    viewModel: MyViewModel,
    navController: NavHostController
) {
    val list = viewModel.audioList.collectAsState().value
    val exoPlayer = viewModel.expoPlayer.collectAsState().value
    val isPlaying = exoPlayer.isPlaying.collectAsState().value

    LaunchedEffect(Unit) {
        exoPlayer.currentSong.value = index
        exoPlayer.setMediaItem(exoPlayer.currentSong.value, list)
        exoPlayer.exoPlayer.prepare()
        exoPlayer.exoPlayer.playWhenReady = true
        exoPlayer.isPlaying.value = true

    }
    val context = LocalContext.current
    val song = list[exoPlayer.currentSong.collectAsState().value]
    val thumbnail: Bitmap? = song.id?.let { getSongThumbnail(context, it.toLong()) }
    val defaultColor = MaterialTheme.colorScheme.surface
    val dominantColor = remember(thumbnail) {
        if (thumbnail != null) {
            Palette.from(thumbnail).generate().getDominantColor(defaultColor.toArgb())

        } else {
            defaultColor.toArgb()
        }
    }
    val blendedColor = Color(dominantColor).copy(alpha = 0.5f)

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                if (song.title.toString().length > 30) {
                    song.title.toString().substring(0, 30) + "..."
                } else {
                    song.title.toString()
                }.capitalize(Locale.ROOT),
                style = MaterialTheme.typography.titleLarge
            )
        }, navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "go back ", modifier = Modifier.clickable {
                    navController.navigateUp()
                }
            )
        })
    }) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.surface),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(modifier = Modifier
                .padding(10.dp)
                .size(400.dp)
                .background(MaterialTheme.colorScheme.surface)
                .drawBehind {

                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                defaultColor,
                                blendedColor,
                                blendedColor,
                                defaultColor
                            ),
                            center = Offset(size.width / 2, size.height / 2),
                            radius = (size.width / 2)
                        ),
                        size = Size(size.width, size.height) // Make it a square
                    )
                }
                .padding(20.dp), contentAlignment = Alignment.Center) {
                if (thumbnail != null) {
                    Image(
                        bitmap = thumbnail.asImageBitmap(),
                        contentDescription = "Thumbnail",
                        modifier = Modifier
                            .size(200.dp)
                            .alpha(.8f)
                            .clip(RoundedCornerShape(20.dp))
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.song),
                        contentDescription = "",
                        modifier = Modifier
                            .size(200.dp)

                    )
                }
            }
            var currentPosition by remember { mutableLongStateOf(0L) }
            var duration by remember { mutableLongStateOf(0L) }
            LaunchedEffect(key1 = exoPlayer) { // Update when exoPlayer changes
                while (true) {
                    currentPosition = exoPlayer.exoPlayer.currentPosition
                    duration = exoPlayer.exoPlayer.duration
                    delay(100) // Update every 1000ms (1 second)
                    if (duration > 0 && currentPosition >= duration - 100) {
                        if (exoPlayer.currentSong.value < viewModel.audioList.value.size - 1) {
                            exoPlayer.currentSong.value++
                        } else {
                            exoPlayer.currentSong.value = 0
                        }
                        exoPlayer.setMediaItem(
                            exoPlayer.currentSong.value,
                            viewModel.audioList.value
                        )
                    }
                }
            }

            AnimatedContent(targetState = currentPosition, label = "aa") {
                Text(
                    if (song.title.toString().length > 30) {
                        song.title.toString().substring(0, 30) + "..."
                    } else {
                        song.title.toString()
                    }.capitalize(Locale.ROOT),
                    style = MaterialTheme.typography.titleLarge
                )

            }
            Duration(currentPosition = currentPosition, duration = duration)

            // Check if duration is valid before setting valueRange
            if (duration > 0) {
                Slider(
                    value = currentPosition.toFloat(),
                    valueRange = 0f..duration.toFloat(),
                    onValueChange = { newPosition ->
                        exoPlayer.exoPlayer.seekTo(newPosition.toLong())
                    },
                    modifier = Modifier
                        .fillMaxWidth(.9f)
                        .background(MaterialTheme.colorScheme.surface),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Magenta,
                        activeTrackColor = Color.Magenta,
                        inactiveTrackColor = Color.DarkGray,
                    ),
                )
            } else {
                // Handle invalid duration (e.g., show a loading indicator or disable the slider)
                Text("Loading...")
            }
            PlayerController(viewModel)
        }
    }
}

