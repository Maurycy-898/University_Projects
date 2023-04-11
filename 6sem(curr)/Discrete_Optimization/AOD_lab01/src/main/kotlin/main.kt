
fun main() {
    val g = Graph.fromFile("src/main/resources/testsGotfryd/aod_testy1/3/g3-6.txt")
//    g.edges.forEachIndexed() { idx, v -> println("$idx : $v") }

//    val resBFS = Graph.performBFS(g)
    val resDFS = Graph.performDFS(g)
//    val resTOrder = Graph.stronglyConectedComponents(g)
//    println(resTOrder)


//    println("\nResults of BFS:")
//    println(resBFS.first)
//    println(resBFS.second)
//
    println("\nResults of DFS:")
    println(resDFS.first)
    println(resDFS.second)

//    println("\nResults of T-Ordering:")
//    if (!resTOrder.first) {
//        println(resTOrder.second)
//    }
//    else {
//        println("Graph has a cycle!")
//    }

//    println("\nResults of SCC:")
//    println(Graph.stronglyConnectedComponents(g))

//    println("\nResults of is Bipartite Graph:")
//    println(Graph.isBipartiteGraph(g))

}
