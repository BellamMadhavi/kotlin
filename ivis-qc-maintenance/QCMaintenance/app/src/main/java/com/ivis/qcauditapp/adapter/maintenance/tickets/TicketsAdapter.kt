package com.ivis.qcauditapp.adapter.maintenance.tickets

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ivis.qcauditapp.models.Ticket
import com.ivis.qcauditapp.retrofit.ItemOnClickListener
import com.qcauditapp.R

class TicketsAdapter(private val context: Context,
                     private val tickets: MutableList<Ticket>, private val itemClickListener: ItemOnClickListener) : RecyclerView.Adapter<TicketsAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maintenance_tckts_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = tickets[position]

        holder.Ticket_id.text = item.ticketId.toString()
        holder.Site_name.text = item.task.toString()
        holder.Ticket_priority.text = item.priority.toString()
        holder.unit_id.text = item.unitId.toString()

        if (item.dependency.toString().equals("null")){
            holder.Dependency.text = "NA"
        } else {
            holder.Dependency.text = item.dependency.toString()
        }

        if (item.spareReceived.toString().equals("F")){
            holder.Spare_Received.text = "Not Received"
        } else if (item.spareReceived.toString().equals("null")) {
            holder.Spare_Received.text = "NA"
        } else {
            holder.Spare_Received.text = item.spareReceived.toString()
        }

        holder.Ticket_SLA.text = item.date.toString()
        holder.Location.text = item.siteName.toString()
        holder.txt_ticket_status.text = item.ticketStatus.toString()

        //visit status
        if (item.attended.equals("Done", ignoreCase = true)) {
            holder.txt_visited_status.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorCompletedInspection))
        } else {
            holder.txt_visited_status.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
        }
        holder.txt_visited_status.text = item.attended
        holder.txt_schedule_order.text = item.priority.toString()

        holder.itemView.setOnClickListener {
            itemClickListener.MaintanenceTicketsClickListener(position)
        }

    }

    override fun getItemCount(): Int {
        return tickets.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Ticket_id: TextView = itemView.findViewById(R.id.ticket_id)
        val Site_name: TextView = itemView.findViewById(R.id.txt_site_name)
        val Ticket_priority: TextView = itemView.findViewById(R.id.txt_ticket_priority)
        val Ticket_SLA: TextView = itemView.findViewById(R.id.txt_ticket_sla)
        val Location: TextView = itemView.findViewById(R.id.txt_location)
        val Dependency: TextView = itemView.findViewById(R.id.txt_dependency)
        val Spare_Received: TextView = itemView.findViewById(R.id.txt_spare_received)
        val unit_id: TextView = itemView.findViewById(R.id.txt_unit_id)
        val txt_ticket_status: TextView = itemView.findViewById(R.id.txt_ticket_status)
        val txt_visited_status: TextView = itemView.findViewById(R.id.txt_visited_status)
        val txt_schedule_order: TextView = itemView.findViewById(R.id.txt_schedule_order)
    }

}