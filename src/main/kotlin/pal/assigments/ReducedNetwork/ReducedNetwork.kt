package pal.assigments.ReducedNetwork

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

fun main() {
    val alg = ReducedNetwork(System.`in`)
    print(alg.compute())
}

private data class Edge(
    val from: Int,
    val to: Int,
    val weight: Int

) : Comparable<Edge> {

    companion object{
        val empty = Edge(-1, -1, Int.MAX_VALUE)
    }

    override fun compareTo(other: Edge) = this.weight - other.weight
}

private data class Vertex(
    val connections: List<Edge>,
    var excluded: Boolean = false,
    var visited: Boolean = false,
    var min: Edge = Edge(-1, -1, Int.MAX_VALUE)
)

class ReducedNetwork(stream: InputStream) {
    val n: Int
    val k: Int

    private val vertexes: Array<Vertex>

    var min = Int.MAX_VALUE

    val combN: IntArray
    var combK: IntArray

    init {
        val input = BufferedReader(InputStreamReader(stream))

        val x = input.readLine()
        val settings = x.split(" ")

        n = settings[0].toInt()
        k = settings[1].toInt()

        combN = IntArray(n){it}
        combK = IntArray(k){-1}

        vertexes = Array(n) {
            val connections = input
                .readLine()
                .split(" ")
                .filterNot { it.isEmpty() }
                .mapIndexedNotNull { index, s ->
                    val weight = s.toInt()

                    if (weight == 0)
                        null
                    else
                        Edge(it, index, weight)
                }

            Vertex(
                connections = connections
            )
        }
    }


    fun compute(): String {
        comb(0, vertexes.size-1, 0, k)
        return min.toString();
    }

    /**
     * Prims algorithm for MTS
     */
    private fun minimumSpanningTree() {
        for (it in vertexes) {
            it.min = Edge.empty
            it.visited = false
        }

        var cost = 0
        var processed  = 1

        addVertex(vertexes.first { !it.excluded })

        while (processed <= n-k-1) {

            if(cost > min) return

            val next = nextVertex() ?: return

            addVertex(vertexes[next.to])

            processed ++
            cost += next.weight
        }

        min = minOf(min, cost)
    }

    /**
     * Add vertex to MST
     * set visited = true
     * update minimum access edge in all connected edges
     */
    private fun addVertex(vertex: Vertex){
        vertex.visited = true
        for (edge in vertex.connections){
            if (!vertexes[edge.to].visited && vertexes[edge.to].min > edge){
                vertexes[edge.to].min = edge
            }
        }
    }

    /**
     * Find minimum edge by iterating over minimum access edge in all unvisited vertexes
     */
    private fun nextVertex(): Edge? {
        var min: Edge = Edge.empty
        for (vertex in vertexes){

            if(vertex.excluded)
                continue

            if ((!vertex.visited && (vertex.min < min ))){
                min = vertex.min
            }
        }

        return if (min.to == -1) null else min
    }

    /**
     * Create all combinations of possible excluded vertexes and build MTS
     */
    private fun comb(start: Int, end: Int, index: Int, r: Int) {
        if (index == r) {
            minimumSpanningTree()
            return
        }

        var i = start
        while (i <= end && end - i + 1 >= r - index) {
            vertexes[i].excluded = true
            combK[index] = combN[i]
            comb(i + 1, end, index + 1, r)
            vertexes[i].excluded = false
            i++
        }
    }

}
