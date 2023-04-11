package lab04.algorithms

import lab04.utils.*
import lab05.utils.setCharsetToUTF8
import kotlin.math.max
import kotlin.random.Random

// Red-Black Binary Search Tree
class TreeRB {
    // Initialize nil and root nodes
    private val nil = Node(color = BLACK)
    private var root: Node? = nil

    // Balance the tree after node insertion
    private fun insertFixup(node: Node) {
        var x: Node? = node; var y: Node?
        while (x !== root && x!!.parent!!.color == RED) {
            if (x.parent === x.parent!!.parent!!.right) {
                y = x.parent!!.parent!!.left
                if (y!!.color == RED) {
                    y.color = BLACK
                    x.parent!!.color = BLACK
                    x.parent!!.parent!!.color = RED
                    x = x.parent!!.parent
                }
                else {
                    if (x === x.parent!!.left) {
                        x = x.parent
                        rightRotate(x)
                    }
                    x!!.parent!!.color = BLACK
                    x.parent!!.parent!!.color = RED
                    leftRotate(x.parent!!.parent)
                }
            }
            else {
                y = x.parent!!.parent!!.right
                if (y!!.color == RED) {
                    y.color = BLACK
                    x.parent!!.color = BLACK
                    x.parent!!.parent!!.color = RED
                    x = x.parent!!.parent
                }
                else {
                    if (x === x.parent!!.right) {
                        x = x.parent
                        leftRotate(x)
                    }
                    x!!.parent!!.color = BLACK
                    x.parent!!.parent!!.color = RED
                    rightRotate(x.parent!!.parent)
                }
            }
        }
        root!!.color = BLACK
    }

    // Balance the tree after node deletion
    private fun deleteFixup(node: Node?) {
        var x = node; var y: Node?
        while (x !== root && x!!.color == BLACK) {
            if (x === x.parent!!.left) {
                y = x.parent!!.right
                if (y!!.color == RED) {
                    y.color = BLACK
                    x.parent!!.color = RED
                    leftRotate(x.parent)
                    y = x.parent!!.right
                }
                if (y!!.left!!.color == BLACK && y.right!!.color == BLACK) {
                    y.color = RED
                    x = x.parent
                }
                else {
                    if (y.right!!.color == BLACK) {
                        y.left!!.color = BLACK
                        y.color = RED
                        rightRotate(y)
                        y = x.parent!!.right
                    }
                    y!!.color = x.parent!!.color
                    x.parent!!.color = BLACK
                    y.right!!.color = BLACK

                    leftRotate(x.parent)
                    x = root
                }
            }
            else {
                y = x.parent!!.left
                if (y!!.color == RED) {
                    y.color = BLACK
                    x.parent!!.color = RED
                    rightRotate(x.parent)
                    y = x.parent!!.left
                }
                if (y!!.right!!.color == BLACK && y.left!!.color == BLACK) {
                    y.color = RED
                    x = x.parent
                }
                else {
                    if (y.left!!.color == BLACK) {
                        y.right!!.color = BLACK
                        y.color = RED
                        leftRotate(y)
                        y = x.parent!!.left
                    }
                    y!!.color = x.parent!!.color
                    x.parent!!.color = BLACK
                    y.left!!.color = BLACK
                    rightRotate(x.parent)
                    x = root
                }
            }
        }
        x!!.color = BLACK
    }

    // To perform left rotation on given node
    private fun leftRotate(x: Node?) {
        val y = x!!.right
        x.right = y!!.left

        if (y.left !== nil) y.left!!.parent = x
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

        if (y.right !== nil) y.right!!.parent = x
        y.parent = x.parent
        if (x.parent == null) root = y
        else if (x === x.parent!!.right) x.parent!!.right = y
        else x.parent!!.left = y

        y.right = x; x.parent = y
    }

