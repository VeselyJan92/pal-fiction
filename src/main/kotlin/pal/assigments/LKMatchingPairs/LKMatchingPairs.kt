package pal.assigments.LKMatchingPairs

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.system.measureTimeMillis

fun main(){
    print(LKMatchingPairs(System.`in`).compute().toString())
}

fun log(msg: String){
    System.err.println(msg)
}

data class LKPair(val s1: Word, val s2: Word){
    val set = setOf(s1, s2)

    override fun equals(other: Any?) = other is LKPair && set == other.set
    override fun hashCode() = set.hashCode()
}

data class Word(val text: String, val range: IntRange){
    val skip  = Array(text.length){0}

    init {
        var counter = 0
        var prev = text.last()
        for (i in text.indices.reversed()) {
            if (prev == text[i]) {
                counter++
            } else {
                prev = text[i]
                counter = 1
            }
            skip[i] = counter
        }

    }

    fun overlap(other: Word) = !(other.range.last < range.first || range.last < other.range.first)

    override fun toString()  = "{$text, $range}"

    override fun equals(other: Any?) = other is Word && text == other.text
}

class LKMatchingPairs(stream: InputStream){
    private val L: Int
    private val K: Int

    private val A: Int
    private val S: String

    private val longWords = mutableSetOf<Word>()
    private val shortWordsCan = mutableSetOf<Word>()

    val set = mutableSetOf<LKPair>()

    init {
        val input = BufferedReader(InputStreamReader(stream))
        val str = input.readLine().split(" ")

        A = str[0].toInt()
        L = str[1].toInt()
        K = str[2].toInt()
        S = input.readLine()
    }

    fun compute(): String {

        val partition = measureTimeMillis { partition() }
        log("Partition: $partition")
        val kPair = measureTimeMillis { KPairProcess() }
        log("KPair: $kPair")
        val kReducedPair = measureTimeMillis { KReducePairProcess() }
        log("KReduced: $kReducedPair" )

        log("Total: " + (partition + kPair + kReducedPair))

        return set.size.toString()
    }

    fun partition(){
        for (i in 0 until S.length - L+1){
            val range = i until i+L
            longWords.add( Word(S.slice(range), range))
        }


        val length = L-K
        for (i in 0 until S.length - length +1){
            val range = i until i+length
            shortWordsCan.add( Word(S.slice(range), range))
        }

    }


    fun KPairProcess(){
        val longWords = longWords.toTypedArray()

        for (i1 in longWords.indices){
            for (i2 in i1 +1 until longWords.size ){
                val s1 = longWords[i1]
                val s2 = longWords[i2]

                if(isKPair(s1, s2, K) && !s1.overlap(s2)){
                    set.add(LKPair(s1, s2))
                }
            }
        }

    }

    fun KReducePairProcess(){



        for (s1 in longWords){
            for (s2 in shortWordsCan){
                if( !s1.overlap(s2) && isKReducedPair(s1, s2, K))
                    set.add(LKPair(s1, s2))
            }
        }
    }


    companion object{

        fun isKReducedPair(word: Word, sub: Word, k:Int): Boolean {
            if (word.text.first() != sub.text.first() && word.text.last() != sub.text.last())
                return false

            var shifted = false
            var i1 = 0
            var i2 = 0

            while (i2 < word.text.length && i1 < sub.text.length){
                when {
                    sub.text[i1] == word.text[i2] -> {
                        val min =  minOf(sub.skip[i1], word.skip[i2])

                        i1+=min
                        i2+=min
                    }
                    !shifted -> {
                        i2+=k
                        shifted = true
                    }
                    else -> return false
                }
            }

            return true
        }

        fun isKPair(s1: Word, s2: Word, k:Int): Boolean {
            val s1 = s1
            val s2 = s2

            var diff = 0

            var i = 0
            while (i < s1.text.length){
                if (s1.text[i] != s2.text[i]){
                    diff++
                    i++

                    if (diff> k) return false

                }else{
                    i += minOf(s1.skip[i], s2.skip[i])
                }
            }

            return diff == k

        }
    }

}


/*
var shifted = false
var i1 = 0
var i2 = 0
while (i2 < word.text.length && i1 < sub.text.length){
    when {
        sub.text[i1] == word.text[i2] -> {
            val min =  minOf(sub.skip[i1], word.skip[i2])

            i1+=min
            i2+=min
        }
        !shifted -> {
            i2+=k
            shifted = true
        }
        else -> return false
    }
}
return true



var counter = 0
var prev = text.last()
for (i in text.indices.reversed()){
    if (prev == text[i]){
        counter ++
    }else{
        prev = text[i]
        counter = 1
    }
    skip[i] = counter
}



for (i in 0.. word.text.length - k){
                val x = word.text.removeRange(i until i+k)

                if (x == sub.text)
                    return true
            }


            return false




            val used = Array(256){0}

            var index = 0
            for (c in sub.text){

                val pos = word.char[c.toInt()].getOrNull(used[c.toInt()])
                if (pos == null || pos < index )
                    return false
                else
                    index = used[c.toInt()]


                used[c.toInt()]++
            }











            */

