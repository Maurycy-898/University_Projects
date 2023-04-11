package lab05.lab04.tests

import lab05.utils.Stats
import kotlin.math.max

class TestSPL {
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

            stats.mem += 3
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

        splay(node)
    }

    fun delete(key: Int) {
        val z = search(key) ?: return
        val y = if (z.left == null || z.right == null) { z } else { successor(z) } ?: return
        val x = if (y.left != null) { y.left } else { y.right }
        val parent = z.parent

        stats.mem += 5
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

        if (z.parent != null) { splay(parent) }

        stats.mem += 2
        stats.cmp += 4
    }

    private fun leftRotate(x: Node?) {
        val y = x!!.right
        x.right = y!!.left
        stats.mem += 2

        if (y.left != null) { y.left!!.parent = x }
        stats.mem += 2
        stats.cmp ++

        y.parent = x.parent
        stats.mem ++
        if (x.parent == null) {
            stats.mem ++
            stats.cmp ++
            this.root = y
        }
        else if (x === x.parent!!.left) {
            stats.mem ++
            stats.cmp ++
            x.parent!!.left = y
        }
        else {
            stats.mem ++
            stats.cmp ++
            x.parent!!.right = y
        }

        y.left = x; x.parent = y
        stats.mem += 2
    }

    private fun rightRotate(x: Node?) {
        val y = x!!.left
        x.left = y!!.right
        stats.mem += 2

        if (y.right != null) {
            stats.mem += 2
            stats.cmp ++
            y.right!!.parent = x
        }

        y.parent = x.parent
        stats.mem ++
        if (x.parent == null) {
            stats.mem ++
            stats.cmp ++
            this.root = y
        }
        else if (x === x.parent!!.right) {
            stats.mem ++
            stats.cmp ++
            x.parent!!.right = y
        }
        else {
            stats.mem ++
            stats.cmp ++
            x.parent!!.left = y
        }

        y.right = x; x.parent = y
        stats.mem += 2
    }

    private fun splay(x: Node?) {
        while (x!!.parent != null) {
            stats.mem +=2
            stats.cmp +=2

            if (x.parent!!.parent == null) {
                stats.cmp ++
                stats.mem ++
                if (x === x.parent!!.left) { rightRotate(x.parent) }
                else { leftRotate(x.parent) }
            }
            else if (x === x.parent!!.left && x.parent === x.parent!!.parent!!.left) {
                stats.cmp += 3
                stats.mem += 3
                rightRotate(x.parent!!.parent)
                rightRotate(x.parent)
            } else if (x === x.parent!!.right && x.parent === x.parent!!.parent!!.right) {
                stats.cmp += 5
                stats.mem += 5
                leftRotate(x.parent!!.parent)
                leftRotate(x.parent)
            } else if (x === x.parent!!.right && x.parent === x.parent!!.parent!!.left) {
                stats.cmp += 7
                stats.mem += 7
                leftRotate(x.parent)
                rightRotate(x.parent)
            } else {
                stats.cmp += 7
                stats.mem += 7
                rightRotate(x.parent)
                leftRotate(x.parent)
            }
        }
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
