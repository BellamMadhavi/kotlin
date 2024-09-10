package com.ivis.qcauditapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivis.qcauditapp.MyViewHolder
import com.ivis.qcauditapp.models.SensorStatus
import com.ivis.qcauditapp.models.TwoWaySimStatus
import com.ivis.qcauditapp.retrofit.OnClickListener
import com.qcauditapp.R

class SensorAdapter(private val menuItemsList: MutableList<SensorStatus>, private val itemClickListener: OnClickListener) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.sensor_adapter, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val menuItem = menuItemsList[position]
        holder.nameTextView.text =menuItem.Sensor_Name.toString()
        if(menuItem.Type.equals("Unchecked")){
            holder.status_check.setImageResource(R.drawable.bg_textview_cricle)
            holder.play_audio.setImageResource(R.drawable.full_circle)
        }else{
            holder.status_check.setImageResource(R.drawable.full_circle)
            holder.play_audio.setImageResource(R.drawable.bg_textview_cricle)
        }
        holder.itemView.setOnClickListener {
            itemClickListener.SensorItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return menuItemsList.size
    }
}