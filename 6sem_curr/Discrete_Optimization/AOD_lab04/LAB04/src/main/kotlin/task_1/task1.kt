package task_1

import utils.Arc
import utils.DirectedGraph
import java.util.*


fun maxFlow(source: Int, target: Int, graph: DirectedGraph) : Pair<Long, HashMap<String, Int>> {
    var maxFlow = 0L
    val flowMap = HashMap<String, Int>()
    graph.adj.forEach { it.forEach{ arc ->
        flowMap[arc.toStringKey()] = 0
        flowMap[arc.toReversedStringKey()] = 0
    } }

    while (true) {
        val parent = arrayOfNulls<Arc?>(graph.size)
        val queue = LinkedList<ArrayList<Arc>>()
        queue.add(graph[source])

        // BFS finding the shortest augmenting path
        while (queue.isNotEmpty()) {
            val adj = queue.poll()
            for (arc in adj) {
                if (parent[arc.tg] == null && arc.tg != source && arc.cp > flowMap[arc.toStringKey()]!!) {
                    parent[arc.tg] = arc // path also keeps track of visited nodes
                    queue.add(graph[arc.tg])
                }
            }
        }
        // If target was NOT reached -> no legal path was found.
        if (parent[target] == null) { break }

        // If target WAS reached, we will push more flow through the path
        var pushFlow = Int.MAX_VALUE

        // Finds maximum flow that can be pushed through given path
        var arc: Arc? = parent[target]
        while (arc != null) { // source's parent is null
            pushFlow = pushFlow.coerceAtMost((arc.cp - flowMap[arc.toStringKey()]!!))
            arc = parent[arc.sc]
        }

        // Adds to flow values and subtracts from reverse flow values in path
        arc = parent[target]
        while (arc != null) { // source's parent is null
            flowMap[arc.toStringKey()] = flowMap[arc.toStringKey()]!! + pushFlow
            flowMap[arc.toReversedStringKey()] = flowMap[arc.toReversedStringKey()]!! - pushFlow
            arc = parent[arc.sc]
        }
        maxFlow += pushFlow
    }
    // Return maxFlow and flow in each arc
    return Pair(maxFlow, flowMap)
}


fun main() {
    val k = 2
//    val graph = HammingGraphGenerator.generateGraph(k)
    val graph = DirectedGraph.graphFromFile("tmpG")
//    graph.dumpToFile("tmpG")
    val source = 0
    val target = graph.size - 1
    println("source: $source, target: $target")

    val res = maxFlow(source, target, graph)
    println("MAX FLOW: ${res.first}, FORMAT:") // result format
    println("arc_source arc_target arc_capacity arc_flow")
    graph.adj.forEach { it.forEach{ arc ->
        val key = arc.toStringKey()
        println("arc: $key, ${res.second[key]}")
    } }
}
