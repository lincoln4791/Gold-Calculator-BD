//package com.lincoln4791.goldcalculatorbd.uts.calculator

import com.lincoln4791.goldcalculatorbd.uts.calculator.Calculator
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CalculatorTest {

    private lateinit var calculator: Calculator

    @Before
    fun setup() {
        calculator = Calculator()
    }

    @Test
    fun testAddition() {
        val result = calculator.add(2, 3)
        assertEquals(5, result)
    }

    @Test
    fun testSubtraction() {
        val result = calculator.subtract(5, 3)
        assertEquals(2, result)
    }

    @Test
    fun testMultiplication() {
        val result = calculator.multiply(4, 5)
        assertEquals(20, result)
    }

    @Test
    fun testDivision() {
        val result = calculator.divide(10, 2)
        assertEquals(5, result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testDivideByZero() {
        calculator.divide(10, 0)
    }
}
