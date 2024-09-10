package com.ivis.qcauditapp.adapter.maintenance.tasks

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ivis.qcauditapp.models.TicketTask
import com.ivis.qcauditapp.retrofit.ItemOnClickListener
import com.qcauditapp.R

class TaskListAdapter(private val context: Context,
                      private val tasks: MutableList<TicketTask>, private val listener: ItemOnClickListener,     private val updatedTasks: List<Int> , private val attended:String): RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tasklist_item_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = tasks[position]
        holder.imv_status.setImageResource(R.drawable.unselected)
        holder.txt_qus.text = item.title

        Log.d("ConditionCheck", "updatedTasks.contains(item.taskId): ${updatedTasks.contains(item.taskId)}")
        Log.d("ConditionCheck", "item.fixedReason?.isNotBlank() == true: ${item.fixedReason?.isNotBlank() == true}")
        Log.d("ConditionCheck", "item.openReason?.isNotBlank() == true: ${item.openReason?.isNotBlank() == true}")
        Log.d("ConditionCheck", "attended.contains(\"Pending\", ignoreCase = true): ${attended.contains("Pending", ignoreCase = true)}")
        Log.d("ConditionCheck", "item.remarks?.isNotBlank() == true: ${item.remarks?.isNotBlank() == true}")

        // Check if the task is already updated
        // Check if the task is already updated
        if(attended.equals("Done", true)) {
            // full green, disable click listener
            holder.Radio_Group.isEnabled = false
            holder.radio_yes.isEnabled = false
            holder.radio_no.isEnabled = false

            // Clear any existing listener to avoid unwanted triggers
            holder.Radio_Group.setOnCheckedChangeListener(null)

            // Set the radio button according to the existing status
            when (item.status) { // Assuming taskStatus is a property in TicketTask
                "Fixed" -> holder.Radio_Group.check(R.id.radio_yes)
                "Open" -> holder.Radio_Group.check(R.id.radio_no)
            }

            holder.imv_status.setImageResource(R.drawable.tick_icon_checked)
            holder.placed_divider.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
        }
        if(attended.equals("Pending", true)) {
            if(updatedTasks.contains(item.taskId)) {
                if(item.fixedReason?.isNotBlank() == true || item.openReason?.isNotBlank() == true) {
                    // locally saved, light green, enable onclick
                    holder.Radio_Group.setOnCheckedChangeListener(null)
                    holder.Radio_Group.clearCheck()
                    holder.Radio_Group.isEnabled = true

                    // Set the radio button according to the existing status
                    when (item.status) { // Assuming taskStatus is a property in TicketTask
                        "Fixed" -> holder.Radio_Group.check(R.id.radio_yes)
                        "Open" -> holder.Radio_Group.check(R.id.radio_no)
                    }

                    holder.Radio_Group.setOnCheckedChangeListener { _, checkedId ->
                        val selectedOption = when (checkedId) {
                            R.id.radio_yes -> "Fixed"
                            R.id.radio_no -> "Open"
                            else -> ""
                        }
                        listener.onRadioButtonClicked(position, selectedOption, item)
                    }
                    holder.imv_status.setImageResource(R.drawable.tick_icon)
                    holder.placed_divider.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
                }
                else {
                    // only task id present db, which means user selected task, but not yet updated the open reason or fixed reason.
                    holder.Radio_Group.setOnCheckedChangeListener(null)
                    holder.Radio_Group.clearCheck()
                    holder.Radio_Group.isEnabled = true
                    holder.itemView.alpha = 1.0f

                    holder.imv_status.setImageResource(R.drawable.unchecked)
                    holder.placed_divider.setBackgroundColor(ContextCompat.getColor(context, R.color.colortextsitename))

                    // Set the radio button according to the task's current status if it's not already updated
                    when (item.status) {
                        "Fixed" -> holder.Radio_Group.check(R.id.radio_yes)
                        "Open" -> holder.Radio_Group.check(R.id.radio_no)
                    }

                    holder.Radio_Group.setOnCheckedChangeListener { _, checkedId ->
                        val selectedOption = when (checkedId) {
                            R.id.radio_yes -> "Fixed"
                            R.id.radio_no -> "Open"
                            else -> ""
                        }
                        listener.onRadioButtonClicked(position, selectedOption, item)
                    }
                }
            }
            else {
                if(item.fixedReason?.isNotBlank() == true || item.openReason?.isNotBlank() == true) {
                    // task id not present in db, but openreason or fixedreason not blank which means somebody updated from web, full green, disable onclick
                    // full green, disable click listener
                    holder.Radio_Group.isEnabled = false
                    holder.radio_yes.isEnabled = false
                    holder.radio_no.isEnabled = false

                    // Clear any existing listener to avoid unwanted triggers
                    holder.Radio_Group.setOnCheckedChangeListener(null)

                    // Set the radio button according to the existing status
                    when (item.status) { // Assuming taskStatus is a property in TicketTask
                        "Fixed" -> holder.Radio_Group.check(R.id.radio_yes)
                        "Open" -> holder.Radio_Group.check(R.id.radio_no)
                    }

                    holder.imv_status.setImageResource(R.drawable.tick_icon_checked)
                    holder.placed_divider.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
                }
                else {
                    // task id not present db, which means user not yet selected task, and not yet updated the open reason or fixed reason.
                    holder.Radio_Group.setOnCheckedChangeListener(null)
                    holder.Radio_Group.clearCheck()
                    holder.Radio_Group.isEnabled = true
                    holder.itemView.alpha = 1.0f

                    holder.imv_status.setImageResource(R.drawable.unchecked)
                    holder.placed_divider.setBackgroundColor(ContextCompat.getColor(context, R.color.colortextsitename))

                    // Set the radio button according to the task's current status if it's not already updated
                    when (item.status) {
                        "Fixed" -> holder.Radio_Group.check(R.id.radio_yes)
                        "Open" -> holder.Radio_Group.check(R.id.radio_no)
                    }

                    holder.Radio_Group.setOnCheckedChangeListener { _, checkedId ->
                        val selectedOption = when (checkedId) {
                            R.id.radio_yes -> "Fixed"
                            R.id.radio_no -> "Open"
                            else -> ""
                        }
                        listener.onRadioButtonClicked(position, selectedOption, item)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
       return tasks.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_qus: TextView = itemView.findViewById(R.id.txt_qus)
        val imv_status: ImageView = itemView.findViewById(R.id.imv_status)
        val Radio_Group: RadioGroup = itemView.findViewById(R.id.Radio_Group)
        val radio_yes: RadioButton = itemView.findViewById(R.id.radio_yes)
        val radio_no: RadioButton = itemView.findViewById(R.id.radio_no)
        val placed_divider: View = itemView.findViewById(R.id.placed_divider)
    }
}
