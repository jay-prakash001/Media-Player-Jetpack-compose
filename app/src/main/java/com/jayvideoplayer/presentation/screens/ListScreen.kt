package com.jayvideoplayer.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.jayvideoplayer.presentation.navigation.NavItems
import com.jayvideoplayer.presentation.utils.BottomPlayerController
import com.jayvideoplayer.presentation.utils.Song
import com.jayvideoplayer.presentation.utils.Video
import com.jayvideoplayer.presentation.viewModel.MyViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirFileListScreen(
    modifier: Modifier = Modifier,
    dir: String,
    navController: NavHostController,
    viewModel: MyViewModel,

    ) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = dir.capitalize(Locale.ROOT)) }, navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "go back ", modifier = Modifier.clickable {
                    navController.navigateUp()
                }
            )
        })
    }, bottomBar = {


        BottomPlayerController(viewModel, navController)


    }
    ) {
        val songs = viewModel.audioList.collectAsState().value.filter { it.path.contains(dir) }
        val videos = viewModel.videoList.collectAsState().value.filter { it.path.contains(dir) }
        LazyColumn(modifier = Modifier.padding(it)) {
            items(songs) {
                Song(song = it) {
                    navController.navigate(NavItems.AudioPlayerScreen(viewModel.findIndex(it)))
                }
            }
            items(videos) {
                Video(video = it, viewModel = viewModel) {
                    viewModel.uri.value = it.path
                    navController.navigate(NavItems.PlayerScreen)

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongsListScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MyViewModel
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "All Songs") }, navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "go back ", modifier = Modifier.clickable {
                    navController.navigateUp()
                }
            )
        })
    }, bottomBar = {

        BottomPlayerController(viewModel, navController)

    }
    ) {
        val songs = viewModel.audioList.collectAsState().value
        LazyColumn(modifier = Modifier.padding(it)) {

            itemsIndexed(songs) { index, it ->
                Song(song = it) {
                    navController.navigate(NavItems.AudioPlayerScreen(index))
                }
            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoListScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MyViewModel
) {

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "All Videos") }, navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "go back ", modifier = Modifier.clickable {
                    navController.navigateUp()
                }
            )
        })
    }
    ) { it ->
        val videos = viewModel.videoList.collectAsState().value
        LazyColumn(modifier = Modifier.padding(it)) {
            items(videos) {
                Video(video = it, viewModel = viewModel) {
                    viewModel.uri.value = it.path
                    navController.navigate(NavItems.PlayerScreen)
                    viewModel.currentPosition.value = 0
                }

            }

        }
    }
}

