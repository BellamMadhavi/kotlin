package com.ivis.qcauditapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ivis.qcauditapp.fragment.DashBoardFragment
import com.ivis.qcauditapp.models.Dashboarditems
import com.qcauditapp.R

class MenuListAdapter(private val fragment: DashBoardFragment, private val items: List<Dashboarditems>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(fragment.requireContext())
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Check if the convertView is null
        val view: View = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.item_grid, parent, false)

        val imgWorkorder: ImageView = view.findViewById(R.id.img_workorder)
        val txtTitle: TextView = view.findViewById(R.id.txt_title)
        val img_tick: ImageView = view.findViewById(R.id.img_tick)

        // Assuming 'item' is a data object corresponding to the current position
        val item = getItem(position) as Dashboarditems

        imgWorkorder.setImageResource(item.itemImg)

        txtTitle.text = item.itemName

        if (item.itemName.equals("Start Maintenance")){
            if (item.checked) {
                img_tick.setBackgroundResource(R.drawable.tick_icon_checked)
            } else {
                img_tick.setBackgroundResource(R.drawable.unchecked)
            }
        } else {
            img_tick.setBackgroundResource(R.drawable.unchecked)
        }

        /*if (item.checked) {
            img_tick.setBackgroundResource(R.drawable.tick_icon_checked )
        } else {
            img_tick.setBackgroundResource(R.drawable.unchecked )
        }*/

        // Return the prepared view
        return view
    }


}
