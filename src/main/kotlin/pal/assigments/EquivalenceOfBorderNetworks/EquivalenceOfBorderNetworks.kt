package pal.assigments.EquivalenceOfBorderNetworks

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.system.measureTimeMillis


var time = 0L

fun main() {
    print(EquivalenceOfBorderNetworks(System.`in`).compute().toString())
}

class EquivalenceOfBorderNetworks(stream: InputStream) {

    val nMin: Int

    val nMax: Int

    val hMin: Int

    val hMax: Int

    val n1RawCertificate: String

    val n2RawCertificate: String

    init {
        val start = System.currentTimeMillis()

        val input = BufferedReader(InputStreamReader(stream))
        val str = input.readLine().split(" ")

        nMin = str[0].toInt()
        nMax = str[1].toInt()
        hMin = str[2].toInt()
        hMax = str[3].toInt()

        n1RawCertificate = input.readLine()
        n2RawCertificate = input.readLine()



        System.err.println("Input processing:" + (System.currentTimeMillis() - start))
        System.err.println("certificate: $time")

    }

    fun compute(): String {
        var match = 0

        val n2BNets: HashMap<String, Int>
        val n1BNets: HashMap<String, Int>

        var time = measureTimeMillis {
            n2BNets = processSubnet(n2RawCertificate)
            n1BNets = processSubnet(n1RawCertificate)
        }

        System.err.println("Certificate: $time")

        time = measureTimeMillis {
            for (net in n2BNets) {
                if (n1BNets.contains(net.key)) match += net.value
            }
        }

        System.err.println("Compare: $time")

        return match.toString()

    }

    fun processSubnet(
            certificate: String
    ): HashMap<String, Int> {
        var cumulativeHeight = 0

        val map = HashMap<String, Int>(certificate.length)
        val networks = ArrayDeque<Int>(certificate.length)
        val heights = ArrayDeque<Int>(certificate.length)

        heights.add(0)

        for (i in certificate.indices) {
            if (certificate.elementAt(i) == '0') {
                networks.add(i)
                heights.add(cumulativeHeight)
                cumulativeHeight++
            } else {
                val start = networks.removeLast()
                val x = heights.removeLast()

                heights.add(maxOf(x, heights.removeLast()))

                if (x - cumulativeHeight + 1 in hMin..hMax && (i - start) / 2 + 1 in nMin..nMax) {
                    val cert = certificate.slice(start..i)

                    val bNetCount = map[cert]

                    if (bNetCount != null)
                        map[cert] = bNetCount + 1
                    else
                        map[cert] = 1
                }

                cumulativeHeight--
            }


        }

        return map

    }

}