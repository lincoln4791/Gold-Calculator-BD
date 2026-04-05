package com.lincoln4791.goldcalculatorbd.uts.myUtil

import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class MyUtilTest {

    @Before
    fun setup() {
        // Perform any necessary setup before each test
    }

    @Test
    fun test(){
        val result = MyUtil.testReturn5()
        assertEquals("5", result)

    }

}