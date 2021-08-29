package com.intsoftdev.iocconsumerapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.intsoftdev.iocconsumerapp.BuildConfig
import com.intsoftdev.iocconsumerapp.R
import com.intsoftdev.iocconsumerapp.di.getViewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private var viewModel: PlayerIOCViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val playBtn = findViewById<Button>(R.id.button_play)
        playBtn.setOnClickListener {
            viewModel?.onPlay()
        }

        val analyticsBtn = findViewById<Button>(R.id.button_analytics)
        analyticsBtn.setOnClickListener {
            viewModel?.onAnalytics()
        }

        val adsBtn = findViewById<Button>(R.id.button_ads)
        adsBtn.setOnClickListener {
            viewModel?.onAds()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel = getViewModel()
        viewModel?.message?.observe(this, {
            Toast.makeText(this, it, LENGTH_SHORT).show()
        })
    }
}