package com.lincoln4791.goldcalculatorbd

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.WindowManager

object Utils {

    fun getBanglaDigitFromEnglishDigit(value:String):String{
        //val valu = "12345"
        var valu2 = ""

    for(element in value){
        if(element=='0'){
            valu2=valu2+'০'
        }
        else if(element=='1'){
            valu2=valu2+'১'
        }
        else if(element=='2'){
            valu2=valu2+'২'
        }
        else if(element=='3'){
            valu2=valu2+'৩'
        }
        else if(element=='4'){
            valu2=valu2+'৪'
        }
        else if(element=='5'){
            valu2=valu2+'৫'
        }
        else if(element=='6'){
            valu2=valu2+'৬'
        }
        else if(element=='7'){
            valu2=valu2+'৭'
        }
        else if(element=='8'){
            valu2=valu2+'৮'
        }
        else if(element=='9'){
            valu2=valu2+'৯'
        }
    }

        Log.d("tag","value is -> ${valu2}")
        return valu2

    }

    fun putCommaInNumber(value:String):String{
        var flag=false
        var count=0
        var count2=0
        val num = value.reversed()
        var num2=""

        for(element in num){
            num2 += element
            if(count==2 && !flag){
                num2=num2.plus(",")
                flag=true
                count=0
            }
            if(count==2 && flag){
               num2=num2.plus(",")
               count=0
            }
            count++
            count2++
        }

        val finalValue = if(num2[num2.length-1]==','){
            Log.d("tag","Dropping last")
            num2.dropLast(1)
        }
        else{
            Log.d("tag","Not Dropping last")
            num2
        }

        Log.d("tag","number with comma is -> ${finalValue.reversed()}")
        return finalValue.reversed()
    }

    fun changeNavBarColor(context: Context,activity:Activity){
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

}