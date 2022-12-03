package com.lincoln4791.goldcalculatorbd

fun main(){

    

}


fun getVoriFromGram(){

    val gram = 4.7
    var vori = (gram*.08576)
    var number = vori*960

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


    print("vori $finalVori :: anar $finalAna :: roti $finalRoti :: point $finalPoint")

}