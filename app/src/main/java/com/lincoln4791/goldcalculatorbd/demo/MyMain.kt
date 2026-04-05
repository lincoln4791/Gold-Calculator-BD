package com.lincoln4791.goldcalculatorbd.demo

import android.util.Log

fun main(){
    var impl  = MyInterfaceImpl()
    print("1 -> ${impl.getData()}\n\n")
    var myUseCase = MyUseCase(impl);
    print("2 -> ${myUseCase.getDataFromUSeCAse()}\n\n")
    var impl2  = MyInterfaceImpl2()
    var myUseCase2 = MyUseCase2(impl2)
    print("3 -> ${myUseCase2.getDataFromUSeCAse()}\n\n")
}