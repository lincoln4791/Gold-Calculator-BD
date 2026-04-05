package com.lincoln4791.goldcalculatorbd.common

import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class DayDiffTest {
    @Before
    fun setup() {
        // Perform any necessary setup before each test
    }

    @Test
    fun test(){
        //val result = DayDifference.testValueThatReturn5()
        val result = DayDifference.getBngDigitFromEngDigit("123")
        assertEquals("১২৩", result)
    }}