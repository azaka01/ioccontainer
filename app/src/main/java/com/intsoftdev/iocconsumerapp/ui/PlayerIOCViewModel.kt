package com.intsoftdev.iocconsumerapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intsoftdev.iocconsumerapp.playertest.SimplePlayerService
import com.intsoftdev.iocconsumerapp.playertest.key_region
import java.util.*

class PlayerIOCViewModel(private val simplePlayerApi: SimplePlayerService) : ViewModel() {

    val message = MutableLiveData<String>()

    fun onPlay() {
        val movieLength = simplePlayerApi.getTrackLength()
        message.value = String.format(Locale.UK, "Playing movie for next $movieLength minutes")
    }

    fun onAnalytics() {
        val data = simplePlayerApi.dispatchEvent(key_region)
        message.value = String.format(Locale.UK, "Google knows you are in $data")
    }

    fun onAds() {
        val numAds = simplePlayerApi.getNumAds()
        message.value = String.format(Locale.UK, "You are forced to watch $numAds ads")
    }
}