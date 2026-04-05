package com.lincoln4791.goldcalculatorbd.uts.calculator

class Calculator {
    fun add(a: Int, b: Int): Int = a + b
    fun subtract(a: Int, b: Int): Int = a - b
    fun multiply(a: Int, b: Int): Int = a * b
    fun divide(a: Int, b: Int): Int {
        require(b != 0) { "Divider cannot be zero" }
        return a / b
    }
}
