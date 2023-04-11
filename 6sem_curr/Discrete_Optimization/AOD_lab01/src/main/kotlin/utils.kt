import java.io.File
import kotlin.collections.ArrayList
import java.util.*


/** Graph Types */
enum class GraphType {
    DIRECTED, UNDIRECTED
}


/** Graph representation */
class Graph(val size: Int, val type: GraphType) {
    // Edges as List of Lists of vertices connected to each vertex (represented by index)
    val edges = ArrayList( List<LinkedList<Int>>((size + 1)) { LinkedList() } )

    /** Add new Edge to the Graph */
    fun addEdge(v: Int, w: Int) {
        if (v > size || v < 1 || w > size || w < 1) {
            throw java.lang.Exception("No such vertices exist!")
        }

        if (!this.edges[v].contains(w)) {
            this.edges[v].add(w)
        }
    }

    /** Utility functions for Graph */
    companion object {
        @JvmStatic
        fun fromFile(fileName: String): Graph {
            val file = File(fileName)
            val lines = file.readLines()

            val type = if (lines[0] == "D") GraphType.DIRECTED else GraphType.UNDIRECTED
            val size = lines[1].toInt()

            val g = Graph(size, type)

            val verticesNo = lines[2].toInt()
            for (lineIdx in 3 until (verticesNo + 3)) {
                val line = lines[lineIdx].split(" ")
                g.addEdge(line[0].toInt(), line[1].toInt())
                if (type == GraphType.UNDIRECTED) {
                    g.addEdge(line[1].toInt(), line[0].toInt())
                }
            }

            return g
        }

        // ------------------------ GRAPH ALGORITHMS -------------------------------------------------------------------
        @JvmStatic
        fun transpose(g: Graph): Graph {
            val gT = Graph(g.size, g.type)
            g.edges.forEachIndexed { outV, inVs ->
                inVs.forEach { inV ->
                    gT.addEdge(inV, outV)
                }
            }
            return gT
        }

        @JvmStatic
        fun performBFS(g: Graph) : Pair<List<Int>, List<Int>> {
            val visited = ArrayList<Boolean>( List(g.size + 1) { false } )
            val parents = ArrayList<Int>( List(g.size + 1) { 0 } )
            val traversal = LinkedList<Int>()

            for (i in 1 .. g.size) {
                if (!visited[i]) {
                    val queue = LinkedList<Int>()
                    visited[i] = true
                    queue.add(i)

                    while (queue.size > 0) {
                        val n: Int = queue.poll()
                        traversal.add(n)

                        g.edges[n].forEach { v ->
                            if (!visited[v]) {
                                parents[v] = v
                                visited[v] = true
                                queue.add(v)
                            }
                        }
                    }
                }
            }

            parents.removeFirst()
            return Pair(traversal, parents)
        }

        @JvmStatic
        private fun visitDFS(g: Graph, v: Int, visited: ArrayList<Boolean>,
                             traversal: LinkedList<Int>, parents: ArrayList<Int>?) {
            visited[v] = true
            traversal.add(v)

            g.edges[v].forEach { n ->
                if (!visited[n]) {
                    visited[n] = true
                    if (parents != null) {
                        parents[n] = v
                    }
                    visitDFS(g, n, visited, traversal, parents)
                }
            }
        }

        @JvmStatic
        fun performDFS(g: Graph) : Pair<List<Int>, List<Int>> {
            val traversal = LinkedList<Int>()
            val visited = ArrayList<Boolean>( List(g.size + 1) { false } )
            val parents = ArrayList<Int>( List(g.size + 1) { 0 } )

            for (i in 1 .. g.size) {
                if (!visited[i]) {
                    visitDFS(g, i, visited, traversal, parents)
                }
            }

            parents.removeFirst()
            return Pair(traversal, parents)
        }

        @JvmStatic
        fun topologicalOrdering(g: Graph): Pair<Boolean, List<Int>?> {
            val inDegree = ArrayList<Int>( List(g.size + 1) { 0 } )
            val zeroDegreeList = LinkedList<Int>()
            val tOrderList = LinkedList<Int>()

            g.edges.forEach { outV ->
                outV.forEach { inV ->
                    inDegree[inV] += 1
                }
            }
            for (i in 1 .. g.size) {
                if (inDegree[i] == 0) {
                    zeroDegreeList.add(i)
                }
            }

            var next = 0
            while (zeroDegreeList.size > 0) {
                val n = zeroDegreeList.poll()
                tOrderList.add(n)
                next += 1

                g.edges[n].forEach { inV ->
                    inDegree[inV] -= 1
                    if (inDegree[inV] == 0) {
                        zeroDegreeList.add(inV)
                    }
                }
            }

            val hasCycle: Boolean = next < g.size
            return if (hasCycle) Pair(true, null)  else Pair(false, tOrderList)
        }

        @JvmStatic
        private fun fillOrder(g: Graph, v: Int, visited: ArrayList<Boolean>, stack: LinkedList<Int>) {
            visited[v] = true
            g.edges[v].forEach { n ->
                if (!visited[n]) {
                    fillOrder(g, n, visited, stack)
                }
            }
            stack.push(v)
        }

        @JvmStatic
        fun stronglyConectedComponents(g: Graph) : List<List<Int>> {
            val scComponents = LinkedList<LinkedList<Int>>()
            val visited = ArrayList<Boolean>( List(g.size + 1) { false } )
            val stack = LinkedList<Int>()

            for (i in 1..g.size) {
                if (!visited[i]) {
                    fillOrder(g, i, visited, stack)
                }
            }

            val gT = Graph.transpose(g)
            visited.forEachIndexed { idx, _ -> visited[idx] = false }

            while (stack.size > 0) {
                val n: Int = stack.poll()
                val scComponent = LinkedList<Int>()

                if (!visited[n]) {
                    visitDFS(gT, n, visited, scComponent, null)
                    scComponents.add(scComponent)
                }
            }
            return scComponents
        }

        @JvmStatic
        fun isBipartiteGraph(g: Graph) : Pair<Boolean, Pair<LinkedList<Int>, LinkedList<Int>>?> {
            val colors = ArrayList<Int>( List(g.size + 1) { -1 } )
            val queue = LinkedList<Pair<Int, Int>>()

            for (i in 1..g.size) {
                if (colors[i] == -1) {
                    queue.add(Pair(i, 0))
                    colors[i] = 0

                    while (queue.size > 0) {
                        val pair = queue.poll()
                        val vertex = pair.first
                        val color = pair.second

                        g.edges[vertex].forEach { n ->
                            if (colors[n] == color) {
                                return Pair(false, null)
                            }

                            if (colors[n] == -1) {
                                colors[n] = if (color == 1) 0 else 1
                                queue.add(Pair(n, colors[n]))
                            }
                        }
                    }
                }
            }
            val color0 = LinkedList<Int>()
            val color1 = LinkedList<Int>()
            colors.forEachIndexed() { v, color ->
                if (color == 0) {
                    color0.add(v)
                }
                else if (color == 1) {
                    color1.add(v)
                }
            }
            return Pair(true, Pair(color0, color1))
        }
    }
}
