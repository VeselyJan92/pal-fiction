package pal

import org.junit.jupiter.api.Assertions.*
import java.io.InputStream

abstract class Runner(val assigment: String){

    fun runner(dataset: String){
        val loader = Runner::class.java.classLoader

        val input = loader.getResource("${assigment}/$dataset.in")!!.openStream()

        val expected = loader.getResource("${assigment}/$dataset.out")!!.readText().trim()

        val actual = run(input)

        assertEquals(expected, actual)
    }

    abstract fun run(input: InputStream):String

}