package task_4

import utils.Arc
import utils.DirectedGraph
import java.util.*


fun dinicMaxFlow(source: Int, target: Int, graph: DirectedGraph) : Pair<Long, HashMap<String, Int>> {
    var maxFlow: Long = 0L
    val nodesSP: IntArray = IntArray(graph.size) { 0 }

    val flowMap = HashMap<String, Int>()
    graph.adj.forEach { it.forEach{ arc ->
        flowMap[arc.toStringKey()] = 0
        flowMap[arc.toReversedStringKey()] = 0
    } }

    fun tryBFS() : Boolean {
        for (i in 0 until graph.size) {
            nodesSP[i] = -1
        }
        nodesSP[source] = 0

        val q = LinkedList<Int>()
        q.add(source)
        var i: ListIterator<Arc>
        while (q.size != 0) {
            val u = q.poll()
            i = graph[u].listIterator()
            while (i.hasNext()) {
                val arc: Arc = i.next()
                if (nodesSP[arc.tg] < 0 && flowMap[arc.toStringKey()]!! < arc.cp) {
                    nodesSP[arc.tg] = nodesSP[u] + 1
                    q.add(arc.tg)
                }
            }
        }
        return nodesSP[target] >= 0
    }

    fun sendFlow(u: Int, flow: Int, start: IntArray): Int {
        if (u == target) {
            return flow
        }
        while (start[u] < graph[u].size) {
            val arc: Arc = graph[u][start[u]]
            if (nodesSP[arc.tg] == nodesSP[u] + 1 && flowMap[arc.toStringKey()]!! < arc.cp) {
                // find minimum flow from u to target
                val currFlow = flow.coerceAtMost((arc.cp - flowMap[arc.toStringKey()]!!))
                val tempFlow = sendFlow(arc.tg, currFlow, start)

                if (tempFlow > 0) {
                    flowMap[arc.toStringKey()] = flowMap[arc.toStringKey()]!! + tempFlow
                    flowMap[arc.toReversedStringKey()] = flowMap[arc.toReversedStringKey()]!! - tempFlow
                    return tempFlow
                }
            }
            start[u]++
        }
        return 0
    }

    while (tryBFS()) {
        val start = IntArray(graph.size + 1)
        while (true) {
            val flow = sendFlow(source, Int.MAX_VALUE, start)
            if (flow == 0) {
                break
            }
            maxFlow += flow
        }
    }
    return Pair(maxFlow, flowMap)
}


fun main() {
    val k = 5
//    val graph = HammingGraphGenerator.generateGraph(k)
    val graph = DirectedGraph.graphFromFile("tmpG")
//    graph.dumpToFile("tmpG")
    val source = 0
    val target = graph.size - 1
    println("source: $source, target: $target")

    val res = dinicMaxFlow(source, target, graph)
    println("MAX FLOW: ${res.first}, FORMAT:") // result format
    println("arc_source arc_target arc_capacity arc_flow")
    graph.adj.forEach { it.forEach{ arc ->
        val key = arc.toStringKey()
        println("arc: $key, ${res.second[key]}")
    } }
}
