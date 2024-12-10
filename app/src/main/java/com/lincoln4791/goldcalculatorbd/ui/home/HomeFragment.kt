package com.lincoln4791.goldcalculatorbd.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.lincoln4791.goldcalculatorbd.common.NetworkCheck
import com.lincoln4791.goldcalculatorbd.common.VersionControl
import com.lincoln4791.goldcalculatorbd.Constants
import com.lincoln4791.goldcalculatorbd.PrefManager
import com.lincoln4791.goldcalculatorbd.activities.AddSubInVori
import com.lincoln4791.goldcalculatorbd.activities.GoldBuyPrice
import com.lincoln4791.goldcalculatorbd.activities.GoldSellPrice
import com.lincoln4791.goldcalculatorbd.activities.WeightConversion
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.AdMobUtil
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.AdUnitIds
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.BannerAddHelper
import com.lincoln4791.goldcalculatorbd.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var rewardedAd: RewardedAd? = null
    private lateinit var binding : FragmentHomeBinding
    private lateinit var prefManager : PrefManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager(requireContext())
        initAdMob()
        loadRewardedAd()
        initCheckAppVersion()

        binding.cvGoldBuyPrice.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(),GoldBuyPrice::class.java))
            requireActivity().finish()
        }

        binding.cvGoldSellPrice.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(), GoldSellPrice::class.java))
            requireActivity().finish()
        }

        binding.cvConversion.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(), WeightConversion::class.java))
            requireActivity().finish()
        }

        binding.cvAddSubInVori.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(),AddSubInVori::class.java))
            requireActivity().finish()
        }

        binding.ivTitle.setOnClickListener{
            showRewardedAd()
        }



    }

    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            requireContext(),
            AdUnitIds.REWARDED_AD_TEST, // Test Ad Unit ID
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
            rewardedAd?.show(requireActivity()) { rewardItem: RewardItem ->
                // Handle the reward
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type
                Log.d("tag", "User rewarded with $rewardAmount $rewardType")
            }

            rewardedAd?.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("tag", "Ad dismissed.")
                    rewardedAd = null
                    loadRewardedAd() // Load a new ad
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


    private fun initAdMob() {
        val  prefManager = PrefManager(requireContext())
        val lastAdShowDate = prefManager.lastBannerAdShownHomeF
        if (AdMobUtil.canBannerAdShow(requireContext(), lastAdShowDate,Constants.AD_TYPE_BANNER)) {
            Log.d("tag","Banner Ad Home will load")

            binding.adView.visibility = View.VISIBLE
            MobileAds.initialize(requireContext()) {
                val bannerAdHelper = BannerAddHelper(requireContext())
                bannerAdHelper.loadBannerAd(binding.adView) {
                    if (it) {
                        prefManager.lastBannerAdShownHomeF = System.currentTimeMillis()
                    }
                }
            }
        } else {
            binding.adView.visibility = View.GONE

            if(System.currentTimeMillis()-lastAdShowDate<0){
                prefManager.lastBannerAdShownHomeF=System.currentTimeMillis()
            }

            Log.d("tag","Banner Ad Home Not Shown")
        }
    }


    fun initCheckAppVersion() {
        if(System.currentTimeMillis()-prefManager.lastAppVersionRemoteConfigDataFetchTime>= Constants.INTERVAL_DAILY){
            if (NetworkCheck.isConnect(requireContext())) {
                //VersionControl.checkVersion(requireContext())
                VersionControl.checkVersion(requireContext())
                Log.d("appVersion", "VC checked")
            } else {
                Log.d("appVersion", "No Internet")
            }
        }
        else{
            Log.d("appVersion","App Version Will Check Tomorrow")
        }
    }


}