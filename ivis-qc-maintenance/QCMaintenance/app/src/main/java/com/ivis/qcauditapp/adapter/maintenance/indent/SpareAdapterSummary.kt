package com.ivis.qcauditapp.adapter.maintenance.indent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivis.qcauditapp.db.SpareBagEntity
import com.ivis.qcauditapp.models.SpareBagItem
import com.ivis.qcauditapp.retrofit.ItemOnClickListener
import com.qcauditapp.R

class SpareAdapterSummary (private val context: Context,
                           private val spare: MutableList<SpareBagEntity>) : RecyclerView.Adapter<SpareAdapterSummary.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.indentdetails_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return spare.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = spare[position]
        holder.txt_name.text = item.itemDescription
        holder.txt_qty.text = item.qty.toString()
        holder.txt_spars_s_no.text = item.serialNo

        holder.checkbox.visibility = View.GONE
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_name: TextView = itemView.findViewById(R.id.txt_name)
        val txt_qty: TextView = itemView.findViewById(R.id.txt_qty)
        val txt_spars_s_no: TextView = itemView.findViewById(R.id.txt_spars_s_no)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)

    }
}