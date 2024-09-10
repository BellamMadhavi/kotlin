package com.ivis.qcauditapp.adapter.maintenance.indent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivis.qcauditapp.db.RaiseIndentSpare
import com.qcauditapp.R

class RaiseIndentSpareAdapterSummary (private val context: Context,
                                      private val indents: MutableList<RaiseIndentSpare>): RecyclerView.Adapter<RaiseIndentSpareAdapterSummary.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.raiseindentspare_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return indents.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = indents[position]
        holder.txt_name.text = item.name
        holder.txt_qty.text = item.qty.toString()
        holder.txt_spars_s_no.text = item.id.toString()
        holder.btn_edit.visibility = View.GONE
        holder.btn_delete.visibility = View.GONE
        holder.checkbox.visibility = View.GONE
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_name: TextView = itemView.findViewById(R.id.txt_name)
        val txt_qty: TextView = itemView.findViewById(R.id.txt_qty)
        val txt_spars_s_no: TextView = itemView.findViewById(R.id.txt_spars_s_no)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
        val btn_edit: ImageView = itemView.findViewById(R.id.btn_edit)
        val btn_delete: ImageView = itemView.findViewById(R.id.btn_delete)
    }

}