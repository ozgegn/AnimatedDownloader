package com.ozgegn.animateddownloader

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {

    private val toolbar: Toolbar by lazy {
        findViewById(R.id.toolbar)
    }
    private val loadingButton: LoadingButton by lazy {
        findViewById(R.id.custom_button)
    }

    private val downloadList: RadioGroup by lazy {
        findViewById(R.id.download_choice_list)
    }

    private val radioGlide: RadioButton by lazy {
        findViewById(R.id.radio_glide)
    }

    private val radioLoadApp: RadioButton by lazy {
        findViewById(R.id.radio_loadapp)
    }

    private val radioRetrofit: RadioButton by lazy {
        findViewById(R.id.radio_retrofit)
    }

    private var downloadID: Long = 0
    private var selectedDownloadOption: DownLoadOptions? = null

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        loadingButton.setOnClickListener {
            onDownloadClicked()
            loadingButton.startLoading()
        }

        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)
        )

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            loadingButton.completeLoading()
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {
                val query = DownloadManager.Query()
                val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val cursor = downloadManager.query(query)
                if (cursor.moveToFirst()) {
                    sendNotification()
                }
            }
        }
    }

    private fun download(url: String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(getString(R.string.app_name))
            .setDescription(getString(R.string.app_description))
            .setRequiresCharging(false)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)

    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun sendNotification() {
        selectedDownloadOption?.let {
            val notificationManager =
                getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.cancelAll()
            notificationManager.sendNotification(
                getString(it.body),
                this
            )
        }
    }

    private fun onDownloadClicked() {
        when (downloadList.checkedRadioButtonId) {
            radioGlide.id -> {
                selectedDownloadOption = DownLoadOptions.GLIDE
                download(DownLoadOptions.GLIDE.url)
            }
            radioLoadApp.id -> {
                selectedDownloadOption = DownLoadOptions.UDACITY
                download(DownLoadOptions.UDACITY.url)
            }
            radioRetrofit.id -> {
                selectedDownloadOption = DownLoadOptions.RETROFIT
                download(DownLoadOptions.RETROFIT.url)
            }
            else -> {
                Toast.makeText(this, R.string.error_no_selection, Toast.LENGTH_LONG).show()
            }
        }
    }
}