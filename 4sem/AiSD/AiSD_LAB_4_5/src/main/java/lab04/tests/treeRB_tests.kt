package lab04.tests

import lab04.utils.*
import lab05.utils.Stats
import kotlin.math.max

class TestRB {
    private val nil: NodeRB = NodeRB()
    private var root: NodeRB?
    var stats: Stats = Stats()

    init { nil.color = BLACK; root = nil }

    private fun insertFixup(node: NodeRB) {
        var x: NodeRB? = node; var y: NodeRB?
        stats.mem += 2

        while (x !== root && x!!.parent!!.color == RED) {
            stats.cmp += 2
            stats.mem ++
            if (x.parent === x.parent!!.parent!!.right) {
                y = x.parent!!.parent!!.left
                stats.mem += 2
                stats.cmp ++

                if (y!!.color == RED) {
                    stats.cmp ++
                    stats.mem += 4

                    y.color = BLACK
                    x.parent!!.color = BLACK
                    x.parent!!.parent!!.color = RED
                    x = x.parent!!.parent
                } else {
                    stats.cmp += 2
                    if (x === x.parent!!.left) {
                        stats.mem ++
                        x = x.parent
                        rightRotate(x)
                    }
                    stats.mem += 2
                    x!!.parent!!.color = BLACK
                    x.parent!!.parent!!.color = RED
                    leftRotate(x.parent!!.parent)
                }
            }
            else {
                y = x.parent!!.parent!!.right
                stats.mem ++
                if (y!!.color == RED) {
                    stats.cmp ++
                    stats.mem += 4

                    y.color = BLACK
                    x.parent!!.color = BLACK
                    x.parent!!.parent!!.color = RED
                    x = x.parent!!.parent
                } else {
                    stats.cmp += 2
                    if (x === x.parent!!.right) {
                        stats.mem ++
                        x = x.parent
                        leftRotate(x)
                    }

                    stats.mem += 2
                    x!!.parent!!.color = BLACK
                    x.parent!!.parent!!.color = RED
                    rightRotate(x.parent!!.parent)
                }
            }
        }
        stats.mem ++
        root!!.color = BLACK
    }

    private fun deleteFixup(node: NodeRB?) {
        var x = node; var y: NodeRB?
        stats.mem += 2
        while (x !== root && x!!.color == BLACK) {
            stats.cmp += 2
            if (x === x.parent!!.left) {
                y = x.parent!!.right
                stats.cmp += 2
                stats.mem ++
                if (y!!.color == RED) {
                    stats.cmp ++
                    stats.mem += 3
                    y.color = BLACK
                    x.parent!!.color = RED
                    leftRotate(x.parent)
                    y = x.parent!!.right
                }
                if (y!!.left!!.color == BLACK && y.right!!.color == BLACK) {
                    stats.cmp += 2
                    stats.mem += 2
                    y.color = RED
                    x = x.parent
                } else {
                    stats.cmp += 3
                    if (y.right!!.color == BLACK) {
                        stats.mem += 3
                        y.left!!.color = BLACK
                        y.color = RED
                        rightRotate(y)
                        y = x.parent!!.right
                    }
                    stats.mem += 4
                    y!!.color = x.parent!!.color
                    x.parent!!.color = BLACK
                    y.right!!.color = BLACK

                    leftRotate(x.parent)
                    x = root
                }
            }
            else {
                y = x.parent!!.left
                stats.mem ++
                if (y!!.color == RED) {
                    stats.cmp ++
                    stats.mem += 3
                    y.color = BLACK
                    x.parent!!.color = RED
                    rightRotate(x.parent)
                    y = x.parent!!.left
                }
                if (y!!.right!!.color == BLACK && y.left!!.color == BLACK) {
                    stats.cmp += 2
                    stats.mem += 2
                    y.color = RED
                    x = x.parent
                } else {
                    stats.cmp += 3
                    if (y.left!!.color == BLACK) {
                        stats.mem += 3
                        y.right!!.color = BLACK
                        y.color = RED
                        leftRotate(y)
                        y = x.parent!!.left
                    }
                    stats.mem += 4
                    y!!.color = x.parent!!.color
                    x.parent!!.color = BLACK
                    y.left!!.color = BLACK
                    rightRotate(x.parent)
                    x = root
                }
            }
        }
        x!!.color = BLACK
        stats.mem ++
    }

