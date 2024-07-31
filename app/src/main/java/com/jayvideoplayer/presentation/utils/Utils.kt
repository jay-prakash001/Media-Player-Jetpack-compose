package com.jayvideoplayer.presentation.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.painter.Painter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.jayvideoplayer.data.model.AudioFile
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File


fun getUniqueImmediateParentNames(filePaths: List<String>): List<String> {
    val parentNames = filePaths.mapNotNull { filePath ->
        val file = File(filePath)
        file.parentFile?.name
    }.toSet() // Convert the list to a set to remove duplicates

    return parentNames.toList()
}

suspend fun getVideoThumbnail(context: Context, videoUri: Uri): Bitmap? {
    return try {
        ThumbnailUtils.createVideoThumbnail(videoUri.path!!, MediaStore.Video.Thumbnails.MINI_KIND)
    } catch (e: Exception) {
        null
    }
}
@SuppressLint("DefaultLocale")
fun formatLongToHHMMSS(timeInMillis: Long): String {
    val totalSeconds = timeInMillis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
@RequiresApi(Build.VERSION_CODES.Q)
fun getSongThumbnail(context: Context, songId: Long): Bitmap? {
    val contentResolver = context.contentResolver
    val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId)
    return try {
        contentResolver.loadThumbnail(uri, Size(300, 300), null)
    } catch (e: Exception) {
        null
    }
}

data class Category(val name: String, val filesCount: Int, val img: Painter)

data class MYPlayer(
    val exoPlayer: ExoPlayer,
    val isPlaying: MutableStateFlow<Boolean> = MutableStateFlow(true),
    val duration: MutableStateFlow<Long> = MutableStateFlow(0L),
    val currentSong: MutableStateFlow<Int> = MutableStateFlow(0),
    val previousSong: MutableStateFlow<Int> = MutableStateFlow(0),
    val nextSong: MutableStateFlow<Int> = MutableStateFlow(0)
) {

    fun setMediaItem(path: String) {
        exoPlayer.pause()
        exoPlayer.setMediaItem(MediaItem.fromUri(path))
        exoPlayer.play()
        isPlaying.value = true
        exoPlayer.seekTo(0)

    }

    fun setMediaItem(audioFile: AudioFile) {
        exoPlayer.setMediaItem(MediaItem.fromUri(audioFile.path))
        exoPlayer.play()
        isPlaying.value = true

    }

    fun setMediaItem(index: Int, audioList: List<AudioFile>, play : Boolean = true) {
        exoPlayer.setMediaItem(MediaItem.fromUri(audioList[index].path))

if (play){
    exoPlayer.play()
    isPlaying.value = true
}
    }

}





//    val permissions = arrayOf(
//        Manifest.permission.READ_MEDIA_VIDEO,
//        Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE
//    )
//    val mediaPermission = rememberPermissionState(permission = permissions[1])
//    val mediaPermissionLauncher =
//        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
//            if (it) {
//                viewModel.showUI.value = true
//            } else {
//                viewModel.showUI.value = false
//
//            }
//        }
//
//    if (!viewModel.showUI.collectAsState().value) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Image(painter = painterResource(id = R.drawable.error), contentDescription ="Permission denied" )
//            Spacer(modifier = Modifier.height(10.dp))
//            Text(text = "permission required", fontSize = 30.sp, fontWeight = FontWeight.SemiBold)
//        }
//    } else {
//        AppNavigation()
//    }
//    LaunchedEffect(key1 = mediaPermission) {
//        if (!mediaPermission.status.isGranted) {
//            mediaPermissionLauncher.launch(mediaPermission.permission)
//        } else {
//            viewModel.showUI.value = true
//        }
//    }