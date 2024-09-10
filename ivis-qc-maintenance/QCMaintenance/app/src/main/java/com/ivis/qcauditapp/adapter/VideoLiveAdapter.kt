package com.ivis.qcauditapp.adapter

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ivis.qcauditapp.MyViewHolder
import com.ivis.qcauditapp.models.Camera
import com.ivis.qcauditapp.retrofit.OnClickListener
import com.qcauditapp.R


class VideoLiveAdapter(
    private val menuItemsList: MutableList<Camera>,
    private val itemClickListener: OnClickListener,
    private val context: Context
) : RecyclerView.Adapter<MyViewHolder>() {

    private val handler = Handler()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.live_video_adapter, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val menuItem = menuItemsList[position]
        if(menuItem.status.equals("Connected")) {
            holder.play_audio.setImageResource(R.drawable.correct)
           holder.nameTextView.setText("Connected - "+menuItem.cameraId)
        }else{
            holder.play_audio.setImageResource(R.drawable.remove)
            holder.nameTextView.setText("Disconnected - "+menuItem.cameraId)

        }
        handler.post(createUpdateImageUrlRunnable(holder, menuItem))
    }

    private fun createUpdateImageUrlRunnable(holder: MyViewHolder, menuItem: Camera): Runnable {
        return object : Runnable {
            override fun run() {
                // Fetch the updated imageUrl from your data source
                val updatedImageUrl = getUpdatedImageUrl(menuItem)

                if (updatedImageUrl.isNotEmpty()) {
                    Glide.with(context)
                        .load(updatedImageUrl)
                        .placeholder(R.drawable.logo) // Replace with your placeholder image
                        .dontAnimate() // Disable animation if needed
                        .into(holder.status_check)
                }

                // Repeat the process after a delay
                handler.postDelayed(this, 10000)
            }
        }
    }

    // Function to simulate getting an updated image URL
    private fun getUpdatedImageUrl(menuItem: Camera): String {
        // Replace this with your logic to fetch the updated imageUrl from your data source
        // For demonstration, appending a random number to the existing URL
        return "${menuItem.cameraSnapshotUrl}?timestamp=${System.currentTimeMillis()}"
    }

    override fun getItemCount(): Int {
        return menuItemsList.size
    }

    // Ensure that the handler is stopped when the adapter is detached from the RecyclerView
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        handler.removeCallbacksAndMessages(null)
        super.onDetachedFromRecyclerView(recyclerView)
    }
    fun stopBackgroundTasks() {
        handler.removeCallbacksAndMessages(null)
    }
}




