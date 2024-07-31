package com.jayvideoplayer.data.test

import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyComposable() {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        IconButton(
            onClick = {
                Toast.makeText(context, "onClick", Toast.LENGTH_SHORT).show()
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "go back "
            )
        }
        Text(
            text = "hello", modifier = Modifier.combinedClickable(
                onClick = {
                    Toast.makeText(context, "Short click detected!", Toast.LENGTH_SHORT).show()
                },
                onLongClick = {
                    Toast.makeText(context, "Long click detected!", Toast.LENGTH_SHORT).show()

                }
            )
        )
    }
}


@Composable
fun YoutubePlayer(videoId: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl("https://www.youtube.com/")
            }
        },
        update = { webView ->
            webView.loadUrl("https://www.youtube.com/playlist?list=PL4fGSI1pDJn4pTWyM3t61lOyZ6_4jcNOw")
        }
    )
}