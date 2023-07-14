package task_2

import task_1.maxFlow
import utils.BipartiteGraphGenerator
import utils.DirectedGraph
import utils.UndirectedGraph
import java.util.*


fun maxBipartiteMatching(nodes1: IntArray, nodes2: IntArray, graph: UndirectedGraph) : Pair<Long, List<String>> {
    val dg = DirectedGraph(graph.size + 2)
    val source = 0
    val target = graph.size + 1
    val capacity = 1

    for (n1 in nodes1) {
        dg.addArc(source, (n1+1), capacity)
        for (n2 in graph[n1]) {
            dg.addArc((n1+1), (n2+1), capacity)
        }
    }
    for (n2 in nodes2) {
        dg.addArc((n2+1), target, capacity)
    }
    dg.dumpToFile("tmpDG")

    val maxFlowRes = maxFlow(source, target, graph=dg)
    val maxFlow = maxFlowRes.first
    val flowMap = maxFlowRes.second

    val matches = LinkedList<String>()
    dg[source].forEach { candidate ->
        if (flowMap[candidate.toStringKey()]!! > 0) {
            dg[candidate.tg].forEach { arc ->
                if (flowMap[arc.toStringKey()]!! > 0) {
                    matches.add("${arc.sc} -> ${arc.tg}")
                }
            }
        }
    }
    return Pair(maxFlow, matches)
}


fun main() {
    val k = 7
    val i = 5

    val genData = BipartiteGraphGenerator.generateGraph(k, i)
    val graph = genData.first
    val nodes1 = genData.second
    val nodes2 = genData.third

    graph.dumpToFile("tmpUG")
    println("Nodes1:")
    nodes1.forEach { print("$it, ") }
    println("\nNodes2:")
    nodes2.forEach { print("$it, ") }
    println()

    val res = maxBipartiteMatching(nodes1, nodes2, graph)
    println("MAX MATCHING: ${res.first}, MATCHES:") // result format
    res.second.forEach { println(it) }
}
