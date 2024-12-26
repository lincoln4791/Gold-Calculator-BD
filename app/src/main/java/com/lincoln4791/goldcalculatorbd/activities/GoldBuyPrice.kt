package com.lincoln4791.goldcalculatorbd.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.lincoln4791.dailyexpensemanager.admobAdsUpdated.InterstistialAdHelper
import com.lincoln4791.goldcalculatorbd.*
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.AdMobUtil
import com.lincoln4791.goldcalculatorbd.admobAdsUpdated.BannerAddHelper
import com.lincoln4791.goldcalculatorbd.common.PriceUnitEnum
import com.lincoln4791.goldcalculatorbd.common.WeightUnitEnum
import com.lincoln4791.goldcalculatorbd.databinding.ActivityGoldBuyPriceBinding
import kotlin.math.roundToInt

class GoldBuyPrice : AppCompatActivity() {
    private lateinit var prefManager: PrefManager
    private lateinit var interAd: InterstistialAdHelper
    private var mInterstitialAd: InterstitialAd? = null
    private var isAdLoaded = false
    private var priceUnit = PRICE_UNIT_GRAM_TEXT
    private var priceUnitIndex = PRICE_UNIT_GRAM_INDEX
    private lateinit var binding: ActivityGoldBuyPriceBinding
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
        prefManager = PrefManager(this)
        super.onCreate(savedInstanceState)
        binding = ActivityGoldBuyPriceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initInterstitialAd()
        Utils.changeNavBarColor(this, this)
        enableBackButton()
        initPriceChooserSpinner()
        initUnitChooserSpinner()
        initAdMob()

        binding.cvGoldWeight.setOnClickListener {

            if (weightChooserUnitIndex == WEIGHT_CHOOSER_UNIT_GRAM_INDEX) {
                showGramWeightDialog()
            } else if (weightChooserUnitIndex == WEIGHT_CHOOSER_UNIT_TOGETHER_INDEX) {
                showVoriWeightDialog()
            } else {
                Utils.showVoriWeightDialogForIndividualCalc(this@GoldBuyPrice) { amount, unit ->
                    separateCalcQty=amount
                    separateCalcWeightUnit=unit
                    var amountUnitE = ""
                    var amountUnitB = ""
                    if(unit==WeightUnitEnum.GRAM.name){
                        amountUnitE= "Gram"
                        amountUnitE= "গ্রাম"
                    }
                    else if(unit==WeightUnitEnum.VORI.name){
                        amountUnitE= "Vori"
                        amountUnitE= "ভরি"
                    }

                    if(unit==WeightUnitEnum.ANA.name){
                        amountUnitE= "Ana"
                        amountUnitE= "আনা"
                    }

                    if(unit==WeightUnitEnum.ROTI.name){
                        amountUnitE= "Roti"
                        amountUnitE= "রটি"
                    }
                    if(unit==WeightUnitEnum.POINT.name){
                        amountUnitE= "Point"
                        amountUnitE= "পয়েন্ট"
                    }
                    binding.tvGoldWeight.text = "$amount $amountUnitB"
                }
            }

        }

