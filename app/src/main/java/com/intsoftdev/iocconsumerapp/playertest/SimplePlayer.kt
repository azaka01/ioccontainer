package com.intsoftdev.iocconsumerapp.playertest

internal const val key_region = "viewerRegion"
internal const val age_group = "ageGroup"

interface SimplePlayerApi {
    fun getTrackLength(): Int
}

class SimplePlayerService constructor(
    private val analyticsApi: AnalyticsApi,
    private val adsApi: AdsApi
) :
    SimplePlayerApi, AdsApi by adsApi, AnalyticsApi by analyticsApi {
    override fun getTrackLength(): Int {
        return 99
    }
}

internal val analyticsProperties = mapOf(
    "analyticsData" to mapOf(key_region to "London", age_group to "pensioner")
)

interface AnalyticsApi {
    fun dispatchEvent(key: String): String
}
class SimpleAnalyticsService(val analyticsData: Map<String, String> = emptyMap()) : AnalyticsApi {
    override fun dispatchEvent(key: String) =
        analyticsData[key]?.let { send(it) } ?: ""

    private fun send(data: String): String = data
}

interface AdsApi {
    fun getNumAds(): Int
}
class SimpleAdsService : AdsApi {
    override fun getNumAds(): Int {
        return 2
    }
}