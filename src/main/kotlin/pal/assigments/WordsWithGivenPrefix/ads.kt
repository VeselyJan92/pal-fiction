package pal.assigments.WordsWithGivenPrefix

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


fun main(){
    print(WordsWithGivenPrefix(System.`in`).compute().toString())
}

data class Transition(
    val destination: Int,
    val letter: Char
)

data class MinMax(
    var min: Int = Int.MAX_VALUE,
    var max: Int = Int.MIN_VALUE
){
    override fun toString() = "$min $max"
}

data class State(
    val id: Int,
    var extremes: MinMax? = null,
    var searched:Boolean = false,
    val transitions: MutableList<Transition> = mutableListOf(),
    var isEnd: Boolean = false
)

class WordsWithGivenPrefix(stream: InputStream){
    val statesCount: Int
    val alphabetSize: Int
    val prefix: String

    val states: Array<State>

    val prefixedStates = mutableSetOf<State>()
    val startState get() = states[0]

    init {
        val input = BufferedReader(InputStreamReader(stream))
        val str = input.readLine().split(" ")

        statesCount = str[0].toInt()
        alphabetSize = str[1].toInt()

        states = Array(statesCount){ State(it) }

        repeat(statesCount){
            val line = input.readLine().split(" ").filter { it != "" }

            val stateIndex = line[0].toInt()
            val state = states[stateIndex]
            state.isEnd = line[1] == "F"

            var letter = line[2].first()

            for ( i in 2 until line.size){
                if (line[i].first().isLetter())
                    letter = line[i].first()
                else
                    state.transitions.add(Transition(line[i].toInt(), letter))
            }
        }
        prefix = input.readLine()
    }

    fun compute(): String {
        findPrefixedStates()

        searchMaxMin()

        val extremes = collectExtremes()

        return extremes.toString()
    }

    fun findPrefixedStates(state: State = startState, prefix: String = this.prefix){
        if (prefix.isEmpty()){
            prefixedStates.add(state)
            return
        }

        state.transitions.forEach {
            if (it.letter == prefix.first())
                findPrefixedStates(states[it.destination], prefix.slice(1 until prefix.length))
        }

    }

    fun searchMaxMin(state: State = startState): MinMax? {
        if (state.searched)
            return state.extremes
        else {
            var extreme: MinMax? = null

            for (it in state.transitions) {
                val nestedExtreme = searchMaxMin(states[it.destination]) ?: continue

                if (extreme == null)
                    extreme = MinMax( nestedExtreme.min +1, nestedExtreme.max +1)
                else{
                    extreme.max = maxOf(extreme.max, nestedExtreme.max + 1)
                    extreme.min = minOf(extreme.min, nestedExtreme.min + 1)
                }
            }

            if(state.isEnd){
                extreme = MinMax(0, extreme?.max ?: 0)
            }

            state.searched = true
            state.extremes = extreme
            return extreme
        }
    }


    fun collectExtremes(): MinMax {
        val extreme = MinMax()

        for ( state in prefixedStates) {
            val ext = state.extremes ?: continue

            extreme.max =  maxOf(extreme.max, ext.max)
            extreme.min =  minOf(extreme.min, ext.min)
        }

        extreme.max += prefix.length
        extreme.min += prefix.length

        return extreme
    }

}