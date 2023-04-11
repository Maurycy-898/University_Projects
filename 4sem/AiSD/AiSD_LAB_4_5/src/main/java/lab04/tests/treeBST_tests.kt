package lab05.lab04.tests

import lab05.utils.Stats
import kotlin.math.max

class TestBST {
    private var root: Node? = null
    var stats: Stats = Stats()

    private fun minimum(node: Node): Node {
        var tmp: Node? = node
        while (tmp!!.left != null) {
            tmp = tmp.left

            stats.mem += 2
            stats.cmp++
        }
        return tmp
    }

    private fun successor(node: Node): Node? {
        if (node.right != null) {
            stats.mem++
            stats.cmp++
            return minimum(node.right!!)
        }

        var x = node; var y = x.parent
        stats.mem += 2
        while (y != null && x === y.right) {
            x = y; y = y.parent
            stats.mem += 2
            stats.cmp += 2
        }
        return y
    }

    private fun search(key: Int): Node? {
        var node = this.root
        stats.mem ++
        stats.cmp ++
        while (node != null) {
            stats.cmp += 2
            stats.mem += 2
            if (node.key == key) { break }
            stats.cmp ++
            stats.mem ++
            node = if (node.key > key)  { node.left } else { node.right }
        }
        return node
    }

    fun insert(key: Int) {
        val node = Node(key)
        var x = this.root; var y: Node? = null
        stats.mem += 3
        while (x != null) {
            y = x
            x = if (node.key < x.key) { x.left }
            else { x.right }

            stats.mem += 2
            stats.cmp += 2
        }

        node.parent = y
        if (y == null) { root = node }
        else if (node.key < y.key) {
            stats.cmp++
            y.left = node
        }
        else { y.right = node }

        stats.mem += 2
        stats.cmp ++
    }

    fun delete(key: Int) {
        val z = search(key) ?: return
        val y = if (z.left == null || z.right == null) { z } else { successor(z) } ?: return
        val x = if (y.left != null) { y.left } else { y.right }

        stats.mem += 4
        stats.cmp += 2

        if (x != null) { x.parent = y.parent }
        if (y.parent == null) { this.root = x }
        else if (y === y.parent?.left) {
            stats.cmp ++
            y.parent?.left = x
        }
        else { y.parent?.right = x }
        if (y !== z) {
            stats.mem ++
            z.key = y.key
        }

        stats.mem += 2
        stats.cmp += 3
    }

    private fun calcHeight(node: Node?): Int {
        if (node == null) { return 0 }
        val leftHeight = calcHeight(node.left)
        val rightHeight = calcHeight(node.right)
        return max(leftHeight, rightHeight) + 1
    }
    fun calcHeight(): Int {
        return calcHeight(this.root)
    }

    private class Node (
        var key: Int,
        var parent: Node? = null,
        var left: Node? = null,
        var right: Node? = null
    )
}
