package com.lincoln4791.goldcalculatorbd.activities
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.lincoln4791.dailyexpensemanager.admobAdsUpdated.InterstistialAdHelper
import com.lincoln4791.goldcalculatorbd.*
import com.lincoln4791.goldcalculatorbd.activities.GoldBuyPrice.Companion.PRICE_UNIT_GRAM_INDEX
import com.lincoln4791.goldcalculatorbd.activities.GoldBuyPrice.Companion.PRICE_UNIT_GRAM_TEXT
import com.lincoln4791.goldcalculatorbd.activities.GoldBuyPrice.Companion.PRICE_UNIT_VORI_INDEX
import com.lincoln4791.goldcalculatorbd.activities.GoldBuyPrice.Companion.PRICE_UNIT_VORI_TEXT
import com.lincoln4791.goldcalculatorbd.activities.GoldBuyPrice.Companion.WEIGHT_CHOOSER_UNIT_GRAM_INDEX
import com.lincoln4791.goldcalculatorbd.activities.GoldBuyPrice.Companion.WEIGHT_CHOOSER_UNIT_GRAM_TEXT
import com.lincoln4791.goldcalculatorbd.activities.GoldBuyPrice.Companion.WEIGHT_CHOOSER_UNIT_SEPARATELY_INDEX
import com.lincoln4791.goldcalculatorbd.activities.GoldBuyPrice.Companion.WEIGHT_CHOOSER_UNIT_SEPARATELY_TEXT
import com.lincoln4791.goldcalculatorbd.activities.GoldBuyPrice.Companion.WEIGHT_CHOOSER_UNIT_TOGETHER_INDEX
import com.lincoln4791.goldcalculatorbd.activities.GoldBuyPrice.Companion.WEIGHT_CHOOSER_UNIT_TOGETHER_TEXT
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.AdMobUtil
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.BannerAddHelper
import com.lincoln4791.goldcalculatorbd.common.PriceUnitEnum
import com.lincoln4791.goldcalculatorbd.common.WeightUnitEnum
import com.lincoln4791.goldcalculatorbd.databinding.ActivityGoldSellPriceBinding
import kotlin.math.roundToInt

class GoldSellPrice : AppCompatActivity() {
    private lateinit var prefManager : PrefManager
    private lateinit var interAd: InterstistialAdHelper
    private var mInterstitialAd: InterstitialAd? = null
    private var isAdLoaded = false
    private var priceUnit = PRICE_UNIT_GRAM_TEXT
    private var priceUnitIndex = PRICE_UNIT_GRAM_INDEX
    private var cutPercentIndex = 14
    private var cutPercentValue = 15
    private lateinit var binding : ActivityGoldSellPriceBinding
    private var gram = 0.0
    private var vori = 0
    private var ana = 0
    private var roti = 0
    private var point = 0
    private var separateCalcQty = 0.0
    private var separateCalcWeightUnit = WeightUnitEnum.GRAM.name

    private var weightChooserUnitText = WEIGHT_CHOOSER_UNIT_GRAM_TEXT
    private var weightChooserUnitIndex = WEIGHT_CHOOSER_UNIT_GRAM_INDEX

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefManager = PrefManager(this)
        binding = ActivityGoldSellPriceBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setContentView(binding.root)
        initInterstitialAd()
        Utils.changeNavBarColor(this,this)
        enableBackButton()
        initAdMob()
        initPriceChooserSpinner()
        initUnitChooserSpinner()
        initcutPercentSpinner()

        binding.cvGoldWeight.setOnClickListener {
            binding.tvGoldWeight.error=null

            if (weightChooserUnitIndex == WEIGHT_CHOOSER_UNIT_GRAM_INDEX) {
                showGramWeightDialog()
            } else if (weightChooserUnitIndex == WEIGHT_CHOOSER_UNIT_TOGETHER_INDEX) {
                showVoriWeightDialog()
            } else {
                Utils.showVoriWeightDialogForIndividualCalc(this@GoldSellPrice) { amount, unit ->
                    separateCalcQty=amount
                    separateCalcWeightUnit=unit

                    binding.tvGoldWeight.text = "$amount ${WeightUnitEnum.getValueBFromName(unit)}"
                }
            }

        }

