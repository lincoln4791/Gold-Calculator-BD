package com.lincoln4791.goldcalculatorbd.demo

class MyUseCase2(var myInterfaceImpl : MyInterfaceImpl2) : MyUseCase(myInterfaceImpl) {
    /*override fun getDataFromUSeCAse() : String{
       // return myInterfaceImpl.getData()
        return "commended"
    }*/

     fun getData(): String {
        return "ggwp New"
    }
}