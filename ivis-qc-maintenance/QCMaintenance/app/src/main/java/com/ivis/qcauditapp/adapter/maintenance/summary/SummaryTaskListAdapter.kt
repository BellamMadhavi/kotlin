package com.ivis.qcauditapp.adapter.maintenance.summary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivis.qcauditapp.adapter.maintenance.indent.RaiseIndentSpareAdapterSummary
import com.ivis.qcauditapp.adapter.maintenance.indent.SpareAdapterSummary
import com.ivis.qcauditapp.db.RaiseIndentSpare
import com.ivis.qcauditapp.db.SpareBagEntity
import com.ivis.qcauditapp.models.SummaryTask
import com.ivis.qcauditapp.models.Task
import com.ivis.qcauditapp.viewmodels.maintenance.TicketTaskDbDataViewModel
import com.qcauditapp.R

class SummaryTaskListAdapter(
    private val tasks: List<SummaryTask>,
    private val viewModel: TicketTaskDbDataViewModel
) : RecyclerView.Adapter<SummaryTaskListAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskIdTextView: TextView = view.findViewById(R.id.txt_task_id)
        val taskTitleTextView: TextView = view.findViewById(R.id.txt_task_name)
        val taskStatusTextView: TextView = view.findViewById(R.id.txt_task_status)
        val taskCommentsTextView: TextView = view.findViewById(R.id.txt_task_comments)
        val taskTypeTextView: TextView = view.findViewById(R.id.txt_task_type)
        val taskReasonTextView: TextView = view.findViewById(R.id.txt_task_reason)
        val raisedIndentsRecyclerView: RecyclerView = view.findViewById(R.id.raised_indentandspare_details_rcv)
        val spareItemsRecyclerView: RecyclerView = view.findViewById(R.id.spare_items_rcv)

        init {
            raisedIndentsRecyclerView.layoutManager = LinearLayoutManager(view.context)
            spareItemsRecyclerView.layoutManager = LinearLayoutManager(view.context)
        }

        fun bind(task: SummaryTask) {
            taskIdTextView.text = task.taskId.toString()
            taskTitleTextView.text = task.taskTitle
            taskStatusTextView.text = task.status
            taskCommentsTextView.text = task.remarks
            taskTypeTextView.text = task.fixedType ?: task.openReason
            taskReasonTextView.text = task.fixedReason ?: task.dependency

            if (task.status == "Fixed") {
                raisedIndentsRecyclerView.visibility = View.GONE
                spareItemsRecyclerView.visibility = View.VISIBLE
            } else if (task.status == "Open") {
                raisedIndentsRecyclerView.visibility = View.VISIBLE
                spareItemsRecyclerView.visibility = View.GONE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.summary_task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size
}
