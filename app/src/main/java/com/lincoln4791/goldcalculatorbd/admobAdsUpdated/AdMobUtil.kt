package com.lincoln4791.goldcalculatorbd.admobAdsUpdated

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.lincoln4791.dailyexpensemanager.common.util.CurrentDate
import com.lincoln4791.dailyexpensemanager.common.util.NetworkCheck
import com.lincoln4791.goldcalculatorbd.BuildConfig
import com.lincoln4791.goldcalculatorbd.Constants
import com.lincoln4791.goldcalculatorbd.PrefManager
import java.text.SimpleDateFormat
import kotlin.math.abs

class AdMobUtil {
    companion object{
        private val tag = "AdMob"

        private val TEST_AD_ID = "ca-app-pub-3940256099942544~3347511713"


        private val testDeviceIds = listOf<String>("c86b65ba-aa79-4913-8900-ad506c4ce57a","2831bbff-d301-4ff9-90a7-a41cfa7b7c5a",
        "f26dd6c3-ae53-4470-880b-ebd4147e12e9","9ce9d179-cbbf-4d5e-a340-c57ef3836adc","2b461cf6-e0f6-40ea-8283-a4f1e040d8a4",
        "9e2891e0-0a37-455a-8400-0016a9314c6d")

        @SuppressLint("SimpleDateFormat")
        fun diffTime(time: String): Long {
            var min: Long = 0
            val difference: Long
            try {
                val simpleDateFormat = SimpleDateFormat("hh:mm aa") // for 12-hour system, hh should be used instead of HH
                // There is no minute different between the two, only 8 hours difference. We are not considering Date, So minute will always remain 0
                val date1 = simpleDateFormat.parse(time)
                val date2 = simpleDateFormat.parse(CurrentDate.currentTime24H)
                difference = (date2?.time!! - date1?.time!!) / 1000
                val hours = difference % (24 * 3600) / 3600 // Calculating Hours
                val minute = difference % 3600 / 60 // Calculating minutes if there is any minutes difference
                min = minute + hours * 60 // This will be our final minutes. Multiplying by 60 as 1 hour contains 60 mins
            } catch (e: Throwable) {
                Log.d(tag,"exception in getting dff time of can ad show method -> ${e.message}")
                e.printStackTrace()
            }
            Log.d("AdMob", "$min of difference")
            return abs(min)
        }


        fun canBannerAdShow(context: Context, lastAdShownDate: Long,adType:String): Boolean {
            val prefManager = PrefManager(context)
            var canAdShow = false
            val interval = when (adType) {
                Constants.AD_TYPE_BANNER -> {
                    prefManager.bannerAdInterval
                }
                Constants.AD_TYPE_INTER -> {
                    prefManager.interAdInterval
                }
                else -> {
                    prefManager.bannerAdInterval
                }
            }

            if (NetworkCheck.isConnect(context)) {
                    if (System.currentTimeMillis()-lastAdShownDate >= interval) {
                        canAdShow=true
                    //if (diffTime(lastAdShownDate).toInt() >=0) {

                        info(tag,
                            " Ad shown because difference(${System.currentTimeMillis()-lastAdShownDate}) is greater than ${prefManager.bannerAdInterval}")
                    }
                    else {
                        info(tag,
                            " AD not loaded because difference ${System.currentTimeMillis()-lastAdShownDate} is less than ${prefManager.bannerAdInterval}")
                        canAdShow = false
                    }



            } else {
                info(tag,"No Internet")
                canAdShow = false

            }


            return canAdShow
        }


        fun isAppInstalledMinimumThreeDaysAgo(context: Context) : Boolean{
            var isInstalled = false
            val prefManager = PrefManager(context)
            val dif = diffTime(prefManager.appInstallDate)

            Log.d("AdMob"," Inter AD diff is -> $dif ")

            if(dif>3){
                isInstalled = true
            }

            return isInstalled
        }

        fun info(tag: String, msg: String){
            if (BuildConfig.DEBUG) Log.i(tag, msg)
        }


        fun setTestDevices(){
            val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
            MobileAds.setRequestConfiguration(configuration)
        }
    }



}