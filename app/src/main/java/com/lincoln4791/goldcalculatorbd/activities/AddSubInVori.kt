package com.lincoln4791.goldcalculatorbd.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.cardview.widget.CardView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.lincoln4791.dailyexpensemanager.admobAdsUpdated.InterstistialAdHelper
import com.lincoln4791.dailyexpensemanager.common.util.CurrentDate
import com.lincoln4791.goldcalculatorbd.*
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.AdMobUtil
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.AdUnitIds
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.BannerAddHelper
import com.lincoln4791.goldcalculatorbd.databinding.ActivityWeightPluMinusBinding

class AddSubInVori : AppCompatActivity() {

    private lateinit var binding : ActivityWeightPluMinusBinding
    private lateinit var prefManager : PrefManager
    private lateinit var interAd: InterstistialAdHelper
    private var isAdLoaded = false
    private var mInterstitialAd: InterstitialAd? = null
    private var calculationType = "যোগ করুন(Addition)"
    private var calculationTypeIndex = 0
    private var vori = 0
    private var ana = 0
    private var roti = 0
    private var point = 0

    private var vori2 = 0
    private var ana2 = 0
    private var roti2 = 0
    private var point2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        prefManager = PrefManager(this)
        super.onCreate(savedInstanceState)
        binding = ActivityWeightPluMinusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initInterstitialAd()
        Utils.changeNavBarColor(this,this)
        enableBackButton()
        initAdMob()
        initSpinner()

        binding.cvGoldWeight.setOnClickListener {
                showVoriWeightDialog()
        }

        binding.cvGoldWeight2.setOnClickListener {
            showVoriWeightDialog2()
        }

