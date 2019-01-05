package com.softinit.dialogspinner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SpinnerRecyclerViewAdapter(itemList: List<SpinnerItem>): RecyclerView.Adapter<SpinnerRecyclerViewAdapter.ItemViewHolder>() {

    var itemList = itemList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_rv, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        itemList.get(position).let {
            holder.textView.text = it.getText()
            holder.imageView.setImageResource(it.getImageId())
        }
    }


    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view.findViewById(R.id.image_view_item)
        var textView: TextView = view.findViewById(R.id.text_view_item)
    }
}