        binding.cvCalculate.setOnClickListener {
            if(binding.etUnitPrice.text.toString().isEmpty()){
                binding.etUnitPrice.error="স্বর্ণের দাম(Price of Gold)"
                return@setOnClickListener
            }
            else if (binding.tvGoldWeight.text.toString().isEmpty()){
                binding.tvGoldWeight.error="স্বর্ণের ওজন(Weight of Gold)"
                return@setOnClickListener
            }
            else{
                showInterAd()
            }

        }
    }

    private fun initPriceChooserSpinner() {
        val spinnerArray = arrayListOf(PRICE_UNIT_GRAM_TEXT, PRICE_UNIT_VORI_TEXT)
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this@GoldSellPrice,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerArray
        )
        val spinnerIndex: Int
        when (priceUnit) {
            PRICE_UNIT_VORI_TEXT -> {
                spinnerIndex = PRICE_UNIT_VORI_INDEX
            }

            PRICE_UNIT_GRAM_TEXT -> {
                spinnerIndex = PRICE_UNIT_GRAM_INDEX
            }

            else -> {
                spinnerIndex = PRICE_UNIT_VORI_INDEX
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
                        priceUnit = PRICE_UNIT_GRAM_TEXT
                        priceUnitIndex = PRICE_UNIT_GRAM_INDEX
                        refreshAllValues()
                    }

                    1 -> {
                        priceUnit = PRICE_UNIT_VORI_TEXT
                        priceUnitIndex = PRICE_UNIT_VORI_INDEX
                        refreshAllValues()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                priceUnit = PRICE_UNIT_GRAM_TEXT
                priceUnitIndex = PRICE_UNIT_GRAM_INDEX
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
        when (priceUnit) {
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

    private fun initUnitChooserSpinner() {
        val spinnerArray = arrayListOf(
            WEIGHT_CHOOSER_UNIT_GRAM_TEXT,
            WEIGHT_CHOOSER_UNIT_TOGETHER_TEXT,
            WEIGHT_CHOOSER_UNIT_SEPARATELY_TEXT
        )
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this@GoldSellPrice,
            R.layout.custom_spinner_item,
            spinnerArray
        )
        val spinnerIndex: Int
        when (weightChooserUnitText) {
            WEIGHT_CHOOSER_UNIT_GRAM_TEXT -> {
                spinnerIndex = WEIGHT_CHOOSER_UNIT_GRAM_INDEX
            }

            WEIGHT_CHOOSER_UNIT_TOGETHER_TEXT -> {
                spinnerIndex = WEIGHT_CHOOSER_UNIT_TOGETHER_INDEX
            }

            WEIGHT_CHOOSER_UNIT_SEPARATELY_TEXT -> {
                spinnerIndex = WEIGHT_CHOOSER_UNIT_SEPARATELY_INDEX
            }

            else -> {
                spinnerIndex = WEIGHT_CHOOSER_UNIT_GRAM_INDEX
            }
        }
        binding.spinnerWeightUnitChooser.adapter = spinnerAdapter
        binding.spinnerWeightUnitChooser.setSelection(spinnerIndex)
        binding.spinnerWeightUnitChooser.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long,
                ) {
                    when (position) {
                        0 -> {
                            weightChooserUnitText = WEIGHT_CHOOSER_UNIT_GRAM_TEXT
                            weightChooserUnitIndex = WEIGHT_CHOOSER_UNIT_GRAM_INDEX
                            refreshAllValues()
                        }

                        1 -> {
                            weightChooserUnitText = WEIGHT_CHOOSER_UNIT_TOGETHER_TEXT
                            weightChooserUnitIndex = WEIGHT_CHOOSER_UNIT_TOGETHER_INDEX
                            refreshAllValues()
                        }

                        2 -> {
                            weightChooserUnitText = WEIGHT_CHOOSER_UNIT_SEPARATELY_TEXT
                            weightChooserUnitIndex = WEIGHT_CHOOSER_UNIT_SEPARATELY_INDEX
                            refreshAllValues()
                        }

                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    weightChooserUnitText = WEIGHT_CHOOSER_UNIT_GRAM_TEXT
                    weightChooserUnitIndex = WEIGHT_CHOOSER_UNIT_GRAM_INDEX
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

        val tPriceInBangla = Utils.getBanglaDigitFromEnglishDigit(totalSellingPrice)

        binding.tvFinalGoldPrice.text = "প্রতি ভরি স্বর্ণের দাম $pricePerVori টাকা দরে $vori ভরি,$ana আনা,$roti রতি,$point পয়েন্ট স্বর্ণের বিক্রয় মূল্য \n${Utils.putCommaInNumber(tPriceInBangla.toInt())} টাকা।\n\nConsidering $pricePerVori per Bhori price, $vori Bhori, $ana Ana, $roti Roti and $point Point gold selling price is $totalSellingPrice BDT"
    }

    private fun calculatePriceGram(gram:Double, pricePerGram : Double) {
        val totalGram : kotlin.Double  = vori.toDouble()+(ana/16.0)+(roti/96.0)+(point/960)
        val totalPrice  = gram*pricePerGram
        val totalSellingPrice  = (totalPrice*(100-cutPercentValue))/100
        Log.d("tag","price $totalPrice :: quantity -> $totalGram Gram")

        val tPriceInBangla = Utils.getBanglaDigitFromEnglishDigit(totalSellingPrice.roundToInt())
        //val tPriceInBanglaWithTax = Utils.getBanglaDigitFromEnglishDigit(totalSellingPrice.roundToInt().toString())

        binding.tvFinalGoldPrice.text = "প্রতি গ্রাম স্বর্ণের দাম $pricePerGram টাকা দরে $gram গ্রাম স্বর্ণের বিক্রয় মূল্য\n${Utils.putCommaInNumber(tPriceInBangla.toInt())} টাকা।\n\nConsidering $pricePerGram per Gram price, $gram gram gold Selling price is ${totalPrice.roundToInt()}"
    }

    private fun refreshAllValues(){
        gram = 0.0
        vori = 0
        ana = 0
        roti = 0
        point = 0
        separateCalcQty = 0.0
        //binding.etUnitPrice.setText("")
        binding.tvGoldWeight.text = ""
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
                val bannerAdHelper = BannerAddHelper()
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
                    Handler(Looper.getMainLooper()).postDelayed({
                        initInterstitialAd()
                    },prefManager.interAdInterval+1000)
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
       if (priceUnitIndex == PRICE_UNIT_GRAM_INDEX) {
           if (weightChooserUnitIndex == WEIGHT_CHOOSER_UNIT_GRAM_INDEX) {
               calculatePriceWeightGram(
                   gram, binding.etUnitPrice.text.toString().toDouble(),
                   PriceUnitEnum.GRAM.name
               )
           } else if (weightChooserUnitIndex == WEIGHT_CHOOSER_UNIT_TOGETHER_INDEX) {
               calculatePriceVoriTogether(
                   vori,
                   ana,
                   roti,
                   point,
                   binding.etUnitPrice.text.toString().toDouble(), PriceUnitEnum.GRAM.name
               )
           } else if (weightChooserUnitIndex == WEIGHT_CHOOSER_UNIT_SEPARATELY_INDEX) {
               calculatePriceSeparately(separateCalcQty,separateCalcWeightUnit,binding.etUnitPrice.text.toString().toDouble(),PriceUnitEnum.GRAM.name)
           }
       } else if (priceUnitIndex == PRICE_UNIT_VORI_INDEX) {
           if (weightChooserUnitIndex == WEIGHT_CHOOSER_UNIT_GRAM_INDEX) {
               calculatePriceWeightGram(
                   gram, binding.etUnitPrice.text.toString().toDouble(),
                   PriceUnitEnum.VORI.name
               )
           }
           else if (weightChooserUnitIndex == WEIGHT_CHOOSER_UNIT_TOGETHER_INDEX) {
               calculatePriceVoriTogether(
                   vori,
                   ana,
                   roti,
                   point,
                   binding.etUnitPrice.text.toString().toDouble(), PriceUnitEnum.VORI.name
               )
           } else if (weightChooserUnitIndex == WEIGHT_CHOOSER_UNIT_SEPARATELY_INDEX) {
               calculatePriceSeparately(separateCalcQty,separateCalcWeightUnit,binding.etUnitPrice.text.toString().toDouble(),PriceUnitEnum.VORI.name)
           }
       }

    }

    private fun calculatePriceVoriTogether(
        vori: Int,
        ana: Int,
        roti: Int,
        point: Int,
        price: Double,
        priceUnit: String
    ) {

        var calculatablePrice = price
        var priceTitleE = "Vori"
        var priceTitleB = "ভরি"
        if (priceUnit == PriceUnitEnum.GRAM.name) {
            calculatablePrice = Utils.getPriceInVoriFromPriceInGram(price.toDouble())
            priceTitleE = "Gram"
            priceTitleB = "গ্রাম"
        }

        val totalVori: kotlin.Double =
            vori.toDouble() + (ana / 16.0) + (roti / 96.0) + (point / 960)
        val totalPrice = (totalVori * calculatablePrice)*((100-cutPercentValue)/100.0)
        val tPriceInBangla = Utils.getBanglaDigitFromEnglishDigit(totalPrice.roundToInt())

        binding.tvFinalGoldPrice.text =
            "প্রতি ${priceTitleB} স্বর্ণের দাম $price টাকা দরে $vori ভরি,$ana আনা,$roti রতি,$point পয়েন্ট স্বর্ণের " +
                    "বিক্রয় মূল্য (${cutPercentValue}% টাকা কাটার পর): ${Utils.putCommaInNumber(tPriceInBangla.toInt())} টাকা।\n" +
                    "Considering $price per ${priceTitleE} price, $vori Bhori, $ana Ana, $roti Roti and $point Point gold selling price(after ${cutPercentValue}% reduction): ${Utils.putCommaInNumber(totalPrice.roundToInt())} BDT\n"
    }

    private fun calculatePriceWeightGram(gram: Double, price: Double, priceUnit: String) {
        var calculatedPrice = price

        if (priceUnit == PriceUnitEnum.VORI.name) {
            calculatedPrice = Utils.getPriceInGramFromPriceInVori(price)
        }

        //val totalGram: kotlin.Double = vori.toDouble() + (ana / 16.0) + (roti / 96.0) + (point / 960)
        val totalPrice = (gram * calculatedPrice)*((100-cutPercentValue)/100.0)
        val tPriceInBangla = Utils.getBanglaDigitFromEnglishDigit(totalPrice.roundToInt())

        binding.tvFinalGoldPrice.text =
            "প্রতি ${PriceUnitEnum.getValueBFromName(priceUnit)} স্বর্ণের দাম $price টাকা দরে $gram গ্রাম স্বর্ণের বিক্রয় মূল্য(${cutPercentValue}% টাকা কাটার পর): ${Utils.putCommaInNumber(tPriceInBangla.toInt())} টাকা।\n\n" +
                    "Considering $price per ${PriceUnitEnum.getValueEFromName(priceUnit)} price, $gram gram gold selling price(after ${cutPercentValue}% reduction): ${Utils.putCommaInNumber(totalPrice.roundToInt())} BDT"
    }

    private fun calculatePriceSeparately(
        weightQty: Double,
        weightUnit: String,
        price: Double,
        priceUnit: String,
    ) {
        var calculatedPrice = price
        var tPrice = 0.0
        if (priceUnit == PriceUnitEnum.GRAM.name) {
            calculatedPrice = Utils.getPriceInVoriFromPriceInGram(price)
        }

        if (weightUnit == WeightUnitEnum.VORI.name) {
            tPrice = Utils.getRoundedDigit(calculatedPrice * weightQty,3)
        }
        else if (weightUnit == WeightUnitEnum.ANA.name) {
            tPrice = Utils.getRoundedDigit(calculatedPrice * (Utils.getVoriFromAna(weightQty)),3)
        }
        else if (weightUnit == WeightUnitEnum.ROTI.name) {
            tPrice = Utils.getRoundedDigit(calculatedPrice * (Utils.getVoriFromRoti(weightQty)),3)
        }
        else if (weightUnit == WeightUnitEnum.POINT.name) {
            tPrice = Utils.getRoundedDigit(calculatedPrice * (Utils.getVoriFromPoint(weightQty)),3)
        }

        val tPriceAfterReduction = tPrice*((100-cutPercentValue)/100.0)
        val tPriceAfterReductionBangla = Utils.getBanglaDigitFromEnglishDigit(tPriceAfterReduction.roundToInt())

        Log.d("tag", "price $tPrice :: quantity -> $weightQty")
        binding.tvFinalGoldPrice.text =
            "প্রতি ${PriceUnitEnum.getValueBFromName(priceUnit)} স্বর্ণের দাম $price টাকা দরে $weightQty ${WeightUnitEnum.getValueBFromName(weightUnit)} স্বর্ণের বিক্রয় মূল্য(${cutPercentValue}% টাকা কাটার পর): ${Utils.putCommaInNumber(tPriceAfterReductionBangla.toInt())} টাকা।\n\n" +
                    "Considering $price per ${PriceUnitEnum.getValueEFromName(priceUnit)} price, $weightQty ${WeightUnitEnum.getValueEFromName(priceUnit)} gold selling price(after ${cutPercentValue}% reduction): ${Utils.putCommaInNumber(tPriceAfterReduction.roundToInt())} BDT"


    }

}