package com.jayvideoplayer.presentation.screens

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jayvideoplayer.presentation.navigation.AppNavigation
import com.jayvideoplayer.presentation.utils.ErrorPermissions
import com.jayvideoplayer.presentation.viewModel.MyViewModel


@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun App(modifier: Modifier = Modifier, viewModel: MyViewModel = hiltViewModel()) {

    val permissions = arrayOf(
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_AUDIO
    )

    val mediaPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val areAllPermissionsGranted = permissions.all { it.value }
        viewModel.showUI.value = areAllPermissionsGranted
    }
    if (viewModel.isSplashShow.collectAsState().value) {
        SplashScreen()
    } else {
        if (!viewModel.showUI.collectAsState().value) {
            ErrorPermissions()
        } else {
            AppNavigation()
        }
    }

    LaunchedEffect(key1 = true) { // Use a different key to trigger only once
        mediaPermissionLauncher.launch(permissions)
    }


}
