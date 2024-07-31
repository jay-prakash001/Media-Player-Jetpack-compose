package com.jayvideoplayer.presentation.viewModel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.jayvideoplayer.data.model.AudioFile
import com.jayvideoplayer.data.model.VideoFile
import com.jayvideoplayer.domain.repo.VideoAppRepo
import com.jayvideoplayer.presentation.utils.MYPlayer
import com.jayvideoplayer.presentation.utils.getUniqueImmediateParentNames
import com.jayvideoplayer.presentation.utils.getVideoThumbnail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    val repo: VideoAppRepo,
    private val application: Application = Application()
) :
    ViewModel() {

    val showUI = MutableStateFlow(false)
    val isLoading = MutableStateFlow(false)
    val videoList = MutableStateFlow(emptyList<VideoFile>())
    val audioList = MutableStateFlow(emptyList<AudioFile>())
    val parentDirectory = MutableStateFlow(emptyList<String>())
    val expoPlayer = MutableStateFlow(MYPlayer(exoPlayer = ExoPlayer.Builder(application).build(), isPlaying = MutableStateFlow(false)))
    private val splashShowFlow = MutableStateFlow(true)
    val isSplashShow = splashShowFlow.asStateFlow()
    val uri = MutableStateFlow("")
    val currentPosition = MutableStateFlow(0L)
    init {
        loadAllVideos()
        loadAllAudios()
        println("Exoplayer : ${expoPlayer.value.isPlaying.value}")
       parentDirectory.value =  getUniqueImmediateParentNames(setPath())
        viewModelScope.launch {
            delay(2000)
            splashShowFlow.value = false
        }
    }
    fun setPath(): ArrayList<String> {
        val listOfPaths = arrayListOf<String>()

        videoList.value.forEach {
            listOfPaths.add(it.path)
        }
        audioList.value.forEach {
            listOfPaths.add(it.path)
        }
        return listOfPaths
    }

    fun loadAllVideos() {
        isLoading.value = true

        viewModelScope.launch {
            repo.getAllVideos(application).collectLatest {
                videoList.value = it
            }
        }
        isLoading.value = false
    }

    fun loadAllAudios() {
        isLoading.value = true
        viewModelScope.launch {
            repo.getAllAudios(application).collectLatest {
                audioList.value = it
            }
        }
    }
    fun findSong(path : String):VideoFile{
        videoList.value.forEach {
            if (it.path.toString().equals(path.toString())){
                return  it
            }
        }
        return videoList.value[0]
    }
    fun findIndex(song: AudioFile) : Int{
        audioList.value.forEachIndexed { index, audioFile ->
            if (song.path.toString() == audioFile.path.toString()){
                return index
            }
        }
        return  0
    }

    fun getThumbnail(context: Context, uri: Uri) : Bitmap?{
        var bitmap : Bitmap? = null
        viewModelScope.launch {
         bitmap=    getVideoThumbnail(context , uri )
        }
        return  bitmap
    }



}