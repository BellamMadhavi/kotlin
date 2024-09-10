package com.ivis.qcauditapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.ivis.qcauditapp.models.AudioData
import com.ivis.qcauditapp.retrofit.OnClickListener
import com.qcauditapp.R

class AudioAdapter(private val menuItemsList: MutableList<AudioData>,
                   private val itemClickListener: OnClickListener
) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.audio_adapter_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val menuItem = menuItemsList[position]

        if(menuItem.Audio_Type.equals("Crowd Audio Clip") || menuItem.Audio_Type.equals("Helmet Audio Clip")){
      //  holder.play_audio.visibility=View.VISIBLE
       // holder.main_layout.visibility=View.VISIBLE
            holder.nameTextView.text = "Play "+menuItem.Audio_Type
        }else {
            holder.nameTextView.text = "Test "+menuItem.Audio_Type
         // holder.play_audio.visibility=View.INVISIBLE
       //   holder.main_layout.visibility=View.GONE

        }
        if(menuItem.Type.equals("Unchecked")){
            holder.status_check_no.setImageResource(R.drawable.full_circle)
            holder.status_check_yes.setImageResource(R.drawable.bg_textview_cricle)
        }else{
            holder.status_check_no.setImageResource(R.drawable.bg_textview_cricle)
            holder.status_check_yes.setImageResource(R.drawable.full_circle)
        }
        holder.status_check_no.setOnClickListener {

            itemClickListener.onAudioItemClick(position,true)
        }
        holder.status_check_yes.setOnClickListener {
            itemClickListener.onAudioItemClick(position,true)
        }
        holder.nameTextView.setOnClickListener {
            itemClickListener.PlayAudioApi(position)
        }
    }

    override fun getItemCount(): Int {
        return menuItemsList.size
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

     val nameTextView: TextView = itemView.findViewById(R.id.txtname)
     val status_check_yes: ImageView = itemView.findViewById(R.id.status_check_yes)
     val status_check_no: ImageView = itemView.findViewById(R.id.status_check_no)
     val play_audio: ImageView = itemView.findViewById(R.id.play_audio)
     val main_layout: LinearLayout = itemView.findViewById(R.id.main_layout)


}
