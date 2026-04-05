package com.lincoln4791.goldcalculatorbd
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.GlobalAds.rewardedInterstitialAd
import com.lincoln4791.goldcalculatorbd.common.FirebaseUtil.fetchCommonDataFromRemoteConfig
import com.lincoln4791.goldcalculatorbd.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager : PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        prefManager = PrefManager(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        if(rewardedInterstitialAd==null){
            loadRewardedInterstitialAd()
        }

        // Utils.changeNavBarColor(this,this) // Removed as edge-to-edge handles this better
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home,R.id.more))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        fetchCommonDataFromRemoteConfig(application.applicationContext);
    }


    private fun loadRewardedInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedInterstitialAd.load(
            this,
            prefManager.adUnitIdRewardedInterstitial,
            adRequest,
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    super.onAdLoaded(ad)
                    rewardedInterstitialAd = ad
                }

                override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                    Log.d("tag", "Failed to load rewarded ad: ${error.message}")
                    rewardedInterstitialAd = null
                }
            }
        )
    }


    private fun showRewardedAd() {
        if (rewardedInterstitialAd != null) {
            rewardedInterstitialAd?.show(this) { rewardItem: RewardItem ->
                // Handle the reward
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type
                Log.d("tag", "User rewarded with $rewardAmount $rewardType")
            }

            rewardedInterstitialAd?.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("tag", "Ad dismissed.")
                    rewardedInterstitialAd = null
                    loadRewardedInterstitialAd() // L
                    showWatchAdThanksGivingDialog()// oad a new ad
                }

                override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                    Log.d("tag", "Ad failed to show: ${adError.message}")
                    rewardedInterstitialAd = null
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
