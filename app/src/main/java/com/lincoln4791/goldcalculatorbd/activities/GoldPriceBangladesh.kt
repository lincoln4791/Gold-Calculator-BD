package com.lincoln4791.goldcalculatorbd.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.google.android.gms.ads.MobileAds
import com.lincoln4791.dailyexpensemanager.common.util.CurrentDate
import com.lincoln4791.goldcalculatorbd.Constants
import com.lincoln4791.goldcalculatorbd.MainActivity
import com.lincoln4791.goldcalculatorbd.PrefManager
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.AdMobUtil
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.BannerAddHelper
import com.lincoln4791.goldcalculatorbd.databinding.ActivityGoldPriceBangladeshBinding

class GoldPriceBangladesh : AppCompatActivity() {
    private lateinit var binding : ActivityGoldPriceBangladeshBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoldPriceBangladeshBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableBackButton()
        initAdMob()


        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl("https://www.livepriceofgold.com/bangladesh-gold-price.html")

        val webSettings: WebSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true

    }

    private fun enableBackButton(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack();
        } else {
            super.onBackPressed()
          /*  startActivity(Intent(this@GoldPriceBangladesh, MainActivity::class.java))
            finish()*/
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
           /* startActivity(Intent(this@GoldPriceBangladesh, MainActivity::class.java))
            finish()*/
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }



    private fun initAdMob() {
        val  prefManager = PrefManager(this)
        val lastAdShowDate = prefManager.lastBannerAdShownGPI
        if (AdMobUtil.canBannerAdShow(this, lastAdShowDate,Constants.AD_TYPE_BANNER)) {
            Log.d("tag","Banner Ad Home will load")

            binding.adView.visibility = View.VISIBLE
            MobileAds.initialize(this) {
                val bannerAdHelper = BannerAddHelper(this)
                bannerAdHelper.loadBannerAd(binding.adView) {
                    if (it) {
                        prefManager.lastBannerAdShownGPI = System.currentTimeMillis()
                    }
                }
            }
        } else {
            binding.adView.visibility = View.GONE

            if(System.currentTimeMillis()-lastAdShowDate<0){
                prefManager.lastBannerAdShownGPI=System.currentTimeMillis()
            }

            Log.d("tag","Banner Ad Home Not Shown")
        }
    }

}