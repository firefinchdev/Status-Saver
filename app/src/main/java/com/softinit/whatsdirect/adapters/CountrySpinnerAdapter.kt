package com.softinit.whatsdirect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.softinit.whatsdirect.R
import com.softinit.whatsdirect.data.Country

class CountrySpinnerAdapter(
    private var _context: Context,
    countries: List<Country>
) : ArrayAdapter<Country>(_context, 0, countries) {

    companion object {
        @JvmStatic
        val TAG="SPINNER_ADAPTER"
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
       return generateView(position, convertView, parent)
    }

//    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) = View(null)    //No View Visible
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return generateView(position, convertView, parent)
    }

    private fun generateView(position: Int, convertView: View?, parent: ViewGroup): View {
        val retView = convertView?: LayoutInflater.from(context).inflate(R.layout.list_item_spinner,parent, false)

        val countryImage: ImageView = retView.findViewById(R.id.image_view_item)
        val countryTextView: TextView = retView.findViewById(R.id.text_view_item)

        getItem(position)?.let {currentItem ->
            countryImage.setImageResource(currentItem.imageRes)
            countryTextView.text = currentItem.name
        } ?: throw Error("$TAG: Invalid List position")

        return retView
    }
}