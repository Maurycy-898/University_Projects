package lab04.algorithms

import lab05.utils.setCharsetToUTF8
import java.util.*
import kotlin.math.max
import kotlin.random.Random

// Splay Binary Search Tree
class TreeSPL {
    // Initialize root node
    private var root: Node? = null

    // To find sub-tree minimum element
    private fun minimum(node: Node): Node {
        var tmp: Node? = node
        while (tmp!!.left != null) {
            tmp = tmp.left
        }
        return tmp
    }

    // To find node successor (deletion process)
    private fun successor(node: Node): Node? {
        if (node.right != null) {
            return minimum(node.right!!)
        }

        var x = node; var y = x.parent
        while (y != null && x === y.right) {
            x = y; y = y.parent
        }
        return y
    }

    // To perform left rotation on given node
    private fun leftRotate(x: Node?) {
        val y = x!!.right
        x.right = y!!.left

        if (y.left != null) y.left!!.parent = x
        y.parent = x.parent
        if (x.parent == null) root = y
        else if (x === x.parent!!.left) x.parent!!.left = y
        else x.parent!!.right = y

        y.left = x; x.parent = y
    }

    // To perform right rotation on given node
    private fun rightRotate(x: Node?) {
        val y = x!!.left
        x.left = y!!.right

        if (y.right != null) y.right!!.parent = x
        y.parent = x.parent
        if (x.parent == null) root = y
        else if (x === x.parent!!.right) x.parent!!.right = y
        else x.parent!!.left = y

        y.right = x; x.parent = y
    }

    // Splay operation. It moves x to the root of the tree
    private fun splay(x: Node?) {
        while (x!!.parent != null) {
            if (x.parent!!.parent == null) {
                if (x === x.parent!!.left) { rightRotate(x.parent) }
                else { leftRotate(x.parent) }
            }
            else if (x === x.parent!!.left && x.parent === x.parent!!.parent!!.left) {
                rightRotate(x.parent!!.parent)
                rightRotate(x.parent)
            } else if (x === x.parent!!.right && x.parent === x.parent!!.parent!!.right) {
                leftRotate(x.parent!!.parent)
                leftRotate(x.parent)
            } else if (x === x.parent!!.right && x.parent === x.parent!!.parent!!.left) {
                leftRotate(x.parent)
                rightRotate(x.parent)
            } else {
                rightRotate(x.parent)
                leftRotate(x.parent)
            }
        }
    }

    // To search for element with given key
    private fun search(key: Int): Node? {
        var node = this.root
        while (node != null) {
            if (node.key == key) break
            node = if (node.key > key) node.left else node.right
        }
        return node
    }

    // To insert new element with given key to this tree
    fun insert(key: Int) {
        val node = Node(key)
        var x = root; var y: Node? = null
        while (x != null) {
            y = x
            x = if (node.key < x.key) x.left
            else x.right
        }

        node.parent = y
        if (y == null) root = node
        else if (node.key < y.key) y.left = node
        else y.right = node

        splay(node)
    }

    // To delete element with given key from this tree (if exist)
    fun delete(key: Int) {
        val z = search(key) ?: return
        val y = if (z.left == null || z.right == null) z
        else successor(z)!!
        val x = if (y.left != null) y.left else y.right
        val parent = z.parent

        if (x != null) x.parent = y.parent
        if (y.parent == null) root = x
        else if (y === y.parent!!.left) y.parent!!.left = x
        else y.parent!!.right = x
        if (y !== z) z.key = y.key

        // splay on deleted node parent
        if (z.parent != null) splay(parent)
    }

    // To calculate this tree height
    private fun calcHeight(node: Node?): Int {
        if (node == null) { return 0 }
        val leftHeight = calcHeight(node.left)
        val rightHeight = calcHeight(node.right)
        return max(leftHeight, rightHeight) + 1
    }
    fun calcHeight(): Int {
        return calcHeight(this.root)
    }

    // To print this BST Tree with its structure
    private fun printSPL(node: Node?, prefix: String = "", isRight: Boolean = false, hasLeft: Boolean = true) {
        if (node != null) {
            print(prefix)

            if (isRight && hasLeft) print("├──── ") else print("└──── ")

            println(node.key)

            printSPL(node.right, (prefix + (if (isRight && hasLeft) "│      " else "      ")), isRight = true, (node.left != null))
            printSPL(node.left, (prefix + (if (isRight && hasLeft) "│      " else "      ")), isRight = false, (node.left != null))
        }
    }
    fun printSPL() {
        printSPL(root)
        println()
    }

    // To print this BST Tree elements in order
    private fun printInOrder(node: Node?) {
        if (node != null) {
            printInOrder(node.left)
            print("${node.key} ")
            printInOrder(node.right)
        }
    }
    fun printInOrder() {
        printInOrder(this.root)
        println()
    }

    // The Node of Splay Binary Search Tree
    private class Node (
        var key: Int,
        var parent: Node? = null,
        var left: Node? = null,
        var right: Node? = null
    )
}

// To test if works
fun main() {
    setCharsetToUTF8()
    val spl = TreeSPL(); val size = 20
    val arr = Array(size) { el -> el }

    for (i in 0 until size) {
        println("Inserting ${arr[i]}...\n")
        spl.insert(arr[i])
        spl.printSPL()
    }

    arr.shuffle()
    for (i in 0 until size) {
        println("Deleting ${arr[i]}...\n")
        spl.delete(arr[i])
        spl.printSPL()
    }
}
