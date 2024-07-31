package com.jayvideoplayer.presentation.navigation

import kotlinx.serialization.Serializable

sealed class NavItems {
    @Serializable
    object Home

    @Serializable
//    data class PlayerScreen(val videoUri: String)
    object PlayerScreen

 @Serializable
    data class AudioPlayerScreen(val index: Int)

    @Serializable
    object SongsListScreen

    @Serializable
    object VideoListScreen
    @Serializable
    data class DirFileListScreen(val dir:String)
}