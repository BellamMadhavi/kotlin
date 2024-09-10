package com.ivis.qcauditapp.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivis.qcauditapp.adapter.SummaryListAdapter
import com.ivis.qcauditapp.adapter.VideoLiveAdapter
import com.qcauditapp.R // Assuming R is the correct resource file for your project
import com.qcauditapp.models.SubmitData


class SummaryNameAdapter(private val menuItemsList: List<SubmitData>) : RecyclerView.Adapter<SummaryNameAdapter.MyViewHolder>() {
    var strname:String=""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.summary_names_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val menuItem = menuItemsList[position]

        if(!menuItem.id.equals(strname)){
            strname=menuItem.id
            holder.nameTextView.text = menuItem.id
        }else{
            holder.nameTextView.visibility=View.GONE
        }
        val context = holder.itemView.context
        holder.summaryListAdapter = SummaryListAdapter(menuItem)
       holder.summary_list_recycler.layoutManager = LinearLayoutManager(context)
        holder.summary_list_recycler.adapter = holder.summaryListAdapter
    }



    override fun getItemCount(): Int {
        return menuItemsList.size
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var summaryListAdapter: SummaryListAdapter
        val nameTextView: TextView = itemView.findViewById(R.id.txtname) // Replace with the actual ID of your TextView in the layout
        val summary_list_recycler: RecyclerView = itemView.findViewById(R.id.summary_list_recycler) // Replace with the actual ID of your TextView in the layout
    }

}
