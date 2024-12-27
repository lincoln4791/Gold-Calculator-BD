package com.lincoln4791.goldcalculatorbd

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.lincoln4791.goldcalculatorbd.common.WeightUnitEnum

object Utils {

    const val TAX_RATE = .05;
    const val MAKING_CHARGE_RATE = .06;

    fun getBanglaDigitFromEnglishDigit(value: Int): String {
        //val valu = "12345"
        var valu2 = ""

        for (element in value.toString()) {
            if (element == '0') {
                valu2 = valu2 + '০'
            } else if (element == '1') {
                valu2 = valu2 + '১'
            } else if (element == '2') {
                valu2 = valu2 + '২'
            } else if (element == '3') {
                valu2 = valu2 + '৩'
            } else if (element == '4') {
                valu2 = valu2 + '৪'
            } else if (element == '5') {
                valu2 = valu2 + '৫'
            } else if (element == '6') {
                valu2 = valu2 + '৬'
            } else if (element == '7') {
                valu2 = valu2 + '৭'
            } else if (element == '8') {
                valu2 = valu2 + '৮'
            } else if (element == '9') {
                valu2 = valu2 + '৯'
            }
        }

        Log.d("tag", "value is -> ${valu2}")
        return valu2

    }

    fun putCommaInNumber(value: Int): String {
        var flag = false
        var count = 0
        var count2 = 0
        val num = value.toString().reversed()
        var num2 = ""

        for (element in num) {
            num2 += element
            if (count == 2 && !flag) {
                num2 = num2.plus(",")
                flag = true
                count = 0
            }
            if (count == 2 && flag) {
                num2 = num2.plus(",")
                count = 0
            }
            count++
            count2++
        }

        val finalValue = if (num2[num2.length - 1] == ',') {
            Log.d("tag", "Dropping last")
            num2.dropLast(1)
        } else {
            Log.d("tag", "Not Dropping last")
            num2
        }

        Log.d("tag", "number with comma is -> ${finalValue.reversed()}")
        return finalValue.reversed()
    }

    fun changeNavBarColor(context: Context, activity: Activity) {
        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = context.resources.getColor(R.color.primary)
    }

    fun goToPlayStore(context: Context) {
        val goToPlayStoreAppLnk = Intent(Intent.ACTION_VIEW)
        val appLink: Uri = Uri.parse(Constants.PLAY_STORE_APP_LINK)
        goToPlayStoreAppLnk.data = appLink
        context.startActivity(goToPlayStoreAppLnk)
    }


