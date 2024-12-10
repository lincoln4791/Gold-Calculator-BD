package com.lincoln4791.goldcalculatorbd.common

import android.content.Context
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.lincoln4791.goldcalculatorbd.Constants
import com.lincoln4791.goldcalculatorbd.PrefManager
import com.lincoln4791.goldcalculatorbd.R

object FirebaseUtil {

    fun fetchCommonDataFromRemoteConfig(context: Context) {
        val prefManager = PrefManager(context)

        if (System.currentTimeMillis() - prefManager.lastCommonRemoteConfigDataFetchTime >= Constants.INTERVAL_HOURLY) {

            val firebaseRemoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
            Log.d("Remote", "Remote Config Inited")
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            }
            firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
            firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_cofig_defaults)

            //val cacheExpiration: Long = 3600 // 1 hour in seconds.

            firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
                if (it.isSuccessful) {
                    val updated = it.result
                    Log.d(
                        "Remote",
                        "Remote : Before Banner adInterval is : ${prefManager.bannerAdInterval}"
                    )
                    prefManager.bannerAdInterval =
                        firebaseRemoteConfig.getString(Constants.BANNER_AD_INTERVAL).toLong()
                    prefManager.interAdInterval =
                        firebaseRemoteConfig.getString(Constants.INTER_AD_INTERVAL).toLong()
                    prefManager.adUnitIdBanner =
                        firebaseRemoteConfig.getString(Constants.REMOTE_CONFIG_VALUE_FOR_AD_UNIT_ID_BANNER)
                    prefManager.adUnitIdInterstitial =
                        firebaseRemoteConfig.getString(Constants.REMOTE_CONFIG_VALUE_FOR_AD_UNIT_ID_INTERSTITIAL)
                    prefManager.adUnitIdRewarded =
                        firebaseRemoteConfig.getString(Constants.REMOTE_CONFIG_VALUE_FOR_AD_UNIT_ID_REWARD)
                    prefManager.adUnitIdAppOpen =
                        firebaseRemoteConfig.getString(Constants.REMOTE_CONFIG_VALUE_FOR_AD_UNIT_ID_APPOPEN)
                    Log.d(
                        "Remote",
                        "Remote : Banner adInterval is : ${
                            firebaseRemoteConfig.getString(Constants.BANNER_AD_INTERVAL).toLong()
                        }"
                    )
                    Log.d(
                        "Remote",
                        "Remote : Inter adInterval is : ${
                            firebaseRemoteConfig.getString(Constants.INTER_AD_INTERVAL).toLong()
                        }"
                    )
                    Log.d(
                        "Remote",
                        "Remote : Banner ad unit ID is : ${firebaseRemoteConfig.getString(Constants.REMOTE_CONFIG_VALUE_FOR_AD_UNIT_ID_BANNER)}"
                    )

                } else {
                    Log.d("Remote", "fetch Failed")
                }
            }

        } else {
            Log.d("Remote", "fetch Will Fetch Tomorrow")
        }
    }


}