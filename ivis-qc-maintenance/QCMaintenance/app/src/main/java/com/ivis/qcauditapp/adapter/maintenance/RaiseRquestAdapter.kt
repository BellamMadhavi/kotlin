package com.ivis.qcauditapp.adapter.maintenance

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ivis.qcauditapp.adapter.maintenance.indent.IndentAdapter
import com.ivis.qcauditapp.models.IndentItem
import com.ivis.qcauditapp.models.RequestSpareItem
import com.ivis.qcauditapp.retrofit.ItemOnClickListener
import com.qcauditapp.R

class RaiseRquestAdapter (private val context: Context, private val results: MutableList<RequestSpareItem>,
                          private val onQuantityUpdated: (RequestSpareItem) -> Unit,  // Callback to update quantity in the database
                          private val onDeleteItem: (RequestSpareItem) -> Unit
) : RecyclerView.Adapter<RaiseRquestAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.raiseindentspare_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return results.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = results[position]
        holder.txt_name.text =item.results[0].name
        holder.txt_qty.text =  item.results[0].qty.toString()
        holder.txt_spars_s_no.text = item.results[0].id.toString()
        holder.checkbox.isChecked = true
        holder.btn_edit.visibility = View.VISIBLE
        holder.btn_delete.visibility = View.VISIBLE


        // Handle Edit Button Click
        holder.btn_edit.setOnClickListener {
            showEditQuantityDialog(item, holder.txt_qty, position)
        }

        // Handle Delete Button Click
        holder.btn_delete.setOnClickListener {
            onDeleteItem(item)
            results.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }

    }

    private fun showEditQuantityDialog(item: RequestSpareItem, qtyTextView: TextView, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_quantity, null)
        val dialog = Dialog(context)
        dialog.setContentView(dialogView)

        val itemNameTextView = dialogView.findViewById<TextView>(R.id.itemNameTextView)
        val serialNumberTextView = dialogView.findViewById<TextView>(R.id.serialNumberTextView)
        val currentQuantityTextView = dialogView.findViewById<TextView>(R.id.currentQuantityTextView)
        val newQuantityEditText = dialogView.findViewById<EditText>(R.id.newQuantityEditText)
        val confirmButton = dialogView.findViewById<Button>(R.id.confirmButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)

        // Set the values in the dialog
        val result = item.results[0]
        itemNameTextView.text = result.name
        serialNumberTextView.text = "Serial Number: ${result.id}"
        currentQuantityTextView.text = "Current Quantity: ${result.qty}"

        confirmButton.setOnClickListener {
            val newQuantity = newQuantityEditText.text.toString().toIntOrNull()
            if (newQuantity != null && newQuantity >= 0) {
                item.results[0].qty = newQuantity
                qtyTextView.text = newQuantity.toString()

                // Update the quantity in the database through the callback
                onQuantityUpdated(item)

                dialog.dismiss()
                notifyItemChanged(position)
            } else {
                Toast.makeText(context, "Please enter a valid quantity", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_name: TextView = itemView.findViewById(R.id.txt_name)
        val txt_qty: TextView = itemView.findViewById(R.id.txt_qty)
        val txt_spars_s_no: TextView = itemView.findViewById(R.id.txt_spars_s_no)
        val btn_edit: ImageView = itemView.findViewById(R.id.btn_edit)
        val btn_delete: ImageView = itemView.findViewById(R.id.btn_delete)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)

    }

}
