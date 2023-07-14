package utils

import kotlin.random.Random


// generate hamming graph
object HammingGraphGenerator {
    // generates Hamming graph where: 0 < k < 17
    fun generateGraph(k: Int) : DirectedGraph {
        if ( (k < 1) || (k > 16) ) {
            throw Exception("Invalid parameter k=$k, k should be > 0 and < 17")
        }
        val nk = (1 shl k)
        val graph = DirectedGraph(size=nk)

        for (u in 0 until (nk-1)) for (v in (u + 1) until nk) {
            if (countDiffBits(u, v, k) == 1) {
                val hu = countBitwiseOnes(u, k)
                val hv = countBitwiseOnes(v, k)
                val cp = rollArcCapacity(u, v, k)

                if (hu > hv) {
                    graph.addArc(v, u, cp)
                } else if (hv > hu) {
                    graph.addArc(u, v, cp)
                } else { // if diffs == 1 then impossible
                    throw Exception("Unexpected behaviour")
                }
            }
        }
        return graph
    }

    // Hamming weight - number of ones in number bit representation ( H(num) )
    private fun countBitwiseOnes(num: Int, k: Int) : Int {
        var ones = 0
        var currNum = num
        repeat(k) {
            if ((currNum and 1) == 1) ones++
            currNum = currNum shr 1
        }
        return ones
    }

    // Number of zeros in number bit representation ( Z(num) )
    private fun countBitwiseZeros(num: Int, k: Int) : Int {
        var zeros = 0
        var currNum = num
        repeat(k) {
            if ((currNum and 1) == 0) zeros++
            currNum = currNum shr 1
        }
        return zeros
    }

    // Number of different bits in numbers bit representations
    private fun countDiffBits(num1: Int, num2: Int, k: Int) : Int {
        var diffs = 0
        var currNum1 = num1
        var currNum2 = num2
        repeat(k) {
            if ((currNum1 and 1) != (currNum2 and 1)) diffs++
            currNum1 = currNum1 shr 1
            currNum2 = currNum2 shr 1
        }
        return diffs
    }

    // Generate random arc capacity (uniform distribution)
    private fun rollArcCapacity(u: Int, v: Int, k: Int) : Int {
        val maxCapacity = 1 shl maxOf(
            countBitwiseOnes(u, k), countBitwiseZeros(u, k),
            countBitwiseOnes(v, k), countBitwiseZeros(v, k)
        )
        return (Random.nextInt(maxCapacity) + 1)
    }
}


// generate bipartite graph
object BipartiteGraphGenerator {
    fun generateGraph(k: Int, i: Int) : Triple<UndirectedGraph, IntArray, IntArray> {
        val size = (1 shl k) * 2
        val graph = UndirectedGraph(size)

        val nodes = IntArray(size) { it }
        nodes.shuffle()

        val half = size / 2
        val nodes1 = nodes.take(half).toIntArray()
        val nodes2 = nodes.takeLast(half).toIntArray()

        val idxs = IntArray(half) { it }
        for (node1 in nodes1) {
            idxs.shuffle()
            for (j in 0 until i) {
                graph.addEdge(node1, nodes2[idxs[j]])
            }
        }
        return Triple(graph, nodes1, nodes2)
    }
}


// show number as its Binary String
fun asBinary(num: Int, k: Int = 16) : String {
    var curr = num
    var res = ""
    repeat(k) {
        res += "${curr and 1}"
        curr = curr shr 1
    }
    return res.reversed()
}
