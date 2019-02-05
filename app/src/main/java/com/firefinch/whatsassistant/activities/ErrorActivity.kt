package com.firefinch.whatsassistant.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.firefinch.whatsassistant.R
import com.firefinch.whatsassistant.utils.AppIntent

class ErrorActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnRestart: Button
    private lateinit var btnReport: Button
    private var config: CaocConfig? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        setupIds()
        setup()
    }

    private fun setup() {
        btnRestart.setOnClickListener(this)
        btnReport.setOnClickListener(this)
        config = CustomActivityOnCrash.getConfigFromIntent(intent)
        if (config == null) {
            finish()
            return
        }
        if (config?.restartActivityClass == null) {
            btnRestart.visibility = View.GONE
        }

    }

    private fun setupIds() {
        btnRestart = findViewById(R.id.btn_restart)
        btnReport = findViewById(R.id.btn_report)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_restart -> CustomActivityOnCrash.restartApplication(this@ErrorActivity, config!!)
            R.id.btn_report -> {
                val msg: String? = CustomActivityOnCrash.getStackTraceFromIntent(intent)
                startActivity(AppIntent.devMail(this@ErrorActivity,
                    "Error in ${getString(R.string.app_name)}",
                    "Following Error occurred on my device:\n\n${msg ?: ""}"))
            }
        }
    }
}
