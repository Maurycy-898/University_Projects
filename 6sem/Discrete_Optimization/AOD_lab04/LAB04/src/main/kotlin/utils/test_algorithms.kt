package utils

import task_1.maxFlow
import task_2.maxBipartiteMatching
import task_4.dinicMaxFlow
import java.io.File
import kotlin.system.measureNanoTime

// test max flow algorithm
fun testMF(filename: String) {
    val file = File("src\\main\\kotlin\\out_results\\$filename.csv")
    file.appendText("k;MAX_FLOW;TIME;\n")
    val tries = 10
    for (k in 1 .. 16) {
        var totalTime = 0L
        var totalFlow = 0L
        repeat(tries) {
            val graph = HammingGraphGenerator.generateGraph(k)
            val res: Pair<Long, HashMap<String, Int>>
            val execTime = measureNanoTime {
                res = maxFlow(0, (graph.size-1), graph)
            }
            totalTime += execTime
            totalFlow += res.first
        }
        totalTime /= tries
        totalFlow /= tries
        file.appendText("$k;$totalFlow;$totalTime;\n")
    }
}

// test max bipartite matching algorithm
fun testMBM(filename: String) {
    val file = File("src\\main\\kotlin\\out_results\\$filename.csv")
    file.appendText("k;i;MAX_FLOW;TIME;\n")
    val tries = 10
    for (k in 3 .. 10) for (i in 1..k) {
        var totalTime = 0L
        var totalFlow = 0L
        repeat(tries) {
            val graphData = BipartiteGraphGenerator.generateGraph(k, i)
            val graph = graphData.first
            val nodes1 = graphData.second
            val nodes2 = graphData.third

            val res: Pair<Long, List<String>>
            val execTime = measureNanoTime {
                res = maxBipartiteMatching(nodes1, nodes2, graph)
            }
            totalTime += execTime
            totalFlow += res.first
        }
        totalTime /= tries
        totalFlow /= tries
        file.appendText("$k;$i;$totalFlow;$totalTime;\n")
    }
}

// test dinic max flow algorithm
fun testDMF(filename: String) {
    val file = File("src\\main\\kotlin\\out_results\\$filename.csv")
    file.appendText("k;MAX_FLOW;TIME;\n")
    val tries = 10
    for (k in 1 .. 16) {
        var totalTime = 0L
        var totalFlow = 0L
        repeat(tries) {
            val graph = HammingGraphGenerator.generateGraph(k)
            val res: Pair<Long, HashMap<String, Int>>
            val execTime = measureNanoTime {
                res = dinicMaxFlow(0, (graph.size-1), graph)
            }
            totalTime += execTime
            totalFlow += res.first
        }
        totalTime /= tries
        totalFlow /= tries
        file.appendText("$k;$totalFlow;$totalTime;\n")
    }
}

// test algorithms:
fun main() {
    testMF(filename="mf_res")
    testMBM(filename="mbm_res")
    testDMF(filename="dmf_res")
}
