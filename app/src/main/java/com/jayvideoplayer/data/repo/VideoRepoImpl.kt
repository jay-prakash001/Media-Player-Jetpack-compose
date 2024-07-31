package com.jayvideoplayer.data.repo

import android.app.Application
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import com.jayvideoplayer.data.model.AudioFile
import com.jayvideoplayer.data.model.VideoFile
import com.jayvideoplayer.domain.repo.VideoAppRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class VideoRepoImpl : VideoAppRepo {



    @RequiresApi(Build.VERSION_CODES.O)
  override suspend fun getAllVideos(application: Application): Flow<ArrayList<VideoFile>> {
        val allVideo = arrayListOf<VideoFile>()
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED,


        )
        Log.d("URI", "getAllVideos: $projection")
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val memoryCursor = application.contentResolver.query(uri, projection, null, null)


        if (memoryCursor != null) {
            while (memoryCursor.moveToNext()) {
                val id = memoryCursor.getString(0)
                val title = memoryCursor.getString(1)
                val path = memoryCursor.getString(2)
                val duration = memoryCursor.getString(3)
                val fileName = memoryCursor.getString(4)
                val size = memoryCursor.getString(5)
                val date = memoryCursor.getString(6)

                val video = VideoFile(
                    id = id,
                    title = title,
                    path = path,
                    duration = duration,
                    fileName = fileName,
                    size = size,
                    date = date
                )

                Log.d("Video", "getAllVideos: $video")
                allVideo.add(video)
                if (memoryCursor.isLast) {
                    break
                }

            }
            memoryCursor.close()
        }
        return flow {
            emit(allVideo)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getAllAudios(application: Application): Flow<ArrayList<AudioFile>> {

    val allAudios = arrayListOf<AudioFile>()

        val projection  = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DATE_ADDED
        )
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val memoryCursor = application.contentResolver.query(uri,projection,null,null)

        if (memoryCursor != null) {
            while (memoryCursor.moveToNext()){
                val audioFile = AudioFile(
                    id = memoryCursor.getString(0),
                    title = memoryCursor.getString(1),
                    path = memoryCursor.getString(2),
                    duration = memoryCursor.getString(3),
                    fileName = memoryCursor.getString(4),
                    size = memoryCursor.getString(5),
                    date = memoryCursor.getString(6),
                )

                allAudios.add(audioFile)
                if (memoryCursor.isLast){
                    break
                }
            }

                memoryCursor.close()
        }
        return flow {
            emit(allAudios)
        }
    }


}