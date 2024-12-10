package com.lincoln4791.goldcalculatorbd
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.AdUnitIds
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.GlobalAds.rewardedAd
import com.lincoln4791.goldcalculatorbd.common.FirebaseUtil.fetchCommonDataFromRemoteConfig
import com.lincoln4791.goldcalculatorbd.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    ;
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager : PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


      prefManager = PrefManager(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar?.apply {
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(false)
            setCustomView(R.layout.custom_home_action_bar) // Use your layout here
        }
        val customImage = actionBar?.customView?.findViewById<LottieAnimationView>(R.id.lottieAnimationView)
        customImage?.setOnClickListener {
            confirmWatchAd()
        }

        if(rewardedAd==null){
            loadRewardedAd()
        }

        Utils.changeNavBarColor(this,this)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home,R.id.more))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        fetchCommonDataFromRemoteConfig(application.applicationContext);
    }


    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            this,
            prefManager.adUnitIdRewarded,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d("tag", "Rewarded ad loaded.")
                    rewardedAd = ad
                }

                override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                    Log.d("tag", "Failed to load rewarded ad: ${error.message}")
                    rewardedAd = null
                }
            }
        )
    }


    private fun showRewardedAd() {
        if (rewardedAd != null) {
            rewardedAd?.show(this) { rewardItem: RewardItem ->
                // Handle the reward
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type
                Log.d("tag", "User rewarded with $rewardAmount $rewardType")
            }

            rewardedAd?.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("tag", "Ad dismissed.")
                    rewardedAd = null
                    loadRewardedAd() // L
                    showWatchAdThanksGivingDialog()// oad a new ad
                }

                override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                    Log.d("tag", "Ad failed to show: ${adError.message}")
                    rewardedAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d("tag", "Ad showed fullscreen content.")
                }
            }
        } else {
            Log.d("tag", "Rewarded ad is not ready yet.")
        }
    }


    private fun confirmWatchAd(){
        val dialog = Dialog(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_confirm_watch_ad,null,false)
        dialog.setContentView(dialogView)
        dialog.show()

        dialog.findViewById<ImageView>(R.id.ivClose).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<Button>(R.id.btnWatchAd).setOnClickListener {
            dialog.dismiss()
          showRewardedAd()
        }
    }

    private fun showWatchAdThanksGivingDialog(){
        val dialog = Dialog(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_watch_ad_thanks_giving,null,false)
        dialog.setContentView(dialogView)
        dialog.show()

        dialog.findViewById<ImageView>(R.id.ivClose).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<Button>(R.id.btnWatchAd).setOnClickListener {
            dialog.dismiss()
        }
    }


}