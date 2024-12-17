package com.lincoln4791.goldcalculatorbd
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.AdUnitIds
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
@HiltAndroidApp
class MyApplication : Application(), Application.ActivityLifecycleCallbacks,LifecycleObserver  {
    private var currentActivity: Activity? = null
    private lateinit var appOpenAdManager: AppOpenAdManager
    override fun onCreate() {
        super.onCreate()
   /*     MobileAds.initialize(this)
        appOpenAdManager = AppOpenAdManager(this)
        appOpenAdManager.loadAd()*/


        registerActivityLifecycleCallbacks(this)
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            MobileAds.initialize(this@MyApplication) {}
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(this@MyApplication)
        appOpenAdManager = AppOpenAdManager()
    }


    /** LifecycleObserver method that shows the app open ad when the app moves to foreground. */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.
        currentActivity?.let {
            appOpenAdManager.showAdIfAvailable(it,object : OnShowAdCompleteListener{
                override fun onShowAdComplete() {

                }

            })
        }
    }

    /** Show the ad if one isn't already showing. */
/*    fun showAdIfAvailable(activity: Activity) {
        showAdIfAvailable(
            activity,
            )
    }*/

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(p0: Activity) {

    }

    override fun onActivityPaused(p0: Activity) {

    }

    override fun onActivityStopped(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }

    override fun onActivityDestroyed(p0: Activity) {

    }

    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }


    private inner class AppOpenAdManager {
        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false
        private var loadTime: Long = 0;

        fun loadAd(context: Context) {
            // Do not load ad if there is an unused ad or one is already loading.
            val prefManager = PrefManager(context)
            if (isLoadingAd || isAdAvailable()) {
                return
            }

            isLoadingAd = true
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                context, prefManager.adUnitIdAppOpen, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                object : AppOpenAdLoadCallback() {

                    override fun onAdLoaded(ad: AppOpenAd) {
                        // Called when an app open ad has loaded.
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
                        Log.d("tag", "app open add loaded")
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Called when an app open ad has failed to load.
                        Log.d("tag", loadAdError.message)
                        isLoadingAd = false;
                    }
                })
        }


        /** Check if ad exists and can be shown. */
        private fun isAdAvailable(): Boolean {
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
        }

        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
            val dateDifference: Long = Date().time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return dateDifference < numMilliSecondsPerHour * numHours
        }


        fun showAdIfAvailable(
            activity: Activity,
            onShowAdCompleteListener: OnShowAdCompleteListener) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.d("tag", "The app open ad is already showing.")
                return
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                Log.d("tag", "The app open ad is not ready yet.")
                onShowAdCompleteListener.onShowAdComplete()
                loadAd(activity)
                return
            }

            appOpenAd?.setFullScreenContentCallback(
                object : FullScreenContentCallback() {

                    override fun onAdDismissedFullScreenContent() {
                        // Called when full screen content is dismissed.
                        // Set the reference to null so isAdAvailable() returns false.
                        Log.d("tag", "Ad dismissed fullscreen content.")
                        appOpenAd = null
                        isShowingAd = false

                        onShowAdCompleteListener.onShowAdComplete()
                        loadAd(activity)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        // Called when fullscreen content failed to show.
                        // Set the reference to null so isAdAvailable() returns false.
                        Log.d("tag", adError.message)
                        appOpenAd = null
                        isShowingAd = false

                        onShowAdCompleteListener.onShowAdComplete()
                        loadAd(activity)
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        Log.d("tag", "Ad showed fullscreen content.")
                    }
                })
            isShowingAd = true
            appOpenAd?.show(activity)
        }


    }

}
