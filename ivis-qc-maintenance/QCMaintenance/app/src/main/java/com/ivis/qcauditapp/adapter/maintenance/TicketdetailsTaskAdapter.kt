package com.ivis.qcauditapp.adapter.maintenance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivis.qcauditapp.models.TicketTask
import com.qcauditapp.R

class TicketdetailsTaskAdapter(private val ticketTasks: List<TicketTask>) : RecyclerView.Adapter<TicketdetailsTaskAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ticketdetails_multipletask_adapter, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val task = ticketTasks[position]

        holder.txt_taskinfo_taskTitle.text = task.title

        holder.txt_taskinfo_status.text = task.status

        holder.txt_taskinfo_queue.text = task.queue

        holder.txt_taskinfo_openreason.text = task.openReason

        holder.txt_taskinfo_fixedReason.text = task.fixedReason

        holder.txt_taskinfo_closingReason.text = task.closingReason

        holder.txt_taskinfo_remarks.text = task.remarks

        holder.txt_taskinfo_cameraid.text = task.cameraId

        holder.txt_taskinfo_footagefrom.text = task.footageFrom

        holder.txt_taskinfo_dependency.text = task.dependency

        holder.txt_taskinfo_fixedType.text = task.fixedType

        holder.txt_taskinfo_footageTo.text = task.footageTo

        if (task.title.equals("Footage Required")){

            holder.txt_taskinfo_cameraid.visibility = View.VISIBLE

            holder.txt_taskinfo_footagefrom.visibility = View.VISIBLE

            holder.txt_taskinfo_footageTo.visibility = View.VISIBLE

        } else {

            holder.txt_taskinfo_cameraid.visibility = View.GONE

            holder.txt_taskinfo_footagefrom.visibility = View.GONE

            holder.txt_taskinfo_footageTo.visibility = View.GONE

        }

    }

    override fun getItemCount(): Int = ticketTasks.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txt_taskinfo_taskTitle: TextView = itemView.findViewById(R.id.txt_taskinfo_taskTitle)

        val txt_taskinfo_status: TextView = itemView.findViewById(R.id.txt_taskinfo_status)

        val txt_taskinfo_queue: TextView = itemView.findViewById(R.id.txt_taskinfo_queue)

        val txt_taskinfo_openreason: TextView = itemView.findViewById(R.id.txt_taskinfo_openreason)

        val txt_taskinfo_fixedReason: TextView = itemView.findViewById(R.id.txt_taskinfo_fixedReason)

        val txt_taskinfo_closingReason: TextView = itemView.findViewById(R.id.txt_taskinfo_closingReason)

        val txt_taskinfo_remarks: TextView = itemView.findViewById(R.id.txt_taskinfo_remarks)

        val txt_taskinfo_cameraid: TextView = itemView.findViewById(R.id.txt_taskinfo_cameraid)

        val txt_taskinfo_footagefrom: TextView = itemView.findViewById(R.id.txt_taskinfo_footagefrom)

        val txt_taskinfo_dependency: TextView = itemView.findViewById(R.id.txt_taskinfo_dependency)

        val txt_taskinfo_fixedType: TextView = itemView.findViewById(R.id.txt_taskinfo_fixedType)

        val txt_taskinfo_footageTo: TextView = itemView.findViewById(R.id.txt_taskinfo_footageTo)

    }

}