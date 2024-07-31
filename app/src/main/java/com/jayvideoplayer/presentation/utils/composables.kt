package com.jayvideoplayer.presentation.utils

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import com.jayvideoplayer.R
import com.jayvideoplayer.data.model.AudioFile
import com.jayvideoplayer.data.model.VideoFile
import com.jayvideoplayer.presentation.navigation.NavItems
import com.jayvideoplayer.presentation.viewModel.MyViewModel
import java.util.Locale
import kotlin.random.Random

@Composable
fun Directory(directory: String = "Camera", navController: NavHostController, onClick: () -> Unit) {
    val randomColor: Color = Color(
        red = Random.nextInt(256),
        green = Random.nextInt(256),
        blue = Random.nextInt(256)
    )
    Row(modifier = Modifier
        .clickable {
            onClick()

        }
        .fillMaxWidth()
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start) {
        Icon(
            painter = painterResource(id = R.drawable.dir),
            contentDescription = "dir icon",
            tint = randomColor,
            modifier = Modifier
                .padding(5.dp)
                .size(60.dp)
        )

        Column {

            Text(text = directory.capitalize(Locale.ROOT))
            Divider()
        }
    }


}

@Composable
fun Song(modifier: Modifier = Modifier, song: AudioFile, onClick: () -> Unit) {
    Row(modifier = Modifier
        .clickable {
            onClick()

        }
        .fillMaxWidth()
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start) {
        Image(
            painter = painterResource(id = R.drawable.song),
            contentDescription = "song icon",
            modifier = Modifier
                .padding(5.dp)
                .size(50.dp)
        )

        Column {

            song.title?.let { Text(text = it.capitalize(Locale.ROOT)) }
            Divider()
        }
    }
}

