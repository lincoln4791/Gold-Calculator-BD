/*
import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.AdUnitIds
import java.util.Date

class AppOpenAdManager(private val application: Application) : Application.ActivityLifecycleCallbacks {
    private var loadTime: Long = 0;
    private var appOpenAd: AppOpenAd? = null
    private var isAdShowing = false
    private var isLoadingAd = false

    init {
        application.registerActivityLifecycleCallbacks(this)
    }

    // Load the App Open Ad
    fun loadAd() {

        if (isLoadingAd || isAdAvailable()) {
            return
        }

        isLoadingAd = true

        val adRequest = AdRequest.Builder().build()
        AppOpenAd.load(
            application,
            AdUnitIds.APP_OPEN, // Replace with your App Open Ad Unit ID
            adRequest,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isLoadingAd = false
                    loadTime = Date().time
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    appOpenAd = null
                    isLoadingAd = false;
                }

            }
        )
    }

    // Show the Ad if available
    fun showAdIfAvailable(activity: Activity) {
        if (isAdShowing || appOpenAd == null) return

        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                isAdShowing = false
                loadAd() // Reload after dismissing
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                isAdShowing = false
            }

            override fun onAdShowedFullScreenContent() {
                isAdShowing = true
                appOpenAd = null // Prevent reuse
            }
        }
        appOpenAd?.show(activity)
    }


    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference: Long = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    private fun isAdAvailable(): Boolean {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
    }
    // Activity Lifecycle Callbacks
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        showAdIfAvailable(activity)
    }
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
*/
