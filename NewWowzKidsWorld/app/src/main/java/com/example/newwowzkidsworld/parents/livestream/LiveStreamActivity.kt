package com.example.newwowzkidsworld.parents.livestream

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout
import com.example.newwowzkidsworld.R
import org.videolan.libvlc.Media

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.newwowzkidsworld.livestream.VideoPlayer

class LiveStreamActivity : AppCompatActivity() {

    private lateinit var libVLC: LibVLC
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var videoLayout: VLCVideoLayout
    private lateinit var thumbnailImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val className = intent.getStringExtra(EXTRA_CLASS_NAME) ?: ""
        val sectionName = intent.getStringExtra(EXTRA_SECTION_NAME) ?: ""
        val streamUrl = intent.getStringExtra(EXTRA_STREAM_URL) ?: ""
        var thumbnailResId = intent.getIntExtra(EXTRA_THUMBNAIL_RES_ID, R.drawable.live1)

        // Fallback to a default thumbnail if the provided thumbnail is invalid
        if (thumbnailResId == 0 || thumbnailResId == android.R.color.black) {
            thumbnailResId = R.drawable.live1
        }
        setContent {
            LiveStreamScreen(className, sectionName, streamUrl, thumbnailResId)
        }
    }

    @Composable
    fun LiveStreamScreen(className: String, sectionName: String, streamUrl: String, thumbnailResId: Int) {
        var isStreamPlaying by remember { mutableStateOf(false) } // State to track if the stream is playing
        // Initialize VLC components within the composable scope
        val context = LocalContext.current
        val libVLC = remember { LibVLC(context) }
        val mediaPlayer = remember { MediaPlayer(libVLC) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Display the class and section names at the top
            Text(
                text = "$className - $sectionName",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display the thumbnail while the live stream initializes
            Box(modifier = Modifier.fillMaxSize()) {
                if (!isStreamPlaying) {
                    // Show the thumbnail until the stream starts
                    Image(
                        painter = painterResource(id = thumbnailResId),
                        contentDescription = "Thumbnail",
                        modifier = Modifier.fillMaxWidth() // Adjust as needed to maintain aspect ratio
                    )
                }

                // Initialize and display the live stream
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp), // Adjust height as needed
                    factory = { context ->
                        val vlcVideoLayout = VLCVideoLayout(context)
                        val media = Media(libVLC, Uri.parse(streamUrl)).apply {
                            setEventListener { event ->
                                if (event.type == MediaPlayer.Event.Playing) {
                                    isStreamPlaying = true // Hide the thumbnail when the stream starts
                                }
                            }
                        }
                        mediaPlayer.media = media
                        mediaPlayer.attachViews(vlcVideoLayout, null, false, false)
                        mediaPlayer.play()
                        vlcVideoLayout
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        libVLC.release()
    }

    companion object {
        private const val EXTRA_CLASS_NAME = "class_name"
        private const val EXTRA_SECTION_NAME = "section_name"
        private const val EXTRA_STREAM_URL = "stream_url"
        private const val EXTRA_THUMBNAIL_RES_ID = "thumbnail_res_id"

        fun start(context: Context, className: String, sectionName: String, streamUrl: String, thumbnailResId: Int) {
            val intent = Intent(context, LiveStreamActivity::class.java).apply {
                putExtra(EXTRA_CLASS_NAME, className)
                putExtra(EXTRA_SECTION_NAME, sectionName)
                putExtra(EXTRA_STREAM_URL, streamUrl)
                putExtra(EXTRA_THUMBNAIL_RES_ID, thumbnailResId)
            }
            context.startActivity(intent)
        }
    }
}