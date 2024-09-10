package com.ivis.qcauditapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivis.qcauditapp.MyViewHolder
import com.ivis.qcauditapp.models.AudioData
import com.ivis.qcauditapp.models.TwoWaySimStatus
import com.ivis.qcauditapp.retrofit.OnClickListener
import com.qcauditapp.R

class TwowayCallAdapter(private val menuItemsList: MutableList<TwoWaySimStatus>,private val itemClickListener: OnClickListener) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.two_way_adapter, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val menuItem = menuItemsList[position]
       holder.nameTextView.text =menuItem.twowaysim_status.toString()
        if(menuItem.twowaysim_status_type.equals("Unchecked")){
            holder.status_check.setImageResource(R.drawable.bg_textview_cricle)
            holder.play_audio.setImageResource(R.drawable.full_circle)
        }else{
            holder.status_check.setImageResource(R.drawable.full_circle)
            holder.play_audio.setImageResource(R.drawable.bg_textview_cricle)

        }
        holder.status_check.setOnClickListener {
            itemClickListener.TwowayItemClick(true)
        }
        holder.play_audio.setOnClickListener {
            itemClickListener.TwowayItemClick(false)
        }
    }

    override fun getItemCount(): Int {
        return menuItemsList.size
    }
}