package com.ivis.qcauditapp.adapter.maintenance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivis.qcauditapp.models.SchInfoHist
import com.ivis.qcauditapp.models.TicketTask
import com.qcauditapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class
TicketDetailsSchedulesInfoAdapter(private val ticketTasks: List<SchInfoHist>) : RecyclerView.Adapter<TicketDetailsSchedulesInfoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ticketdetails_scheduleinfo_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = ticketTasks[position]
        holder.txt_schdinfo_fieldrep.text= task.fieldRep

        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat(
            "yyyy-MM-dd", Locale.getDefault())

        val date: Date? = inputFormat.parse(task.scheduledDate)
        val formattedDate: String = date?.let{
            outputFormat.format(it) } ?: ""

        holder.txt_schdinfo_scheduleddate.text= formattedDate
        holder.txt_schdinfo_visiteddate.text= task.visitedDate
        holder.txt_schdinfo_status.text= task.visitStatus
        holder.txt_schdinfo_ticketstatus.text= task.ticketStatus
        holder.txt_schdinfo_total.text= task.total.toString()
        holder.txt_schdinfo_description.text= task.description
    }

    override fun getItemCount(): Int = ticketTasks.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_schdinfo_fieldrep: TextView = itemView.findViewById(R.id.txt_schdinfo_fieldrep)
        val txt_schdinfo_scheduleddate: TextView = itemView.findViewById(R.id.txt_schdinfo_scheduleddate)
        val txt_schdinfo_visiteddate: TextView = itemView.findViewById(R.id.txt_schdinfo_visiteddate)
        val txt_schdinfo_status: TextView = itemView.findViewById(R.id.txt_schdinfo_status)
        val txt_schdinfo_ticketstatus: TextView = itemView.findViewById(R.id.txt_schdinfo_ticketstatus)
        val txt_schdinfo_total: TextView = itemView.findViewById(R.id.txt_schdinfo_total)
        val txt_schdinfo_description: TextView = itemView.findViewById(R.id.txt_schdinfo_description
        )
    }
}