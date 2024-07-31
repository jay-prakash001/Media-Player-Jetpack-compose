package com.jayvideoplayer.presentation.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.jayvideoplayer.presentation.screens.AudioPlayerScreen
import com.jayvideoplayer.presentation.screens.DirFileListScreen
import com.jayvideoplayer.presentation.screens.HomeScreen
import com.jayvideoplayer.presentation.screens.PlayerScreen
import com.jayvideoplayer.presentation.screens.SongsListScreen
import com.jayvideoplayer.presentation.screens.VideoListScreen
import com.jayvideoplayer.presentation.viewModel.MyViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AppNavigation(modifier: Modifier = Modifier, viewModel: MyViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavItems.Home) {

        composable<NavItems.Home> {
            HomeScreen(navController = navController)
        }


        composable<NavItems.DirFileListScreen> {
            val dir = it.toRoute<NavItems.DirFileListScreen>().dir
            DirFileListScreen(dir = dir, navController = navController, viewModel = viewModel)
        }
        composable<NavItems.PlayerScreen> {

            Log.d("URI", "videoUri ${viewModel.uri.value}")
            PlayerScreen(navController = navController, viewModel = viewModel)
        }
        composable<NavItems.SongsListScreen> {
            SongsListScreen(navController = navController, viewModel =viewModel )
        }
        composable<NavItems.VideoListScreen> {
            VideoListScreen(navController = navController, viewModel =viewModel )
        }

        composable<NavItems.AudioPlayerScreen> {
            val index = it.toRoute<NavItems.AudioPlayerScreen>().index
            AudioPlayerScreen( index = index, viewModel = viewModel,navController = navController)
        }

    }
}

