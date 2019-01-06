package com.softinit.dialogspinner

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.SpinnerAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.FragmentManager

class DialogSpinner : AppCompatSpinner, DialogInterface.OnClickListener, View.OnTouchListener {

    private var searchableDialog: SearchableDialog = SearchableDialog.newInstance(arrayListOf())    //Initialize with empty Arraylist
    private var fragmentManager: FragmentManager
    private var _context: Context

    constructor(context: Context, fm: FragmentManager): super(context) {
        this._context = context
        fragmentManager = fm
    }

    //TODO: How to get it to work for FragmentManag instead of supportFragManag?
    constructor(context: Context, attrs: AttributeSet): this(context, (context as AppCompatActivity).supportFragmentManager) {
//        Useless for now
//        val a = context.obtainStyledAttributes(attrs, R.styleable.SearchableSpinner)
//        val N = a.getIndexCount()
//        for (i in 0 until N) {
//            val attr = a.getIndex(i)
//            if (attr == R.styleable.SearchableSpinner_hintText) {
//                _strHintText = a.getString(attr)
//            }
//        }
//        a.recycle()
    }

    init {
        setOnTouchListener(this)
    }
//    constructor(context: Context, attrs: AttributeSet, fm: FragmentManager): super(context, attrs) {init(fm)}
//    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, fm: FragmentManager): super(context, attrs, defStyleAttr) {init(fm)}

    override fun performClick(): Boolean {
        return (this as View).performClick()
    }

    // Cant set only spinner adapter, so no super.setAdapter()
    // Dont Use this method, instead use setAdapter(adapter: DialogSpinnerAdapter)
    override fun setAdapter(adapter: SpinnerAdapter?) {
        //super.setAdapter(adapter)
        return
    }

    fun setAdapter(adapter: DialogSpinnerAdapter)  {
        super.setAdapter(adapter)
        searchableDialog.adapter = SpinnerRecyclerViewAdapter(adapter.getItemList())
    }

    //TODO: Do I need this?
    override fun onClick(dialog: DialogInterface?, which: Int) {
        super.onClick(dialog, which)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}