    fun showVoriWeightDialogForIndividualCalc(
        context: Context,
        cb: (amount: Double, unit: String) -> Unit
    ) {
        val dialog = Dialog(context)
        val dView = LayoutInflater.from(context)
            .inflate(R.layout.layout_gold_weight_vori_separate_calc, null, false)
        dialog.setContentView(dView)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val etVori = dView.findViewById<EditText>(R.id.etVori)
        val etAna = dView.findViewById<EditText>(R.id.etAna)
        val etRoti = dView.findViewById<EditText>(R.id.etRoti)
        val etPoint = dView.findViewById<EditText>(R.id.etPoint)
        val btnCalculate = dView.findViewById<CardView>(R.id.cv_calculate)

        var selectedAmount: Double? = null
        var selectedUnit: String? = null

        etVori.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

                if (p0.toString().isNotEmpty()) {
                    selectedUnit = WeightUnitEnum.VORI.name
                    selectedAmount = p0.toString().toDouble()
                    if (etAna.text.toString().isNotEmpty()) {
                        etAna.text.clear()
                    }
                    if (etRoti.text.toString().isNotEmpty()) {
                        etRoti.text.clear()
                    }
                    if (etPoint.text.toString().isNotEmpty()) {
                        etPoint.text.clear()
                    }
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        etAna.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

                if (p0.toString().isNotEmpty()) {
                    selectedUnit = WeightUnitEnum.ANA.name
                    selectedAmount = p0.toString().toDouble()
                    if (etVori.text.toString().isNotEmpty()) {
                        etVori.text.clear()
                    }
                    if (etRoti.text.toString().isNotEmpty()) {
                        etRoti.text.clear()
                    }
                    if (etPoint.text.toString().isNotEmpty()) {
                        etPoint.text.clear()
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        etRoti.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

                if (p0.toString().isNotEmpty()) {
                    selectedUnit = WeightUnitEnum.ROTI.name
                    selectedAmount = p0.toString().toDouble()
                    if (etVori.text.toString().isNotEmpty()) {
                        etVori.text.clear()
                    }
                    if (etAna.text.toString().isNotEmpty()) {
                        etAna.text.clear()
                    }
                    if (etPoint.text.toString().isNotEmpty()) {
                        etPoint.text.clear()
                    }
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        etPoint.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

                if (p0.toString().isNotEmpty()) {
                    selectedUnit = WeightUnitEnum.POINT.name
                    selectedAmount = p0.toString().toDouble()
                    if (etVori.text.toString().isNotEmpty()) {
                        etVori.text.clear()
                    }
                    if (etAna.text.toString().isNotEmpty()) {
                        etAna.text.clear()
                    }
                    if (etRoti.text.toString().isNotEmpty()) {
                        etRoti.text.clear()
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })


        btnCalculate.setOnClickListener {

            var isValidInput = true

            if (etVori.text.toString().isEmpty() && etAna.text.toString()
                    .isEmpty() && etRoti.text.toString().isEmpty() && etPoint.text.toString()
                    .isEmpty()
            ) {
                isValidInput = false
                selectedUnit = null
                selectedAmount = null
            }

            if (isValidInput) {
                if (etVori.text.toString().isNotEmpty()) {
                    selectedUnit = WeightUnitEnum.VORI.name
                    selectedAmount = etVori.text.toString().toDouble()

                } else if (etAna.text.toString().isNotEmpty()) {
                    selectedUnit = WeightUnitEnum.ANA.name
                    selectedAmount = etAna.text.toString().toDouble()
                } else if (etRoti.text.toString().isNotEmpty()) {
                    selectedUnit = WeightUnitEnum.ROTI.name
                    selectedAmount = etRoti.text.toString().toDouble()
                } else if (etPoint.text.toString().isNotEmpty()) {
                    selectedUnit = WeightUnitEnum.POINT.name
                    selectedAmount = etPoint.text.toString().toDouble()
                }
                Log.d("tag", "Selected Amount -> $selectedAmount :: Selected Unit -> $selectedUnit")
                dialog.dismiss()
                cb(selectedAmount!!, selectedUnit!!)
            } else {
                Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun getVoriFromGram(gram:Double) : Double{
        return getRoundedDigit(gram/11.664,3)
    }

    fun getVoriFromAna(ana:Double) : Double{
        return getRoundedDigit(ana/16.0,3)
    }

    fun getVoriFromRoti(roti:Double) : Double{
        return getRoundedDigit(roti/64.0,3)
    }

    fun getVoriFromPoint(pount:Double) : Double{
        return getRoundedDigit(pount/512.0,3)
    }



    fun getGramFromVori(vori:Double) : Double{
        return getRoundedDigit(vori*11.664,3)
    }

    fun getGramFromAna(ana:Double) : Double{
        return getRoundedDigit((ana*0.729),3)
    }

    fun getGramFromRoti(roti:Double) : Double{
        return getRoundedDigit((roti*0.182),3)
    }

    fun getGramFromPoint(point:Double) : Double{
        return getRoundedDigit((point*0.0228),3)
    }


    fun getPriceInVoriFromPriceInGram(priceInGram : Double) : Double{
        return  getRoundedDigit(priceInGram*11.664,3)
    }
    fun getPriceInGramFromPriceInVori(priceInVori : Double) : Double{
        return  getRoundedDigit(priceInVori/11.664,3)
    }

    fun getRoundedDigit(number : Double, digit : Int?=3) : Double{
        return String.format("%.${digit}f", number).toDouble()
    }

    fun getPriceWithTax(amount:Double,taxRate : Double?=TAX_RATE):Double{
        return getRoundedDigit(amount+(amount*taxRate!!),3)
    }

    fun getPriceWithMakingCharge(amount:Double,makingChargeRate : Double?=MAKING_CHARGE_RATE):Double{
        return getRoundedDigit(amount+(amount*makingChargeRate!!),3)
    }



}