package com.ivis.qcauditapp.adapter.maintenance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.qcauditapp.R

class UploadImagesAdapter(private val context: Context, private val imageList: ArrayList<String>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return imageList.size
    }

    override fun getItem(position: Int): Any {
        return imageList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun removeItem(position: Int) {
        imageList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val holder: ViewHolder

        if (view == null) {
            view = inflater.inflate(R.layout.grid_item_layout, parent, false)
            holder = ViewHolder()
            holder.imageView = view.findViewById(R.id.idIVCourse)
            holder.clearImage = view.findViewById(R.id.clearImage)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        holder.clearImage!!.setOnClickListener {
            removeItem(position)
        }

        // Load the image into the ImageView using Glide or any other image loading library
        Glide.with(context)
            .load(imageList[position])
            .centerCrop()
            .into(holder.imageView!!)

        return view!!
    }

    private class ViewHolder {
        var imageView: ImageView? = null
        var clearImage: ImageView? = null
    }
}
