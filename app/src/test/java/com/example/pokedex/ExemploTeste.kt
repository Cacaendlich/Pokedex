package com.example.pokedex

import org.junit.Assert
import org.junit.Test

class ExemploTeste {
    val calc = Calc()
    @Test
    fun calc(){
        Assert.assertEquals(4, calc.soma(2,2))
        Assert.assertEquals(10, calc.soma(8,2))
        Assert.assertEquals(10f, calc.soma(8,2))
    }
}