package com.example.pokedex

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    fun som(n1: Int, n2: Int): Int{
        return n1 + n2
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun addition_inCorrect() {
        assertEquals(8, som(6, 2))
    }

    @Test
    fun subtraction_isCorrect() {
        assertEquals(2, 4 - 2)
    }

}
