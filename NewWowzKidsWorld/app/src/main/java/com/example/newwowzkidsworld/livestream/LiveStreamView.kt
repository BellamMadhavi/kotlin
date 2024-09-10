package com.example.newwowzkidsworld.livestream

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.videolan.libvlc.util.VLCVideoLayout

@Composable
fun LiveStreamView(
    modifier: Modifier = Modifier,
    streamUrl: String,
    thumbnailResId: Int
) {
    var videoPlayer: VideoPlayer? by remember { mutableStateOf(null) }

    DisposableEffect(Unit) {
        onDispose {
            videoPlayer?.release()
        }
    }

    Column(modifier = modifier) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            factory = { context ->
                val vlcVideoLayout = VLCVideoLayout(context)
                videoPlayer = VideoPlayer(context).apply {
                    attachView(vlcVideoLayout)
                    playStream(streamUrl)
                }
                vlcVideoLayout
            }
        )
    }
}


