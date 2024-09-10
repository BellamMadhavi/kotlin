package com.example.newwowzkidsworld.livestream

import android.content.Context
import android.net.Uri
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout

class VideoPlayer(context: Context) {

    private val libVLC = LibVLC(context)
    private val mediaPlayer = MediaPlayer(libVLC)

    fun attachView(videoLayout: VLCVideoLayout) {
        mediaPlayer.attachViews(videoLayout, null, false, false)
    }

    fun playStream(streamUrl: String) {
        val media = Media(libVLC, Uri.parse(streamUrl))
        mediaPlayer.media = media
        mediaPlayer.play()
    }

    fun release() {
        mediaPlayer.release()
        libVLC.release()
    }
}
