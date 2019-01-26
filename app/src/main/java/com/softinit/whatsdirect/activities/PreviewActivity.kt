package com.softinit.whatsdirect.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.softinit.whatsdirect.R

class PreviewActivity : AppCompatActivity() {

    companion object {
        const val INTENT_EXTRA_PATH = "INTENT_EXTRA_PATH"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
    }
}
