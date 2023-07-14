package utils

import java.io.File
import kotlin.collections.ArrayList


enum class GraphType { DIRECTED, UNDIRECTED }

/**
 * Arc represented by:
 * sc - arc's source,
 * tg - arc's target,
 * cp - arc's capacity
 */
data class Arc(var sc: Int, var tg: Int, var cp: Int) {
    fun toStringKey() : String {
        return "$sc $tg"
    }
    fun toReversedStringKey() : String {
        return "$tg $sc"
    }

    companion object {
        fun fromStringKey(key: String) : Arc {
            val data = key.split(" ")
            return Arc(data[0].toInt(), data[1].toInt(), data[2].toInt())
        }
    }
}


open class Graph<T>(size: Int) {
    val adj = Array<ArrayList<T>>(size) {
        ArrayList()
    }

    operator fun get(i: Int) : ArrayList<T> {
        return this.adj[i]
    }
}


class DirectedGraph(val size: Int) {
    /** This Graph's Edges */
    val adj = Array<ArrayList<Arc>>(size) {
        ArrayList()
    }

    operator fun get(i: Int) : ArrayList<Arc> {
        return this.adj[i]
    }

    operator fun get(i: Int, j: Int) : Arc {
        return this.adj[i].find {
            it.tg == j
        } ?: throw Exception("Invalid index")
    }

    fun hasArc(sc: Int, tg: Int) : Boolean {
        return this.adj[sc].find { it.tg == tg } != null
    }

    /** Add new Edge to the Graph */
    fun addArc(sc: Int, tg: Int, cp: Int) {
        if (sc > size || sc < 0 || tg > size || tg < 0) {
            throw Exception("No such vertices exist! : $sc, $tg")
        }
        if (this.hasArc(sc, tg)) {
            this[sc, tg].cp == cp
        } else {
          this[sc].add(Arc(sc, tg, cp))
        }
    }

    fun dumpToFile(filename: String) {
        val sb = StringBuilder()
        sb.append("SIZE: ${this.size}\n")
        this.adj.forEach {
            it.forEach { arc ->
                sb.append("${arc.sc} ${arc.tg} ${arc.cp}\n")
            }
        }
        val file = File("src\\main\\kotlin\\out_graphs\\$filename.txt")
        file.writeText(sb.toString())
    }

    companion object {
        fun graphFromFile(filename: String) : DirectedGraph {
            val file = File("src\\main\\kotlin\\out_graphs\\$filename.txt")
            val lines = file.readLines()
            val size = lines[0].split(" ")[1].toInt()
            val graph = DirectedGraph(size)

            lines.drop(1).forEach { line ->
                val lineData = line.split(" ")
                val sc = lineData[0].toInt()
                val tg = lineData[1].toInt()
                val cp = lineData[2].toInt()
                graph.addArc(sc, tg, cp)
            }
            return graph
        }
    }
}


class UndirectedGraph(val size: Int) {
    val adj = Array<ArrayList<Int>>(size) {
        ArrayList()
    }

    operator fun get(i: Int) : ArrayList<Int> {
        return this.adj[i]
    }

    operator fun get(i: Int, j: Int) : Int {
        val edge = this.adj[i].find { it == j }
        return if (edge == null) 0 else 1
    }

    fun addEdge(u: Int, v: Int) {
        if (!(v in adj[u])) adj[u].add(v)
        if (!(u in adj[v])) adj[v].add(u)
    }

    fun dumpToFile(filename: String) {
        val sb = StringBuilder()
        sb.append("SIZE: ${this.size}\n")
        this.adj.forEachIndexed { u, adjU ->
            adjU.forEach { v ->
                sb.append("$u $v\n")
            }
        }
        val file = File("src\\main\\kotlin\\out_graphs\\$filename.txt")
        file.writeText(sb.toString())
    }

    companion object {
        fun graphFromFile(filename: String) : UndirectedGraph {
            val file = File("src\\main\\kotlin\\out_graphs\\$filename.txt")
            val lines = file.readLines()
            val size = lines[0].split(" ")[1].toInt()
            val graph = UndirectedGraph(size)

            lines.drop(1).forEach { line ->
                val lineData = line.split(" ")
                val sc = lineData[0].toInt()
                val tg = lineData[1].toInt()
                graph.addEdge(sc, tg)
            }
            return graph
        }
    }
}
