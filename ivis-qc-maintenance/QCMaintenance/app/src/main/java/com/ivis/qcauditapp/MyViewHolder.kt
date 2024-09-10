package com.ivis.qcauditapp

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.qcauditapp.R

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  val nameTextView: TextView = itemView.findViewById(R.id.txtname)
  val status_check: ImageView = itemView.findViewById(R.id.status_check)
  val play_audio: ImageView = itemView.findViewById(R.id.play_audio)
}