package com.lincoln4791.goldcalculatorbd.demo

open class MyUseCase(var myInterface : MyInterface) {
     open fun getDataFromUSeCAse() : String{
        return myInterface.getData()
    }
}