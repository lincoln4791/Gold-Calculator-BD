package com.lincoln4791.goldcalculatorbd.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.MobileAds
import com.lincoln4791.goldcalculatorbd.Constants
import com.lincoln4791.goldcalculatorbd.PrefManager
import com.lincoln4791.goldcalculatorbd.activities.AddSubInVori
import com.lincoln4791.goldcalculatorbd.activities.GoldBuyPrice
import com.lincoln4791.goldcalculatorbd.activities.GoldSellPrice
import com.lincoln4791.goldcalculatorbd.activities.WeightConversion
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.AdMobUtil
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.BannerAddHelper
import com.lincoln4791.goldcalculatorbd.common.NetworkCheck
import com.lincoln4791.goldcalculatorbd.common.VersionControl
import com.lincoln4791.goldcalculatorbd.databinding.FragmentHomeBinding
import com.lincoln4791.goldcalculatorbd.ui.TempActivity

class HomeFragment : Fragment() {


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
        prefManager = PrefManager(requireActivity().applicationContext)
        initAdMob()
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
            requireContext().startActivity(Intent(requireContext(), TempActivity::class.java))
        }


    }




    private fun initAdMob() {
        val lastAdShowDate = prefManager.lastBannerAdShownHomeF
        if (AdMobUtil.canBannerAdShow(requireContext(), lastAdShowDate,Constants.AD_TYPE_BANNER)) {
            Log.d("tag","Banner Ad Home will load")

            binding.adView.visibility = View.VISIBLE
            MobileAds.initialize(requireContext()) {
                val bannerAdHelper = BannerAddHelper()
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
        if(System.currentTimeMillis()-prefManager.lastAppVersionRemoteConfigDataFetchTime>= Constants.INTERVAL_SIX_HOUR){
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