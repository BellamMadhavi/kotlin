package com.ivis.qcauditapp.adapter.maintenance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivis.qcauditapp.models.SpareIndent
import com.qcauditapp.R

class TicketTaskdetailsSpareIndentInfoAdapter(private val ticketTasks: List<SpareIndent>) : RecyclerView.Adapter<TicketTaskdetailsSpareIndentInfoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tickettaskdetailsspareindent_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = ticketTasks[position]
        holder.txt_indentSpareinfo_ticketid.text = task.ticketId.toString()
        holder.txt_indentSpareinfo_unitid.text = task.unitId
        holder.txt_indentSpareinfo_indentStatus.text = task.indentStatus
        holder.txt_indentSpareinfo_requestedDate.text = task.requestedDate
        holder.txt_indentSpareinfo_dispatcheddate.text = task.dispatchedDate
        holder.txt_indentSpareinfo_itemName.text = task.itemName
        holder.txt_indentSpareinfo_reqfieldreq.text = task.reqFieldRep
        holder.txt_indentSpareinfo_receiveddate.text = task.receivedDate
    }

    override fun getItemCount(): Int = ticketTasks.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_indentSpareinfo_ticketid: TextView = itemView.findViewById(R.id.txt_indentSpareinfo_ticketid)
        val txt_indentSpareinfo_unitid: TextView = itemView.findViewById(R.id.txt_indentSpareinfo_unitid)
        val txt_indentSpareinfo_indentStatus: TextView = itemView.findViewById(R.id.txt_indentSpareinfo_indentStatus)
        val txt_indentSpareinfo_requestedDate: TextView = itemView.findViewById(R.id.txt_indentSpareinfo_requestedDate)
        val txt_indentSpareinfo_dispatcheddate: TextView = itemView.findViewById(R.id.txt_indentSpareinfo_dispatcheddate)
        val txt_indentSpareinfo_itemName: TextView = itemView.findViewById(R.id.txt_indentSpareinfo_itemName)
        val txt_indentSpareinfo_reqfieldreq: TextView = itemView.findViewById(R.id.txt_indentSpareinfo_reqfieldreq)
        val txt_indentSpareinfo_receiveddate: TextView = itemView.findViewById(R.id.txt_indentSpareinfo_receiveddate)
    }
}