@Composable
fun Video(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel,
    video: VideoFile,
    onClick: () -> Unit
) {

    Row(modifier = modifier
        .clickable {
            onClick()

        }
        .fillMaxWidth()
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start) {
        Image(
            painter = painterResource(id = R.drawable.video),
            contentDescription = "video icon",
            modifier = Modifier
                .padding(5.dp, 15.dp)
                .size(50.dp)
        )

        Column {

            video.title?.let { Text(text = it.capitalize(Locale.ROOT)) }
            Divider()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    category: Category = Category("Recent", 10, painterResource(id = R.drawable.app)),
    onClick: () -> Unit,
) {
    Card(
        onClick = { onClick() },
        modifier = Modifier.padding(10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.inverseOnSurface),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .height(140.dp)
                .width(160.dp)
                .padding(5.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = category.img,
                contentDescription = category.name,
                modifier = Modifier.size(50.dp)
            )
            Text(
                text = category.name.capitalize(Locale.ROOT),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Text(
                text = "${category.filesCount} Files",
                fontSize = 18.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
fun Categories(context: Context, categories: List<Category>, navController: NavHostController) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 130.dp)
    ) {
        items(categories) { cat ->
            CategoryCard(category = cat) {
                if (cat.name.contains("songs", ignoreCase = true)) {
                    navController.navigate(NavItems.SongsListScreen)
                } else if (cat.name.contains("video", ignoreCase = true)) {
                    navController.navigate(NavItems.VideoListScreen)
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesRow(context: Context, categories: List<Category>, navController: NavHostController) {

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(categories) { cat ->
            Card(onClick = {
                if (cat.name.contains("songs", ignoreCase = true)) {
                    navController.navigate(NavItems.SongsListScreen)
                } else if (cat.name.contains("video", ignoreCase = true)) {
                    navController.navigate(NavItems.VideoListScreen)
                }
            }, modifier =  Modifier.height(50.dp).padding(5.dp)) {
                Row(
                    modifier = Modifier.padding(15.dp,5.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = cat.img, contentDescription = cat.name.toString(),modifier = Modifier.size(25.dp ))
                    Text(text = cat.name.toString()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })


                }

            }

        }
    }
}


@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun PlayerController(

    viewModel: MyViewModel
) {
    val exoPlayer = viewModel.expoPlayer.collectAsState().value
    val isPlaying by exoPlayer.isPlaying.collectAsState()
    val icon = if (isPlaying) R.drawable.pause else R.drawable.play

    Card(
        modifier = Modifier
            .padding(5.dp)
            .border(
                1.dp, MaterialTheme.colorScheme.inverseOnSurface,
                RoundedCornerShape(14.dp)
            ), colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp)
    ) {


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = {
                if (0 < exoPlayer.currentSong.value) {
                    exoPlayer.currentSong.value = --exoPlayer.currentSong.value
                } else {
                    exoPlayer.currentSong.value = viewModel.audioList.value.size - 1
                }
                exoPlayer.setMediaItem(exoPlayer.currentSong.value, viewModel.audioList.value)
                exoPlayer.isPlaying.value = true

            }) {
                Icon(
                    painter = painterResource(id = R.drawable.fastrewind),
                    contentDescription = "Skip to previous"
                )
            }

            IconButton(onClick = {
                exoPlayer.exoPlayer.seekTo(exoPlayer.exoPlayer.currentPosition - 10000)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.tenback),
                    contentDescription = "back 10 second"
                )
            }
            IconButton(onClick = {
                exoPlayer.isPlaying.value = !exoPlayer.isPlaying.value
                if (exoPlayer.isPlaying.value) {
                    exoPlayer.exoPlayer.play()
                } else {
                    exoPlayer.exoPlayer.pause()
                }

            }, modifier = Modifier.size(60.dp)) {

                Image(
                    painter = painterResource(id = icon),
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    modifier = Modifier.size(50.dp)
                )

            }
            IconButton(onClick = {
                if (exoPlayer.exoPlayer.duration < exoPlayer.exoPlayer.currentPosition + 10000) {
                    if (exoPlayer.currentSong.value < viewModel.audioList.value.size - 1) {
                        exoPlayer.currentSong.value = ++exoPlayer.currentSong.value
                    } else {
                        exoPlayer.currentSong.value = 0
                    }
                    exoPlayer.setMediaItem(exoPlayer.currentSong.value, viewModel.audioList.value)

                } else {
                    exoPlayer.exoPlayer.seekTo(exoPlayer.exoPlayer.currentPosition + 10000)
                }

            }) {
                Image(
                    painter = painterResource(id = R.drawable.tenforward),
                    contentDescription = "forward 10 second"
                )
            }
            IconButton(onClick = {
                if (exoPlayer.currentSong.value < viewModel.audioList.value.size - 1) {
                    exoPlayer.currentSong.value = ++exoPlayer.currentSong.value
                } else {
                    exoPlayer.currentSong.value = 0
                }
                exoPlayer.setMediaItem(exoPlayer.currentSong.value, viewModel.audioList.value)
                exoPlayer.isPlaying.value = true

            }) {
                Icon(
                    painter = painterResource(id = R.drawable.fastforward),
                    contentDescription = "Skip to previous"
                )
            }
        }
    }
}


@Composable
fun BottomPlayerController(
    viewModel: MyViewModel,
    navController: NavHostController
) {
    val exoPlayer = viewModel.expoPlayer.collectAsState().value

    LaunchedEffect(Unit) {
        println("EXo ${exoPlayer.exoPlayer.duration}")
        if (!exoPlayer.isPlaying.value) {
            exoPlayer.exoPlayer.pause()

        }
        if (exoPlayer.exoPlayer.duration <= 0L) {
            exoPlayer.setMediaItem(exoPlayer.currentSong.value, viewModel.audioList.value)

        }

    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(NavItems.AudioPlayerScreen(viewModel.expoPlayer.value.currentSong.value))
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val title =
            viewModel.audioList.collectAsState().value[viewModel.expoPlayer.collectAsState().value.currentSong.collectAsState().value].title.toString()

        Text(
            text = if (title.length > 45) {
                title.substring(0, 45) + "..."
            } else {
                title
            }.capitalize(Locale.ROOT)
        )
        PlayerController(viewModel = viewModel)
    }
}


@Composable
fun ErrorPermissions() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.error),
            contentDescription = "Permission denied"
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "permission required", fontSize = 30.sp, fontWeight = FontWeight.SemiBold)
    }
}


@Composable
fun Duration(currentPosition: Long, duration: Long) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(text = formatLongToHHMMSS(currentPosition))
        Text(text = formatLongToHHMMSS(duration))
    }
}

