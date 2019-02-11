package com.firefinch.dialogspinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class DialogSpinnerAdapter(context: Context, private val itemList: List<SpinnerItem>) : ArrayAdapter<SpinnerItem>(context, 0, itemList) {
    companion object {
        @JvmStatic
        val TAG="SPINNER_ADAPTER"
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val retView = convertView?: LayoutInflater.from(context).inflate(R.layout.list_item_rv,parent, false)

        val itemImage: ImageView = retView.findViewById(R.id.image_view_item)
        val itemTextView: TextView = retView.findViewById(R.id.text_view_item)

        getItem(position)?.let {currentItem ->
            itemImage.setImageResource(currentItem.getImageId())
            itemTextView.text = currentItem.getText()
        } ?: throw Error("$TAG: Invalid List position")

        return retView
    }

    fun getItemList() = itemList

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) = View(null)    //No View Visible

}