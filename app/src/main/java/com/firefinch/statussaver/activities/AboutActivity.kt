package com.firefinch.statussaver.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.firefinch.statussaver.BuildConfig
import com.firefinch.statussaver.R
import com.firefinch.statussaver.utils.AppIntent
import com.firefinch.statussaver.utils.getOpenSourceDialog

class AboutActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btnOSLicense: Button
    private lateinit var btnPrivacyPolicy: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        btnOSLicense = findViewById(R.id.btn_licenses)
        btnPrivacyPolicy = findViewById(R.id.btn_privacy_policy)

        findViewById<TextView>(R.id.tv_app_version).text = "Version v${BuildConfig.VERSION_NAME}"

        listOf(btnOSLicense, btnPrivacyPolicy).forEach { it.setOnClickListener(this)}
    }
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_licenses -> {
                getOpenSourceDialog(this).show()
            }
            R.id.btn_privacy_policy -> {
                startActivity(AppIntent
                    .viewInBrowser(getString(R.string.privacy_policy_url))
                )
            }
        }
    }
}
