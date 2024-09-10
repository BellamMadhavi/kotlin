package com.ivis.qcauditapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.qcauditapp.R
import com.qcauditapp.models.SubmitData

class SummaryListAdapter(private val menuItemsList: SubmitData) : RecyclerView.Adapter<SummaryListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.summary_list_adapter, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context = holder.itemView.context

        if(menuItemsList.type.toString().equals("Checked")){
            holder.nameTextView.text = menuItemsList.value
            holder.status_check.setImageResource(R.drawable.check)
        }else{
            holder.nameTextView.text = menuItemsList.value
          //  holder.nameTextView.setBackgroundColor(R.color.text_color_name)
            holder.status_check.setImageResource(R.drawable.uncheck)
           /* val tintColor = ContextCompat.getColor(context, R.color.text_color_name)
            holder.status_check.setColorFilter(tintColor)*/


        }
    }

    override fun getItemCount(): Int {
        return 1
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.txtname) // Replace with the actual ID of your TextView in the layout
        val status_check: ImageView = itemView.findViewById(R.id.status_check) // Replace with the actual ID of your TextView in the layout
    }

}