        binding.cvCalculate.setOnClickListener {
            if (binding.etUnitPrice.text.isEmpty()) {
                binding.etUnitPrice.error = "স্বর্ণের দাম(Price of Gold)"
            } else if (binding.tvGoldWeight.text.isEmpty()) {
                binding.tvGoldWeight.error = "স্বর্ণের ওজন(Weight of Gold)"
            } else {
                showInterAd()
            }
        }
    }

    private fun initPriceChooserSpinner() {
        val spinnerArray = arrayListOf(PRICE_UNIT_GRAM_TEXT, PRICE_UNIT_VORI_TEXT)
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this@GoldBuyPrice,
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

    private fun initUnitChooserSpinner() {
        val spinnerArray = arrayListOf(
            WEIGHT_CHOOSER_UNIT_GRAM_TEXT,
            WEIGHT_CHOOSER_UNIT_TOGETHER_TEXT,
            WEIGHT_CHOOSER_UNIT_SEPARATELY_TEXT
        )
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this@GoldBuyPrice,
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
                            //refreshAllValues()
                        }

                        1 -> {
                            weightChooserUnitText = WEIGHT_CHOOSER_UNIT_TOGETHER_TEXT
                            weightChooserUnitIndex = WEIGHT_CHOOSER_UNIT_TOGETHER_INDEX
                            //refreshAllValues()
                        }

                        2 -> {
                            weightChooserUnitText = WEIGHT_CHOOSER_UNIT_SEPARATELY_TEXT
                            weightChooserUnitIndex = WEIGHT_CHOOSER_UNIT_SEPARATELY_INDEX
                            //refreshAllValues()
                        }

                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    weightChooserUnitText = WEIGHT_CHOOSER_UNIT_GRAM_TEXT
                    weightChooserUnitIndex = WEIGHT_CHOOSER_UNIT_GRAM_INDEX
                }
            }
    }

    private fun showVoriWeightDialog() {
        val dialog = Dialog(this)
        val dView = layoutInflater.inflate(R.layout.layout_gold_weight_vori, null, false)
        dialog.setContentView(dView)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val etVori = dView.findViewById<EditText>(R.id.etVori)
        val etAna = dView.findViewById<EditText>(R.id.etAna)
        val etRoti = dView.findViewById<EditText>(R.id.etRoti)
        val etPoint = dView.findViewById<EditText>(R.id.etPoint)
        val btnCalculate = dView.findViewById<CardView>(R.id.cv_calculate)


        btnCalculate.setOnClickListener {
            vori = if (etVori.text.toString().isEmpty()) 0 else etVori.text.toString().toInt()
            ana = if (etAna.text.toString().isEmpty()) 0 else etAna.text.toString().toInt()
            roti = if (etRoti.text.toString().isEmpty()) 0 else etRoti.text.toString().toInt()
            point = if (etPoint.text.toString().isEmpty()) 0 else etPoint.text.toString().toInt()
            if (ana > 15) {
                etAna.error = "আনা সর্বোচ্চ ১৫ হতে পারবে"
            } else if (roti > 5) {
                etRoti.error = "রতি সর্বোচ্চ ৫ হতে পারবে"
            } else if (point > 9) {
                etPoint.error = "পয়েন্ট সর্বোচ্চ ৯ হতে পারবে"
            } else {
                dialog.dismiss()
                Log.d("tag", "$vori , $ana, $roti, $point")
                binding.tvGoldWeight.text = "$vori ভরি,$ana আনা,$roti রতি,$point পয়েন্ট"
            }
        }


    }

    private fun showGramWeightDialog() {
        val dialog = Dialog(this)
        val dView = layoutInflater.inflate(R.layout.layout_gold_weight_gram, null, false)
        dialog.setContentView(dView)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val etVori = dView.findViewById<EditText>(R.id.etGram)
        val btnCalculate = dView.findViewById<CardView>(R.id.cv_calculate)


        btnCalculate.setOnClickListener {
            gram = if (etVori.text.toString().isEmpty()) 0.0 else etVori.text.toString().toDouble()
            dialog.dismiss()
            Log.d("tag", "$gram Gram")
            binding.tvGoldWeight.text = "$gram গ্রাম(Gram)"

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
        val totalPrice = totalVori * calculatablePrice
        val totalPriceWithTax = Utils.getPriceWithTax(totalVori * calculatablePrice)
        val tPriceInBangla = Utils.getBanglaDigitFromEnglishDigit(totalPrice.roundToInt().toString())
        val tPriceWithTaxInBangla = Utils.getBanglaDigitFromEnglishDigit(totalPriceWithTax.roundToInt().toString())
        val tPriceWithMakingCharge = Utils.getPriceWithMakingCharge(totalPriceWithTax)
        val tPWithMakingChargeBangla = Utils.getBanglaDigitFromEnglishDigit(tPriceWithMakingCharge.toString())

        binding.tvFinalGoldPrice.text =
            "প্রতি ${priceTitleB} স্বর্ণের দাম $price টাকা দরে $vori ভরি,$ana আনা,$roti রতি,$point পয়েন্ট স্বর্ণের " +
                    "দাম: ${Utils.putCommaInNumber(tPriceInBangla)} টাকা।\n" +
                    "${(Utils.TAX_RATE*100)}% ট্যাক্স সহ: ${Utils.putCommaInNumber(tPriceWithTaxInBangla)} টাকা\n" +
                    "${(Utils.MAKING_CHARGE_RATE*100)}% মেকং চার্জ সহ: ${Utils.putCommaInNumber(tPWithMakingChargeBangla)} টাকা\n\n"+
                    "Considering $price per ${priceTitleE} price, $vori Bhori, $ana Ana, $roti Roti and $point Point gold price: ${totalPrice.roundToInt()} BDT\n" +
                    "Including ${(Utils.TAX_RATE*100)}% tax: ${totalPriceWithTax.roundToInt()} BDT"+
                    "Including ${Utils.MAKING_CHARGE_RATE*100}% making charge: ${tPriceWithMakingCharge.roundToInt()} BDT"
    }

    private fun calculatePriceWeightGram(gram: Double, price: Double, priceUnit: String) {
        var calculatedPrice = price

        if (priceUnit == PriceUnitEnum.VORI.name) {
            calculatedPrice = Utils.getPriceInGramFromPriceInVori(price)
        }

        //val totalGram: kotlin.Double = vori.toDouble() + (ana / 16.0) + (roti / 96.0) + (point / 960)
        val totalPrice = gram * calculatedPrice
        val totalPriceWithTax = Utils.getPriceWithTax(totalPrice)
        val totalPriceWithMakingCharge = Utils.getPriceWithMakingCharge(totalPriceWithTax)
        val tPriceInBangla = Utils.getBanglaDigitFromEnglishDigit(totalPrice.roundToInt().toString())
        val tPriceInBanglaWithTax = Utils.getBanglaDigitFromEnglishDigit(totalPriceWithTax.roundToInt().toString())
        val tPriceInBanglaWithMakingCharge = Utils.getBanglaDigitFromEnglishDigit(totalPriceWithMakingCharge.roundToInt().toString())

        binding.tvFinalGoldPrice.text =
            "প্রতি ${PriceUnitEnum.getValueBFromName(priceUnit)} স্বর্ণের দাম $price টাকা দরে $gram গ্রাম স্বর্ণের দাম: ${Utils.putCommaInNumber(tPriceInBangla)} টাকা।\n" +
                    "${Utils.TAX_RATE*100}% ট্যাক্স সহ : ${Utils.putCommaInNumber(tPriceInBanglaWithTax)} টাকা\n" +
                    "${Utils.MAKING_CHARGE_RATE*100}% মেকিং চার্জ সহ : ${Utils.putCommaInNumber(tPriceInBanglaWithMakingCharge)} টাকা\n\n" +
                    "Considering $price per ${PriceUnitEnum.getValueEFromName(priceUnit)} price, $gram gram gold price: ${totalPrice.roundToInt()} BDT" +
                    "\nIncluding ${Utils.TAX_RATE*100}% tax: ${totalPriceWithTax.roundToInt()} BDT"+
                    "\nIncluding ${Utils.MAKING_CHARGE_RATE*100}% making charge: ${totalPriceWithMakingCharge.roundToInt()} BDT"
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

        val tPriceWithTax = Utils.getPriceWithTax(tPrice)
        val tPriceWithMakingCharge = Utils.getPriceWithMakingCharge(tPriceWithTax)
        var tPriceBangla = Utils.getBanglaDigitFromEnglishDigit(tPrice.roundToInt().toString())
        var tPriceBanglaWithTax = Utils.getBanglaDigitFromEnglishDigit(tPriceWithTax.roundToInt().toString())
        var tPriceBanglaWithMakingCharge = Utils.getBanglaDigitFromEnglishDigit(tPriceWithMakingCharge.roundToInt().toString())

        Log.d("tag", "price $tPrice :: quantity -> $weightQty")
        binding.tvFinalGoldPrice.text =
            "প্রতি ${PriceUnitEnum.getValueBFromName(priceUnit)} স্বর্ণের দাম $price টাকা দরে ${WeightUnitEnum.getValueBFromName(weightUnit)} স্বর্ণের দাম: ${Utils.putCommaInNumber(tPriceBangla)} টাকা।\n" +
                    "${Utils.TAX_RATE*100}% ট্যাক্স সহ: ${Utils.putCommaInNumber(tPriceBanglaWithTax)} টাকা\n"+
                    "${Utils.MAKING_CHARGE_RATE*100}% মেকিং চার্জ সহ: ${Utils.putCommaInNumber(tPriceBanglaWithMakingCharge)} টাকা\n\n"+
                    "Considering $price per ${PriceUnitEnum.getValueEFromName(priceUnit)} price, $weightQty ${WeightUnitEnum.getValueEFromName(priceUnit)} gold price: ${tPrice.roundToInt()} BDT\n" +
                    "Including ${Utils.TAX_RATE*100}% tax : ${tPriceWithTax.roundToInt()} BDT\n"+
                    "Including ${Utils.MAKING_CHARGE_RATE*100}% making charge : ${tPriceWithMakingCharge.roundToInt()} BDT"

    }

    private fun refreshAllValues() {
        gram = 0.0
        vori = 0
        ana = 0
        roti = 0
        point = 0
        binding.etUnitPrice.setText("")
        binding.tvGoldWeight.text = "স্বর্ণের ওজন(Gold Weight)"
        binding.tvFinalGoldPrice.text = "ফলাফল(Result)"
    }

    private fun enableBackButton() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@GoldBuyPrice, MainActivity::class.java))
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            startActivity(Intent(this@GoldBuyPrice, MainActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initAdMob() {
        val prefManager = PrefManager(this)
        val lastAdShowDate = prefManager.lastBannerAdShownGBP
        if (AdMobUtil.canBannerAdShow(this, lastAdShowDate, Constants.AD_TYPE_BANNER)) {
            Log.d("tag", "Banner Ad Home will load")

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

            if (System.currentTimeMillis() - lastAdShowDate < 0) {
                prefManager.lastBannerAdShownGBP = System.currentTimeMillis()
            }

            Log.d("tag", "Banner Ad Home Not Shown")
        }
    }

    private fun initInterstitialAd() {
        interAd = InterstistialAdHelper(this, this, mInterstitialAd)
        val lastAdShown = prefManager.lastInterstitialAdShown
        if (AdMobUtil.canBannerAdShow(this, lastAdShown, Constants.AD_TYPE_INTER)) {
            interAd.loadinterAd(prefManager.adUnitIdInterstitial) {
                Log.d("InterAd", "Inter ad loaded -> $it")
                isAdLoaded = it
            }
        } else {
            if (System.currentTimeMillis() - lastAdShown < 0) {
                prefManager.lastInterstitialAdShown = System.currentTimeMillis()
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

    private fun initCalculation() {
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

    companion object {
        const val WEIGHT_CHOOSER_UNIT_GRAM_INDEX = 0
        const val WEIGHT_CHOOSER_UNIT_TOGETHER_INDEX = 1
        const val WEIGHT_CHOOSER_UNIT_SEPARATELY_INDEX = 2

        const val WEIGHT_CHOOSER_UNIT_GRAM_TEXT = "গ্রামে মাপুন-Measure In Gram"
        const val WEIGHT_CHOOSER_UNIT_TOGETHER_TEXT =
            "একসাথে ভরি,আনা,রতি,পয়েন্ট মিলিয়ে-Vori,Ana,Roti,Point Together"
        const val WEIGHT_CHOOSER_UNIT_SEPARATELY_TEXT =
            "আলাদা ভাবে ভরি/আনা/রতি/পয়েন্ট-Separately Vori,Ana,Roti,Point"

        const val PRICE_UNIT_GRAM_INDEX = 0
        const val PRICE_UNIT_VORI_INDEX = 1
        const val PRICE_UNIT_GRAM_TEXT = "প্রতি গ্রাম(Per Gram)"
        const val PRICE_UNIT_VORI_TEXT = "প্রতি ভরি(Per Bhori)"
    }

}