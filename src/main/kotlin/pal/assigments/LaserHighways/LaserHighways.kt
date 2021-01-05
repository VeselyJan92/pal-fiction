package pal.assigments.LaserHighways

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*


fun main() {
    val alg = LaserHighways(System.`in`)
    println(alg.compute())
}

private data class City(
        val id: Int,
        var connected: Boolean,
        val connections: MutableList<Edge>
)

private data class Edge(
        val from: Int,
        val destination: Int,
        val cost: Short
): Comparable<Edge> {

    override fun toString() = "$from:$destination / $cost"

    override fun compareTo(other: Edge) = this.cost - other.cost
}

class LaserHighways(stream: InputStream) {
    private var cities: Int
    private var connections: Int
    private var capitol: Int
    private var data: Array<City>

    init {
        val input = BufferedReader(InputStreamReader(stream))
        var str = input.readLine()


        var line = str.split(" ")
        cities = line[0].toInt()
        connections = line[1].toInt()
        capitol = line[2].toInt()

        data = Array(cities) { City(it, false, mutableListOf()) }

        var coutner = 0;

        while (input.readLine().also { str = it } != null) {
            line = str.split(" ")

            val from = line[0].toInt()
            val to = line[1].toInt()
            val cost = line[2].toShort()

            data[from].connections.add(Edge(from, to, cost))
            data[to].connections.add(Edge(to, from, cost))

            coutner++
        }
    }


    fun compute(): String {
        var cost = 0L
        var processed  = 1

        var inner: List<Int> = mutableListOf(capitol)
        data[capitol].connected = true


        while (processed != cities) {
            val q = PriorityQueue<Edge>()

            val outer = HashSet<Int>()

            for (known in inner) {
                val branching = data[known].connections

                for (edge in branching) {

                    if (!data[edge.destination].connected) {
                        outer.add(edge.destination)
                        q.add(edge)
                    }

                }
            }

            inner = outer.toList()

            while (outer.size != 0) {
                val edge = q.poll()

                if (!data[edge.destination].connected) {
                    data[edge.destination].connected = true

                    q.addAll(data[edge.destination].connections.filter { outer.contains(it.destination) })

                    cost += edge.cost
                    processed++
                    outer.remove(edge.destination)
                }
            }
        }

        return cost.toString()
    }

}