package com.ozgegn.animateddownloader

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    private val textFileName: TextView by lazy {
        findViewById(R.id.detail_file_name)
    }
    private val textFileStatus: TextView by lazy {
        findViewById(R.id.detail_file_status)
    }
    private val buttonOk: Button by lazy {
        findViewById(R.id.detail_ok)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        buttonOk.setOnClickListener {
            finish()
        }

        val status = intent.getBooleanExtra(STATUS_EXTRA, false)
        val message  = intent.getStringExtra(MESSAGE_EXTRA)

        textFileStatus.text = if (status) getString(R.string.detail_success) else getString(R.string.detail_fail)
        textFileName.text = message
    }

    companion object {
        const val STATUS_EXTRA = "status"
        const val MESSAGE_EXTRA = "message"
    }

}