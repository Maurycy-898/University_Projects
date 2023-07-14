package task_3

import utils.DirectedGraph
import utils.UndirectedGraph
import java.io.File


fun maxFlowGLPK(filename: String, graph: DirectedGraph, additionalParams: String="") {
    val sb = StringBuilder()
    sb.append(
        "/* Author: Maurycy Sosnowski */\n\n" +
        "/* Graph size */\n" +
        "param n, integer, > 1;\n" +
        "set Nodes := {1..n};\n\n" +
        "param SourceNode in Nodes, default 1;\n" +
        "param TargetNode in Nodes, != SourceNode, default n;\n\n" +
        "set Arcs within Nodes cross Nodes;\n\n" +
        "param Capacities{(i,j) in Arcs}, >= 0;\n\n" +
        "var x{(i,j) in Arcs}, >= 0, <= Capacities[i,j];\n\n" +
        "var totalFlow, >= 0;\n\n" +
        "s.t. node{i in Nodes}:\n" +
        "\t(sum{(i,j) in Arcs} x[i,j]) - (sum{(j,i) in Arcs} x[j,i]) =\n" +
        "\t(if (i = SourceNode) then totalFlow else if (i = TargetNode) then -totalFlow else 0);\n\n" +
        "maximize obj: totalFlow;\n\n" +
         additionalParams +
        "data;\n" +
        "param n := ${graph.size};\n\n" +
        "param SourceNode := 1;\n" +
        "param TargetNode := ${graph.size};\n\n"
    )
    sb.append("param: Arcs : Capacities :=")
    graph.adj.forEach {
        it.forEach { arc ->
            sb.append("\n${arc.sc+1} ${arc.tg+1} ${arc.cp}")
        }
    }
    sb.append(";\n\nend;\n")
    val file = File("src\\main\\kotlin\\out_glpk\\$filename.txt")
    file.writeText(sb.toString())
}


fun bipartiteGLPK(filename: String, nodes1: IntArray, nodes2: IntArray, graph: UndirectedGraph) {
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
    maxFlowGLPK(filename, graph=dg, additionalParams =
        "solve;\n" +
        "printf \"Start node    End node\\n\";\n" +
        "printf \"------------  ------------\\n\";\n" +
        "printf{(i,j) in Arcs: (x[i,j] > 0) and (i != SourceNode) and (j != TargetNode)}: \"%11s   %11s\\n\", i, j;\n\n"
    )
}


fun main() {
    val graph = DirectedGraph.graphFromFile("tmpG")
    maxFlowGLPK("out_mf1", graph)
}
