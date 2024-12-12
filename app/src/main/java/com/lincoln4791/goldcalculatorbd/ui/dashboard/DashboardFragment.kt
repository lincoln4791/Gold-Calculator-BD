package com.lincoln4791.goldcalculatorbd.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.MobileAds
import com.lincoln4791.dailyexpensemanager.common.util.CurrentDate
import com.lincoln4791.goldcalculatorbd.Constants
import com.lincoln4791.goldcalculatorbd.PrefManager
import com.lincoln4791.goldcalculatorbd.Utils
import com.lincoln4791.goldcalculatorbd.activities.DeveloperInfo
import com.lincoln4791.goldcalculatorbd.activities.DeveloperProfile
import com.lincoln4791.goldcalculatorbd.activities.DollerRate
import com.lincoln4791.goldcalculatorbd.activities.GoldPriceBangladesh
import com.lincoln4791.goldcalculatorbd.activities.GoldPriceInternational
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.AdMobUtil
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.BannerAddHelper
import com.lincoln4791.goldcalculatorbd.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private lateinit var binding : FragmentDashboardBinding
    private lateinit var prefManager : PrefManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager(requireContext())
        initAdMob()

        binding.cvInternationPrice.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(),GoldPriceInternational::class.java))
            //requireActivity().finish()
        }

        binding.cvLocalPrice.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(),GoldPriceBangladesh::class.java))
            //requireActivity().finish()
        }

        binding.cvDollarRateToday.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(),DollerRate::class.java))
            //requireActivity().finish()
        }

        binding.cvCheckUpdate.setOnClickListener {
            Utils.goToPlayStore(requireContext())
        }

        binding.cvDevInfo.setOnClickListener {
            //requireContext().startActivity(Intent(requireContext(),DeveloperInfo::class.java))
            //requireActivity().finish()
            requireContext().startActivity(Intent(requireContext(), DeveloperProfile::class.java))
        }

    }

    private fun initAdMob() {
        val  prefManager = PrefManager(requireContext())
        val lastAdShowDate = prefManager.lastBannerAdShownDB
        if (AdMobUtil.canBannerAdShow(requireContext(), lastAdShowDate,Constants.AD_TYPE_BANNER)) {
            Log.d("tag","Banner Ad Home will load")

            binding.adView.visibility = View.VISIBLE
            MobileAds.initialize(requireContext()) {
                val bannerAdHelper = BannerAddHelper()
                //binding.adView.adUnitId=prefManager.adUnitIdBanner
                bannerAdHelper.loadBannerAd(binding.adView) {
                    if (it) {
                        prefManager.lastBannerAdShownDB = System.currentTimeMillis()
                    }
                }
            }
        } else {
            binding.adView.visibility = View.GONE

            if(System.currentTimeMillis()-lastAdShowDate<0){
                prefManager.lastBannerAdShownDB=System.currentTimeMillis()
            }

            Log.d("tag","Banner Ad Home Not Shown")
        }
    }
}