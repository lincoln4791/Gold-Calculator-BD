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
import com.lincoln4791.goldcalculatorbd.databinding.ActivityGoldBuyPriceBinding
import kotlin.math.roundToInt

class GoldBuyPrice : AppCompatActivity() {
    private lateinit var prefManager : PrefManager
    private lateinit var interAd: InterstistialAdHelper
    private var mInterstitialAd: InterstitialAd? = null
    private var isAdLoaded = false
    private var unit = "প্রতি ভরি(Per Bhori)"
    private var unitIndex = 0
    private lateinit var binding : ActivityGoldBuyPriceBinding
    private var gram = 0.0
    private var vori = 0
    private var ana = 0
    private var roti = 0
    private var point = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        prefManager = PrefManager(this)
        super.onCreate(savedInstanceState)
        binding = ActivityGoldBuyPriceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initInterstitialAd()
        Utils.changeNavBarColor(this,this)
        enableBackButton()
        initSpinner()
        initAdMob()

        binding.cvGoldWeight.setOnClickListener {
            if(unitIndex==0){
                showVoriWeightDialog()
            }
            else if(unitIndex==1){
                showGramWeightDialog()
            }
            else{

            }

        }

        binding.cvCalculate.setOnClickListener {
            if(binding.etUnitPrice.text.isEmpty()){
                binding.etUnitPrice.error="স্বর্ণের দাম(Price of Gold)"
            }
            else if (binding.tvGoldWeight.text.isEmpty()){
                binding.tvGoldWeight.error="স্বর্ণের ওজন(Weight of Gold)"
            }
            else{
                showInterAd()

            }

        }

    }

    private fun initSpinner() {
        val spinnerArray = arrayListOf("প্রতি ভরি(Per Bhori)","প্রতি গ্রাম(Per Gram)")
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this@GoldBuyPrice,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerArray)
        val spinnerIndex: Int
        when (unit) {
            "প্রতি ভরি" -> {
                spinnerIndex = 0
            }
            "প্রতি গ্রাম" -> {
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
                        unit="প্রতি ভরি"
                        unitIndex=0
                        refreshAllValues()
                    }
                    1 -> {
                        unit="প্রতি গ্রাম"
                        unitIndex=1
                        refreshAllValues()
                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                unit="প্রতি ভরি"
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

    private fun showGramWeightDialog(){
        val dialog = Dialog(this)
        val dView = layoutInflater.inflate(R.layout.layout_gold_weight_gram,null,false)
        dialog.setContentView(dView)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val etVori = dView.findViewById<EditText>(R.id.etGram)
        val btnCalculate = dView.findViewById<CardView>(R.id.cv_calculate)


        btnCalculate.setOnClickListener {
            gram = if(etVori.text.toString().isEmpty()) 0.0 else etVori.text.toString().toDouble()
            dialog.dismiss()
            Log.d("tag","$gram Gram")
            binding.tvGoldWeight.text="$gram গ্রাম(Gram)"

        }

    }

    private fun calculatePriceVori(vori:Int, ana:Int, roti:Int, point:Int, pricePerVori : Int) {
        val totalVori : kotlin.Double  = vori.toDouble()+(ana/16.0)+(roti/96.0)+(point/960)
        val totalPrice  = totalVori*pricePerVori
        val totalPriceWithTax  = totalVori*pricePerVori*1.05
        Log.d("tag","price ${totalPrice} :: quantity -> $totalVori")

        val tPriceInBangla = Utils.getBanglaDigitFromEnglishDigit(totalPrice.roundToInt().toString())
        val tPriceWithTaxInBangla = Utils.getBanglaDigitFromEnglishDigit(totalPriceWithTax.roundToInt().toString())

        binding.tvFinalGoldPrice.text = "প্রতি ভরি স্বর্ণের দাম $pricePerVori টাকা দরে $vori ভরি,$ana আনা,$roti রতি,$point পয়েন্ট স্বর্ণের দাম\n${Utils.putCommaInNumber(tPriceInBangla)} টাকা।\n৫% ট্যাক্স সহ\n${Utils.putCommaInNumber(tPriceWithTaxInBangla)} টাকা\n\nConsidering $pricePerVori per Bhori price, $vori Bhori, $ana Ana, $roti Roti and $point Point gold price is ${totalPrice.roundToInt()} BDT \nIncluding 5% tax, price is\n${totalPriceWithTax.roundToInt()} BDT"
    }

    private fun calculatePriceGram(gram:Double, pricePerGram : Double) {
        val totalGram : kotlin.Double  = vori.toDouble()+(ana/16.0)+(roti/96.0)+(point/960)
        val totalPrice  = gram*pricePerGram
        val totalPriceWithTax  = totalPrice*1.05
        Log.d("tag","price $totalPrice :: quantity -> $totalGram Gram")

        val tPriceInBangla = Utils.getBanglaDigitFromEnglishDigit(totalPrice.roundToInt().toString())
        val tPriceInBanglaWithTax = Utils.getBanglaDigitFromEnglishDigit(totalPriceWithTax.roundToInt().toString())

        binding.tvFinalGoldPrice.text = "প্রতি গ্রাম স্বর্ণের দাম $pricePerGram টাকা দরে $gram গ্রাম স্বর্ণের দাম\n${Utils.putCommaInNumber(tPriceInBangla)} টাকা।\n৫% ট্যাক্স সহ\n${Utils.putCommaInNumber(tPriceInBanglaWithTax)} টাকা\n\nConsidering $pricePerGram per Gram price, $gram gram gold price is ${totalPrice.roundToInt()} BDT \nIncluding 5% tax, price is\n${totalPriceWithTax.roundToInt()} BDT"
    }

    private fun refreshAllValues(){
        gram = 0.0
        vori = 0
        ana = 0
        roti = 0
        point = 0
        binding.etUnitPrice.setText("")
        binding.tvGoldWeight.text = "স্বর্ণের ওজন(Gold Weight)"
        binding.tvFinalGoldPrice.text = "ফলাফল(Result)"
    }

    private fun enableBackButton(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@GoldBuyPrice,MainActivity::class.java))
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            startActivity(Intent(this@GoldBuyPrice,MainActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initAdMob() {
        val  prefManager = PrefManager(this)
        val lastAdShowDate = prefManager.lastBannerAdShownGBP
        if (AdMobUtil.canBannerAdShow(this, lastAdShowDate,Constants.AD_TYPE_BANNER)) {
            Log.d("tag","Banner Ad Home will load")

            binding.adView.visibility = View.VISIBLE
            MobileAds.initialize(this) {
                val bannerAdHelper = BannerAddHelper()
                //binding.adView.adUnitId=prefManager.adUnitIdBanner
                bannerAdHelper.loadBannerAd(binding.adView) {
                    if (it) {
                        prefManager.lastBannerAdShownGBP = System.currentTimeMillis()
                    }
                }
            }
        } else {
            binding.adView.visibility = View.GONE

            if(System.currentTimeMillis()-lastAdShowDate<0){
                prefManager.lastBannerAdShownGBP=System.currentTimeMillis()
            }

            Log.d("tag","Banner Ad Home Not Shown")
        }
    }


    private fun initInterstitialAd() {
        interAd = InterstistialAdHelper(this, this,mInterstitialAd)
        val lastAdShown = prefManager.lastInterstitialAdShown
        if (AdMobUtil.canBannerAdShow(this, lastAdShown,Constants.AD_TYPE_INTER)) {
            interAd.loadinterAd(prefManager.adUnitIdInterstitial) {
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
                    initCalculation()
                } else {
                    Log.d("InterAD", "InterAd Not been shown->$error")
                    initCalculation()
                }
            }
        } else {
            Log.d("InterAD", "InterAd Not Loaded yet")
            initCalculation()
        }
    }

    private fun initCalculation(){
        if(unitIndex==0){
            calculatePriceVori(vori,ana,roti,point,binding.etUnitPrice.text.toString().toInt())
        }
        else if (unitIndex==1){
            calculatePriceGram(gram,binding.etUnitPrice.text.toString().toDouble())
        }
    }

}