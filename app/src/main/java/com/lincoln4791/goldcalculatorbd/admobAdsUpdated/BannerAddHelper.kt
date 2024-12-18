package com.lincoln4791.goldcalculatorbd.admobAdsUpdated

import android.util.Log
import com.google.android.gms.ads.*

class BannerAddHelper() {

    fun loadBannerAd(mAdView : AdView,addUnitID:String?=null, callBack:(isShown:Boolean)->Unit){
        //MobileAds.initialize(context)
        addUnitID?.let {
            mAdView.setAdUnitId(it)
        }

        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        mAdView.adListener = object: AdListener(){
            override fun onAdLoaded() {
                //Toast.makeText(this@MainActivity, "Add clicked",Toast.LENGTH_SHORT).show()
                Log.d("Banner","Banner Loaded")
                callBack(true)

            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
                Log.d("Banner","Banner Failed -> error -> ${adError.responseInfo}:::${adError.message}")
                callBack(false)
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d("Banner","Banner Opened")
            }

            override fun onAdClicked() {
                //Toast.makeText(context, "Add clicked", Toast.LENGTH_SHORT).show()
                Log.d("Banner","banner Clicked")
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                //Toast.makeText(context, "Add clicked", Toast.LENGTH_SHORT).show()
                Log.d("Banner","banner Closed")

            }
        }
    }

}
