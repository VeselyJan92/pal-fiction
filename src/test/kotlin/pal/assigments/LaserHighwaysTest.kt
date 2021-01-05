package pal.assigments

import org.junit.jupiter.api.Test
import pal.Runner
import pal.assigments.LaserHighways.LaserHighways
import pal.assigments.ReducedNetwork.ReducedNetwork
import java.io.InputStream

internal class LaserHighwaysTest: Runner("LaserHighways"){

    @Test
    fun pub01()= runner("pub01")

    @Test
    fun pub02()= runner("pub02")

    @Test
    fun pub03()= runner("pub03")

    @Test
    fun pub04()= runner("pub04")

    @Test
    fun pub05()= runner("pub05")

    @Test
    fun pub06()= runner("pub06")

    @Test
    fun pub07()= runner("pub07")

    @Test
    fun pub08()= runner("pub08")

    @Test
    fun pub09()= runner("pub09")

    @Test
    fun pub10()= runner("pub10")

    override fun run(input: InputStream) = LaserHighways(input).compute()
}