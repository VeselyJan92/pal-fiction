package pal.assigments.MinibusTrips

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Integer.max
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {
    println(MinibusTrips(System.`in`).compute())
}

data class Vertex(
        val connections: ArrayList<Edge>,
        var dfsIndex: Int = Int.MAX_VALUE,
        var dfsLowlink: Int = Int.MAX_VALUE,
        var component: Int = -1,
        var safe: Boolean = true,
        var inStack: Boolean = false
){
    val visited: Boolean
        get() = dfsIndex != Int.MAX_VALUE
}

data class Edge(
        val from:Int,
        val to: Int
)

data class Component(
        val index: Int,
        val vertexes: LinkedList<Vertex>,
        val prev: MutableSet<Component> = HashSet(),
        var end: Boolean = true
){
    var safeStops: Int = 0
    var prevMax = -1;

    override fun hashCode() = index

    override fun equals(other: Any?) = (other is Component ) && this.index == other.index

    fun max(): Int{
        if (prevMax == -1)
            prevMax = when {
                prev.isEmpty() -> safeStops
                else -> prev.maxOf { it.max() } + safeStops
            }
        return prevMax
    }

}

class MinibusTrips(stream: InputStream) {

    val stops: Int
    val connections: Int

    val data: Array<Vertex>

    var dfsIndex: Int = 0;
    val dfsStack = Stack<Vertex>()

    val components = ArrayList<Component>()

    var componentIndex = 0;

    init {
        val input: BufferedReader
        var str: String?
        var time = measureTimeMillis {
            input = BufferedReader(InputStreamReader(stream), 8192*16)
            str = input.readLine()

            var line = str!!.split(" ")
            stops = line[0].toInt()
            connections = line[1].toInt()


            data = Array(stops) { Vertex(ArrayList(connections/stops * 4)) }

            str = input.readLine()
        }
        println("prep $time")


        time = measureTimeMillis {
            while (str != null) {
                val p = str!!.indexOf(" ")

                val from = str!!.substring(0, p).toInt()
                val to = str!!.substring(p + 1, str!!.length).toInt()
                data[from].connections.add(Edge(from, to))
                str = input.readLine()
            }
        }
        println("reading $time")


    }

    fun compute() : String {

        var time = measureTimeMillis {
            for (vertex in data){
                if ( !vertex.visited ){
                    dfs(vertex)
                }
            }
        }

        println("trajan: $time")

        time = measureTimeMillis {
            for (component in components){
                component.safeStops = component.vertexes.count { it.safe }
            }
        }

        println("count: $time")

        time = measureTimeMillis {
            for (component in components){
                for (vertex in component.vertexes){
                    for (edge in vertex.connections){
                        val from = data[edge.from]
                        val to = data[edge.to]

                        if (from.component != to.component){
                            components[to.component].prev.add(components[from.component])
                            components[from.component].end = false
                        }

                    }
                }
            }

            for (component in components){
                component.prev.remove(component)
            }
        }

        println("components: $time")

        var max = 0
        time = measureTimeMillis {
            val ends = components.filter { it.end}

            for (end in ends) {
                max = max(end.max(), max)
            }

        }

        println("max: $time")

        return max.toString()

    }


    fun dfs(vertex: Vertex){
        dfsIndex++
        vertex.dfsLowlink = dfsIndex
        vertex.dfsIndex = dfsIndex

        dfsStack.push(vertex)
        vertex.inStack = true

        for (edge in vertex.connections){
            val to = data[edge.to]
            when{
                !to.visited ->{
                    dfs(to)
                    vertex.dfsLowlink = min(vertex.dfsLowlink, to.dfsLowlink)
                }
                to.inStack -> { //TODO possible bottle neck
                    vertex.dfsLowlink = min(vertex.dfsLowlink, to.dfsIndex)
                }
            }
        }

        if (vertex.dfsLowlink == vertex.dfsIndex){
            val component = Component(componentIndex, LinkedList())

            do {
                val pop = dfsStack.pop()
                pop.inStack = false
                pop.component = componentIndex
                component.vertexes.add(pop)

            }while (pop.dfsIndex != vertex.dfsIndex)

            for(v in component.vertexes){
                for (edge in v.connections){
                    if (data[edge.to].component != componentIndex){
                        data[edge.to].safe = false
                        data[edge.from].safe = false
                    }
                }
            }

            components.add(componentIndex, component)
            componentIndex++
        }
    }

}