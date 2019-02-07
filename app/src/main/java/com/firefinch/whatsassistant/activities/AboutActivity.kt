package com.firefinch.whatsassistant.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.firefinch.whatsassistant.R
import com.firefinch.whatsassistant.utils.getOpenSourceDialog

class AboutActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btnOSLicense: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        btnOSLicense = findViewById(R.id.btn_licenses)
        btnOSLicense.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_licenses -> {
                getOpenSourceDialog(this).show()
            }
        }
    }
}
