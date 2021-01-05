package pal.assigments.CombinedLinearCongruentialGenerators

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.pow
import kotlin.system.measureTimeMillis

fun main(){
    print(CombinedLinearCongruentialGenerators(System.`in`).compute())
}

fun log(msg: String){
    System.err.println(msg)
}


class CombinedLinearCongruentialGenerators(input: InputStream) {

    private var time: Long = 0L
    val mMin: Int
    val mMax: Int
    val R: Int

    val sieve: Array<MutableList<Int>>
    val factors: Array<HashMap<Int, Int>>
    var powers: Array<Int>
    val ms: ArrayList<Int>


    var coutner = 0;

    init{
        val conf = BufferedReader(InputStreamReader(input)).readLine().split(" ")
        mMin = conf[0].toInt()
        mMax = conf[1].toInt()
        R = conf[2].toInt()

        sieve = Array(mMax + 1){ mutableListOf() }
        factors = Array(mMax +1){ hashMapOf()}

        ms = ArrayList(mMax-mMin)

        powers = Array(R){2.0.pow(it).toInt()}
    }

    fun compute(): String {

        log("erastothenes: " + measureTimeMillis {
            eratosthenes()
        })

        log("factors: " + measureTimeMillis {
            factors()
        })

        log("filter: " + measureTimeMillis {
            filter()
        })

        log("comb: " + measureTimeMillis {
            comb()
        })


        return coutner.toString()
    }

    fun eratosthenes() {
        val n = mMax
        var p = 2

        while (p <= n) {
            if (this.sieve[p].size == 0) {
                var i = 2*p
                while (i <= n) {
                    this.sieve[i].add(p)
                    i += p
                }
            }
            p++
        }
    }

    fun factors(){
        for( i in mMin-1..mMax){
            val factors = sieve[i]

            if (factors.isEmpty()){
                this.factors[i][i] = 1
                continue
            }

            var m = i
            for ( f in factors){
                var count = 0
                while (m%f==0){
                    m /= f
                    count++
                }

                this.factors[i][f] = count
            }
        }

        for( i in mMin-1..mMax){
            if (sieve[i].isNotEmpty() && sieve[i][0] == 2 )
                sieve[i].removeFirst()
        }

    }


    fun filter(){
        mRage@for( i in mMin..mMax){
            if (this.sieve[i].size != 0)
                continue

            for ( f in this.factors[i-1].entries){
                if (f.value >= 2 && f.key != 2){
                    ms.add(i)
                    continue@mRage
                }
            }
        }
    }


    fun comb(
        start: Int = 0,
        end: Int = ms.size -1,
        index: Int = 0,
        factors: List<Int> =  ArrayList(),
        containsTwo: Boolean = false
    ) {
        if (index == R) {
            coutner++
            return
        }

        var i = start
        comb@while (i <= end && end - i + 1 >= R - index) {
            val next = ms[i]-1
            val nextF = this.factors[next]

            val nextMod2 = next%4 == 0

            if(containsTwo && nextMod2){
                i++
                continue@comb
            }

            for (f in factors) {
                if (nextF.containsKey(f)) {
                    i++
                    continue@comb
                }
            }

            comb(
                i + 1, end, index + 1,
                factors + sieve[next],
                containsTwo || nextMod2
            )

            i++
        }
    }

}