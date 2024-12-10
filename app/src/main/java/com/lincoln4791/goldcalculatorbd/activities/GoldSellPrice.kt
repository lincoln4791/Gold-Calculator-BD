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
import com.lincoln4791.goldcalculatorbd.databinding.ActivityGoldSellPriceBinding
import kotlin.math.roundToInt

class GoldSellPrice : AppCompatActivity() {
    private lateinit var prefManager : PrefManager
    private lateinit var interAd: InterstistialAdHelper
    private var mInterstitialAd: InterstitialAd? = null
    private var isAdLoaded = false
    private var unit = "প্রতি ভরি(Per Bhori)"
    private var unitIndex = 0
    private var cutPercentIndex = 14
    private var cutPercentValue = 15
    private lateinit var binding : ActivityGoldSellPriceBinding
    private var gram = 0.0
    private var vori = 0
    private var ana = 0
    private var roti = 0
    private var point = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefManager = PrefManager(this)
        binding = ActivityGoldSellPriceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initInterstitialAd()
        Utils.changeNavBarColor(this,this)
        enableBackButton()
        initAdMob()
        initSpinner()
        initcutPercentSpinner()

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
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this@GoldSellPrice,
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

    private fun initcutPercentSpinner() {
        val spinnerArray = arrayListOf("1%","2%","3%","4%","5%","6%","7%","8%","9%","10%","11%","12%","13%","14%","15%","16%","17%","18%","19%","20%")
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this@GoldSellPrice,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerArray)
        binding.spinnerCutPercent.setSelection(14)
        val spinnerIndex: Int
        when (unit) {
            "1%" -> {
                spinnerIndex = 0
                cutPercentValue=1
            }
            "2%" -> {
                spinnerIndex = 1
                cutPercentValue=2
            }
            "3%" -> {
                spinnerIndex = 2
                cutPercentValue=3
            }
            "4%" -> {
                spinnerIndex = 3
                cutPercentValue=4
            }
            "5%" -> {
                spinnerIndex = 4
                cutPercentValue=5
            }
            "6%" -> {
                spinnerIndex = 5
                cutPercentValue=6
            }
            "7%" -> {
                spinnerIndex = 6
                cutPercentValue=7
            }
            "8%" -> {
                spinnerIndex = 7
                cutPercentValue=8
            }
            "9%" -> {
                spinnerIndex = 8
                cutPercentValue=9
            }
            "10%" -> {
                spinnerIndex = 9
                cutPercentValue=10
            }
            "11%" -> {
                spinnerIndex = 10
                cutPercentValue=11
            }
            "12%" -> {
                spinnerIndex = 11
                cutPercentValue=12
            }
            "13%" -> {
                spinnerIndex = 12
                cutPercentValue=13
            }
            "14%" -> {
                spinnerIndex = 13
                cutPercentValue=14
            }
            "15%" -> {
                spinnerIndex = 14
                cutPercentValue=15
            }
            "16%" -> {
                spinnerIndex = 15
                cutPercentValue=16
            }
            "17%" -> {
                spinnerIndex = 16
                cutPercentValue=17
            }
            "18%" -> {
                spinnerIndex = 17
                cutPercentValue=18
            }
            "19%" -> {
                spinnerIndex = 18
                cutPercentValue=19
            }
            "20%" -> {
                spinnerIndex = 19
                cutPercentValue=20
            }
            else -> {
                spinnerIndex=14
                cutPercentValue=15
            }
        }
        binding.spinnerCutPercent.adapter = spinnerAdapter
        binding.spinnerCutPercent.setSelection(spinnerIndex)
        binding.spinnerCutPercent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long,
            ) {
                when (position) {
                    0 -> {
                        cutPercentIndex=0
                        cutPercentValue=1
                    }
                    1 -> {
                        cutPercentIndex=1
                        cutPercentValue=2
                    }
                    2 -> {
                        cutPercentIndex=2
                        cutPercentValue=3
                    }
                    3 -> {
                        cutPercentIndex=3
                        cutPercentValue=4
                    }
                    4 -> {
                        cutPercentIndex=4
                        cutPercentValue=5
                    }
                    5 -> {
                        cutPercentIndex=5
                        cutPercentValue=6
                    }
                    6 -> {
                        cutPercentIndex=6
                        cutPercentValue=7
                    }
                    7 -> {
                        cutPercentIndex=7
                        cutPercentValue=8
                    }
                    8 -> {
                        cutPercentIndex=8
                        cutPercentValue=9
                    }
                    9 -> {
                        cutPercentIndex=9
                        cutPercentValue=10
                    }
                    10 -> {
                        cutPercentIndex=10
                        cutPercentValue=11
                    }
                    11 -> {
                        cutPercentIndex=11
                        cutPercentValue=12
                    }
                    12 -> {
                        cutPercentIndex=12
                        cutPercentValue=13
                    }
                    13 -> {
                        cutPercentIndex=13
                        cutPercentValue=14
                    }
                    14 -> {
                        cutPercentIndex=14
                        cutPercentValue=15
                    }
                    15 -> {
                        cutPercentIndex=15
                        cutPercentValue=16
                    }
                    16 -> {
                        cutPercentIndex=16
                        cutPercentValue=17
                    }
                    17 -> {
                        cutPercentIndex=17
                        cutPercentValue=18
                    }
                    18 -> {
                        cutPercentIndex=18
                        cutPercentValue=19
                    }
                    19 -> {
                        cutPercentIndex=19
                        cutPercentValue=20
                    }
                    else -> {
                        cutPercentIndex=14
                        cutPercentValue=15
                    }


                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                cutPercentIndex=14
                cutPercentValue=15
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
        val totalSellingPrice  = ((totalPrice*((100-cutPercentValue)))/100).roundToInt()
        Log.d("tag","price ${totalPrice} :: quantity -> $totalVori")

        val tPriceInBangla = Utils.getBanglaDigitFromEnglishDigit(totalSellingPrice.toString())

        binding.tvFinalGoldPrice.text = "প্রতি ভরি স্বর্ণের দাম $pricePerVori টাকা দরে $vori ভরি,$ana আনা,$roti রতি,$point পয়েন্ট স্বর্ণের বিক্রয় মূল্য \n${Utils.putCommaInNumber(tPriceInBangla)} টাকা।\n\nConsidering $pricePerVori per Bhori price, $vori Bhori, $ana Ana, $roti Roti and $point Point gold selling price is $totalSellingPrice BDT"
    }

    private fun calculatePriceGram(gram:Double, pricePerGram : Double) {
        val totalGram : kotlin.Double  = vori.toDouble()+(ana/16.0)+(roti/96.0)+(point/960)
        val totalPrice  = gram*pricePerGram
        val totalSellingPrice  = ((totalPrice*(100-cutPercentValue))/100)
        Log.d("tag","price $totalPrice :: quantity -> $totalGram Gram")

        val tPriceInBangla = Utils.getBanglaDigitFromEnglishDigit(totalSellingPrice.roundToInt().toString())
        //val tPriceInBanglaWithTax = Utils.getBanglaDigitFromEnglishDigit(totalSellingPrice.roundToInt().toString())

        binding.tvFinalGoldPrice.text = "প্রতি গ্রাম স্বর্ণের দাম $pricePerGram টাকা দরে $gram গ্রাম স্বর্ণের বিক্রয় মূল্য\n${Utils.putCommaInNumber(tPriceInBangla)} টাকা।\n\nConsidering $pricePerGram per Gram price, $gram gram gold Selling price is ${totalPrice.roundToInt()}"
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
        startActivity(Intent(this@GoldSellPrice, MainActivity::class.java))
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            startActivity(Intent(this@GoldSellPrice, MainActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initAdMob() {
        val  prefManager = PrefManager(this)
        val lastAdShowDate = prefManager.lastBannerAdShownGSP
        if (AdMobUtil.canBannerAdShow(this, lastAdShowDate,Constants.AD_TYPE_BANNER)) {
            Log.d("tag","Banner Ad Home will load")

            binding.adView.visibility = View.VISIBLE
            MobileAds.initialize(this) {
                val bannerAdHelper = BannerAddHelper(this)
                //binding.adView.adUnitId=prefManager.adUnitIdBanner
                bannerAdHelper.loadBannerAd(binding.adView) {
                    if (it) {
                        prefManager.lastBannerAdShownGSP = System.currentTimeMillis()
                    }
                }
            }
        } else {
            binding.adView.visibility = View.GONE

            if(System.currentTimeMillis()-lastAdShowDate<0){
                prefManager.lastBannerAdShownGSP=System.currentTimeMillis()
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