        binding.cvCalculate.setOnClickListener {
            if (binding.tvGoldWeight.text.isEmpty()){
                binding.tvGoldWeight.error="স্বর্ণের ওজন(Weight of Gold)"
            }
            else if(binding.tvGoldWeight2.text.isEmpty()){
                binding.tvGoldWeight2.error="স্বর্ণের ওজন(Weight of Gold)"
            }
            else{
                //calculateVoriToVori(vori,ana,roti,point,vori2,ana2,roti2,point2)
                showInterAd()
            }
        }
    }

    private fun initSpinner() {
        val spinnerArray = arrayListOf("যোগ করুন(Addition)","বিয়োগ করুন(Subtraction)")
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this@AddSubInVori,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerArray)
        val spinnerIndex: Int
        when (calculationType) {
            "যোগ করুন(Addition)" -> {
                spinnerIndex = 0
            }
            "বিয়োগ করুন(Subtraction)" -> {
                spinnerIndex = 1
            }
            else -> {
                spinnerIndex=0
            }
        }
        binding.spinnerUnit.adapter = spinnerAdapter
        binding.spinnerUnit.setSelection(spinnerIndex)
        binding.spinnerUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long,
            ) {
                when (position) {
                    0 -> {
                        calculationType="যোগ করুন(Addition)"
                        calculationTypeIndex=0
                        refreshAllValues()
                    }
                    1 -> {
                        calculationType="বিয়োগ করুন(Subtraction)"
                        calculationTypeIndex=1
                        refreshAllValues()
                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                calculationType="যোগ করুন(Addition)"
            }
        }
    }

    private fun showVoriWeightDialog(){
        val dialog = Dialog(this)
        val dView = layoutInflater.inflate(R.layout.layout_gold_weight_vori,null,false)
        dialog.setContentView(dView)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val etVori = dView.findViewById<EditText>(R.id.etVori)
        val etAna = dView.findViewById<EditText>(R.id.etAna)
        val etRoti = dView.findViewById<EditText>(R.id.etRoti)
        val etPoint = dView.findViewById<EditText>(R.id.etPoint)
        val btnCalculate = dView.findViewById<CardView>(R.id.cv_calculate)


        btnCalculate.setOnClickListener {
            vori = if(etVori.text.toString().isEmpty()) 0 else etVori.text.toString().toInt()
            ana = if(etAna.text.toString().isEmpty()) 0 else etAna.text.toString().toInt()
            roti = if(etRoti.text.toString().isEmpty()) 0 else etRoti.text.toString().toInt()
            point = if(etPoint.text.toString().isEmpty()) 0 else etPoint.text.toString().toInt()
            if(ana>15){
                etAna.error="আনা সর্বোচ্চ ১৫ হতে পারবে"
            }

            else if(roti>5){
                etRoti.error="রতি সর্বোচ্চ ৫ হতে পারবে"
            }

            else if(point>9){
                etPoint.error="পয়েন্ট সর্বোচ্চ ৯ হতে পারবে"
            }

            else{
                dialog.dismiss()
                Log.d("tag","$vori , $ana, $roti, $point")
                binding.tvGoldWeight.text="$vori ভরি,$ana আনা,$roti রতি,$point পয়েন্ট"
            }
        }

    }


    private fun showVoriWeightDialog2(){
        val dialog = Dialog(this)
        val dView = layoutInflater.inflate(R.layout.layout_gold_weight_vori,null,false)
        dialog.setContentView(dView)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val etVori = dView.findViewById<EditText>(R.id.etVori)
        val etAna = dView.findViewById<EditText>(R.id.etAna)
        val etRoti = dView.findViewById<EditText>(R.id.etRoti)
        val etPoint = dView.findViewById<EditText>(R.id.etPoint)
        val btnCalculate = dView.findViewById<CardView>(R.id.cv_calculate)


        btnCalculate.setOnClickListener {
            vori2 = if(etVori.text.toString().isEmpty()) 0 else etVori.text.toString().toInt()
            ana2 = if(etAna.text.toString().isEmpty()) 0 else etAna.text.toString().toInt()
            roti2 = if(etRoti.text.toString().isEmpty()) 0 else etRoti.text.toString().toInt()
            point2 = if(etPoint.text.toString().isEmpty()) 0 else etPoint.text.toString().toInt()
            if(ana2>15){
                etAna.error="আনা সর্বোচ্চ ১৫ হতে পারবে"
            }

            else if(roti2>5){
                etRoti.error="রতি সর্বোচ্চ ৫ হতে পারবে"
            }

            else if(point2>9){
                etPoint.error="পয়েন্ট সর্বোচ্চ ৯ হতে পারবে"
            }

            else{
                dialog.dismiss()
                Log.d("tag","$vori , $ana, $roti, $point")
                binding.tvGoldWeight2.text="$vori ভরি,$ana আনা,$roti রতি,$point পয়েন্ট"
            }
        }



    }


    private fun calculateVoriToVori(vori: Int, ana: Int, roti: Int, point: Int, vori2: Int, ana2: Int, roti2: Int, point2: Int){
        val totalVori : kotlin.Double  = vori.toDouble()+(ana/16.0)+(roti/96.0)+(point/960)
        val totalVori2 : kotlin.Double  = vori2.toDouble()+(ana2/16.0)+(roti2/96.0)+(point2/960)

        if(calculationTypeIndex==0){
            val finalVori = totalVori+totalVori2
            breakDownVori(finalVori)
        }
        else{
            val finalVori = totalVori-totalVori2
            breakDownVori(finalVori)
        }

    }

    private fun breakDownVori(totalVori:Double) {
        var number = totalVori*960
        var finalPoint = 0
        var finalRoti = 0
        var finalAna = 0
        var finalVori = 0

        //var remaining=0

        // finding point
        if(number/10.0!=0.0){
            finalPoint=(number%10.0).toInt()
            number /= 10.0


            //finding roti
            if(number/6.0!=0.0){
                finalRoti=(number%6.0).toInt()
                number /= 6.0


                //finding ana
                if(number/16.0!=0.0){
                    finalAna=(number%16.0).toInt()
                    number /= 16.0

                    finalVori=number.toInt()

                }

            }

        }

        if(calculationTypeIndex==0){
            binding.tvFinalGoldPrice.text = "$vori ভরি $ana আনা $roti রতি $point পয়েন্ট স্বর্ণের সাথে $vori2 ভরি $ana2 আনা $roti2 রতি $point2 পয়েন্ট যোগ করলে মোট স্বর্ণের পরিমাণ হয়\n\n$finalVori ভরি,$finalAna আনা,$finalRoti রতি,$finalPoint পয়েন্ট"
        }
        else{
            binding.tvFinalGoldPrice.text = "$vori ভরি $ana আনা $roti রতি $point পয়েন্ট স্বর্ণের থেকে $vori2 ভরি $ana2 আনা $roti2 রতি $point2 পয়েন্ট বিয়োগ করলে মোট স্বর্ণের পরিমাণ হয়\n\n$finalVori ভরি,$finalAna আনা,$finalRoti রতি,$finalPoint পয়েন্ট"
        }


    }

    private fun refreshAllValues(){
        calculationTypeIndex=0
        vori = 0
        ana = 0
        roti = 0
        point = 0
        binding.tvGoldWeight.text = "স্বর্ণের ওজন(Gold Weight)"
        binding.tvGoldWeight2.text = "স্বর্ণের ওজন(Gold Weight)"
        binding.tvFinalGoldPrice.text = "ফলাফল(Result)"
    }

    private fun enableBackButton(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initAdMob() {
        val  prefManager = PrefManager(this)
        val lastAdShowDate = prefManager.lastBannerAdShownAdSub
        if (AdMobUtil.canBannerAdShow(this, lastAdShowDate,Constants.AD_TYPE_BANNER)) {
            Log.d("tag","Banner Ad Home will load")

            binding.adView.visibility = View.VISIBLE
            MobileAds.initialize(this) {
                val bannerAdHelper = BannerAddHelper(this)
                bannerAdHelper.loadBannerAd(binding.adView) {
                    if (it) {
                        prefManager.lastBannerAdShownAdSub = System.currentTimeMillis()
                    }
                }
            }
        } else {
            binding.adView.visibility = View.GONE

            if(System.currentTimeMillis()-lastAdShowDate<0){
                prefManager.lastBannerAdShownAdSub=System.currentTimeMillis()
            }

            Log.d("tag","Banner Ad Home Not Shown")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AddSubInVori, MainActivity::class.java))
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            startActivity(Intent(this@AddSubInVori, MainActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initInterstitialAd() {
        interAd = InterstistialAdHelper(this, this,mInterstitialAd)
        val lastAdShown = prefManager.lastInterstitialAdShown
        if (AdMobUtil.canBannerAdShow(this, lastAdShown,Constants.AD_TYPE_INTER)) {
            interAd.loadinterAd(AdUnitIds.INTERSTITIAL) {
                Log.d("InterAd", "Inter ad loaded -> $it")
                isAdLoaded = it
            }
        }
        else{
            if(System.currentTimeMillis()-lastAdShown<0){
                prefManager.lastInterstitialAdShown=System.currentTimeMillis()
            }
        }
    }


    private fun showInterAd() {
        if (isAdLoaded) {
            Log.d("InterAD", "InterAd Loaded")
            interAd.showInterAd { isShown: Boolean, error: String? ->
                if (isShown) {
                    Log.d("InterAD", "InterAd has been shown")
                    prefManager.lastInterstitialAdShown = System.currentTimeMillis()
                    isAdLoaded = false
                    calculateVoriToVori(vori,ana,roti,point,vori2,ana2,roti2,point2)
                } else {
                    Log.d("InterAD", "InterAd Not been shown->$error")
                    calculateVoriToVori(vori,ana,roti,point,vori2,ana2,roti2,point2)
                }
            }
        } else {
            Log.d("InterAD", "InterAd Not Loaded yet")
            calculateVoriToVori(vori,ana,roti,point,vori2,ana2,roti2,point2)
        }
    }

}