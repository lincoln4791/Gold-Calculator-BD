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

class FirebaseUtil {

    companion object{
        fun fetchCommonDataFromRemoteConfig(context:Context){
             val prefManager = PrefManager(context)

            if(System.currentTimeMillis()-prefManager.lastCommonRemoteConfigDataFetchTime>= Constants.INTERVAL_DAILY){

                val firebaseRemoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
                Log.d("Remote","Remote Config Inited")
                val configSettings  = remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 3600
                }
                firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
                firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_cofig_defaults)

                //val cacheExpiration: Long = 3600 // 1 hour in seconds.

                firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val updated = it.result
                        prefManager.bannerAdInterval = firebaseRemoteConfig.getString(Constants.BANNER_AD_INTERVAL).toLong()
                        prefManager.interAdInterval = firebaseRemoteConfig.getString(Constants.INTER_AD_INTERVAL).toLong()
                        Log.d("Remote", "Remote : adInterval is : ${prefManager.bannerAdInterval}")

                    } else {
                        Log.d("Remote", "fetch Failed")
                    }
                }

            }
            else{
                Log.d("Remote", "fetch Will Fetch Tomorrow")
            }
        }
    }

}