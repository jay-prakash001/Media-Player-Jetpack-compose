package com.jayvideoplayer.domain.repo

import android.app.Application
import android.content.Context
import com.jayvideoplayer.data.model.AudioFile
import com.jayvideoplayer.data.model.VideoFile
import kotlinx.coroutines.flow.Flow


interface VideoAppRepo {
    suspend fun getAllVideos(application: Application): Flow<ArrayList<VideoFile>>

    suspend fun getAllAudios(application: Application): Flow<ArrayList<AudioFile>>
}