    // To find node successor (deletion process)
    private fun successor(node: Node): Node? {
        if (node.right !== nil) {
            return minimum(node.right!!)
        }

        var x = node; var y = x.parent
        while (y !== nil && x === y?.right) {
            x = y; y = y.parent
        }
        return y
    }

    // To find sub-tree minimum element
    private fun minimum(node: Node?): Node? {
        var tmp = node
        while (tmp!!.left !== nil) {
            tmp = tmp!!.left
        }
        return tmp
    }

    // To search for element with given key
    private fun search(key: Int): Node? {
        var node = root
        while (node !== nil) {
            if (node!!.key == key) return node
            node = if (node.key > key) node.left else node.right
        }
        return null
    }

    // To insert an element with given key
    fun insert(key: Int) {
        val z = Node(key = key, left = nil, right = nil)
        var x = root; var y: Node? = null
        while (x !== nil) {
            y = x
            x = if (z.key < x!!.key) x.left
            else x.right
        }

        z.parent = y
        if (y == null) {
            z.color = BLACK
            root = z
            return
        }
        else if (z.key < y.key) y.left = z
        else y.right = z

        if (y.parent == null) return
        else insertFixup(z)
    }

    // To delete element with given key from this tree (if exist)
    fun delete(key: Int) {
        val z = search(key) ?: return
        val y = if (z.left === nil || z.right === nil) z
        else successor(z)!!
        val x = if (y.left !== nil) y.left else y.right
        x!!.parent = y.parent

        if (y.parent == null) root = x
        else if (y === y.parent!!.left) y.parent!!.left = x
        else y.parent!!.right = x

        if (y !== z) z.key = y.key
        if (y.color == BLACK) deleteFixup(x)

        // if x was nil reset nils parent
        nil.parent = null
    }

    // To calculate this tree height
    private fun calcHeight(node: Node?): Int {
        if (node == null || node == nil) { return 0 }
        val leftHeight = calcHeight(node.left)
        val rightHeight = calcHeight(node.right)
        return max(leftHeight, rightHeight) + 1
    }
    fun calcHeight(): Int {
        return calcHeight(root)
    }

    // To print this ree elements with its structure
    private fun printRB(node: Node?, withNil: Boolean, prefix: String = "", isRight: Boolean = false) {
        if (!withNil && node === nil) { return }
        if (node != null) {
            print(prefix)

            if (withNil) {
                if (isRight) print("├──── ") else print("└──── ")
            }
            else {
                if (isRight && node.parent?.left !== nil) print("├──── ") else print("└──── ")
            }

            if (node.color == RED) print(RED_COLOR) else print(BLACK_COLOR)
            if (node !== nil) print("${node.key}") else print("nil")
            print("$RESET_COLOR\n")

            printRB(node.right, withNil, (prefix + (if (isRight) "│      " else "      ")), isRight = true)
            printRB(node.left, withNil, (prefix + (if (isRight) "│      " else "      ")), isRight = false)
        }
    }
    fun printRB(withNil:Boolean = false) {
        printRB(this.root, withNil)
        println()
    }

    // To print this tree elements in order
    private fun printInOrder(node: Node?) {
        if (node !== nil) {
            printInOrder(node!!.left)
            print("${node.key} ")
            printInOrder(node.right)
        }
    }
    fun printInOrder() {
        printInOrder(root)
        println()
    }

    // The Node of Red-Black Binary Search Tree
    private class Node (
        var key: Int = 0,
        var parent: Node? = null,
        var left: Node? = null,
        var right: Node? = null,
        var color: Int = RED
    )
}

// To test if works
fun main() {
    setCharsetToUTF8()
    val rb = TreeRB(); val size = 20
    val arr = Array(size){ Random.nextInt(100) }

    for (i in 0 until size) {
        println("Inserting ${arr[i]}...\n")
        rb.insert(arr[i])
        rb.printRB()
    }

    arr.shuffle()
    for (i in 0 until size) {
        println("Deleting ${arr[i]}...\n")
        rb.delete(arr[i])
        rb.printRB()
    }
}
