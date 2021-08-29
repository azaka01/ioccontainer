package com.intsoftdev.iocconsumerapp.di

import androidx.lifecycle.ViewModel
import com.intsoftdev.iocconsumerapp.playertest.*
import com.intsoftdev.iocconsumerapp.ui.PlayerIOCViewModel
import com.intsoftdev.simpleioclib.ClassScope
import com.intsoftdev.simpleioclib.SimpleIOCContainer
import timber.log.Timber

private fun containerFactory(): SimpleIOCContainer? =
    runCatching {
        SimpleIOCContainer().apply {
            bind(SimplePlayerApi::class, SimplePlayerService::class, ClassScope.Factory)
            bind(
                AnalyticsApi::class,
                SimpleAnalyticsService::class,
                ClassScope.Singleton,
                analyticsProperties
            )
            bind(AdsApi::class, SimpleAdsService::class, ClassScope.Factory)
            bind(ViewModel::class, PlayerIOCViewModel::class, ClassScope.Factory)
        }
    }.onFailure {
        Timber.e("unable to create container $it")
    }.getOrNull()

internal inline fun <reified T : Any> getViewModel(): T? =
    containerFactory()?.let { iocContainer ->
        runCatching<T> {
            iocContainer.create()
        }.onFailure {
            Timber.e("unable to create VM $it")
        }.getOrNull()
    }