    private fun leftRotate(x: NodeRB?) {
        val y = x!!.right
        x.right = y!!.left
        stats.mem += 2

        if (y.left !== nil) { stats.mem ++; y.left!!.parent = x }
        stats.cmp ++

        y.parent = x.parent
        if (x.parent == null) { this.root = y }
        else if (x === x.parent!!.left) { stats.cmp ++; x.parent!!.left = y }
        else { stats.cmp ++; x.parent!!.right = y }

        y.left = x; x.parent = y
        stats.mem += 4
        stats.cmp ++
    }

    private fun rightRotate(x: NodeRB?) {
        val y = x!!.left
        x.left = y!!.right
        stats.mem += 2

        if (y.right !== nil) { stats.mem ++; y.right!!.parent = x }
        stats.cmp ++

        y.parent = x.parent
        if (x.parent == null) { this.root = y }
        else if (x === x.parent!!.right) { stats.cmp ++; x.parent!!.right = y }
        else { stats.cmp ++; x.parent!!.left = y }

        y.right = x; x.parent = y
        stats.mem += 4
        stats.cmp ++
    }

    private fun transplant(x: NodeRB?, y: NodeRB?) {
        if (x!!.parent == null) {
            stats.cmp ++
            stats.mem ++
            this.root = y
        }
        else if (x === x.parent!!.left) {
            stats.cmp += 2
            stats.mem ++
            x.parent!!.left = y
        }
        else {
            stats.cmp += 2
            stats.mem ++
            x.parent!!.right = y
        }
        stats.mem ++
        y!!.parent = x.parent
    }

    private fun minimum(node: NodeRB?): NodeRB? {
        var tmp = node
        stats.mem ++
        while (tmp!!.left !== nil) {
            stats.mem ++
            stats.cmp ++
            tmp = tmp!!.left
        }
        return tmp
    }

    fun insert(key: Int) {
        val z = NodeRB(key = key, left = nil, right = nil)
        var x = this.root; var y: NodeRB? = null
        stats.mem += 3
        while (x !== nil) {
            y = x
            x = if (z.key < x!!.key) { x.left }
            else { x.right }
            stats.mem += 2
            stats.cmp += 2
        }

        z.parent = y
        if (y == null) { root = z }
        else if (z.key < y.key) { y.left = z }
        else { y.right = z }

        stats.mem += 2
        stats.cmp ++

        if (z.parent == null) {
            stats.cmp ++
            stats.mem ++
            z.color = BLACK
            return
        }
        if (z.parent!!.parent == null) {
            stats.cmp ++
            return
        }

        insertFixup(z)
    }

    fun delete(key: Int) {
        val x: NodeRB?; var y: NodeRB?; var z = this.root
        stats.mem += 3
        while (z !== nil) {
            stats.cmp += 2
            stats.mem ++
            if (z!!.key == key) { break }
            stats.cmp ++
            stats.mem ++
            z = if (z.key > key) { z.left } else { z.right }
        }
        stats.cmp ++
        if (z == null || z == nil) { return }

        y = z; var yOriginalColor = y.color
        stats.mem += 2
        if (z.left === nil) {
            transplant(z, z.right)
            x = z.right
            stats.mem ++
            stats.cmp ++
        }
        else if (z.right === nil) {
            transplant(z, z.left)
            x = z.left
            stats.mem ++
            stats.cmp += 2
        }
        else {
            stats.cmp += 2
            y = minimum(z.right)
            yOriginalColor = y!!.color
            x = y.right
            stats.mem += 2
            if (y.parent === z) {
                stats.mem ++
                x!!.parent = y
            } else {
                transplant(y, y.right)
                y.right = z.right
                y.right!!.parent = y
                stats.mem += 2
            }
            stats.cmp ++
            stats.mem += 3
            transplant(z, y)
            y.left = z.left
            y.left!!.parent = y
            y.color = z.color
        }
        stats.cmp ++
        if (yOriginalColor == BLACK) {
            deleteFixup(x)
        }
    }

    private fun calcHeight(node: NodeRB?): Int {
        if (node == null || node == nil) { return 0 }
        val leftHeight = calcHeight(node.left)
        val rightHeight = calcHeight(node.right)
        return max(leftHeight, rightHeight) + 1
    }
    fun calcHeight(): Int {
        return calcHeight(this.root)
    }

    private class NodeRB (
        var key: Int = 0,
        var parent: NodeRB? = null,
        var left: NodeRB? = null,
        var right: NodeRB? = null,
        var color: Int = RED
    )
}
