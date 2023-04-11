package lab05.algorithms

import lab05.utils.setCharsetToUTF8
import kotlin.random.Random

// Max Heap priority queue + heap sort
class Heap(private val maxHeapSize: Int) {
    // array representing this heap
    private val heapArray: Array<Int?> = arrayOfNulls(maxHeapSize)
    private var heapSize: Int = 0 // current size of this heap

    // Companion object containing static methods related to heap-sort
    companion object Sort {
        @JvmStatic
        // to perform heapsort algorithm on an array
        fun heapSort(arr: IntArray) {
            buildHeap(arr)

            for (i in (arr.size - 1) downTo 1) {
                swap(0, i, arr)
                heapify(arr, 0, i)
            }
        }

        // to build heap structure from the array
        private fun buildHeap(arr: IntArray) {
            val arrLen = arr.size - 1
            for (i in (arrLen / 2) downTo 0)
                heapify(arr, arrLen, i)
        }

        // heapify function implementation
        private fun heapify(arr: IntArray, idx: Int, size: Int) {
            val left = leftChildIndex(idx)
            val right = rightChildIndex(idx)

            var largest = idx
            if (left < size && arr[left] > arr[largest])
                largest = left

            if (right < size && arr[right] > arr[largest])
                largest = right

            if (largest != idx) {
                swap(idx, largest, arr)
                heapify(arr, largest, size)
            }
        }

        // To get left child index from given heap-array
        private fun leftChildIndex(index: Int) = (2 * index) + 1

        // To get right child index from given heap-array
        private fun rightChildIndex(index: Int) = (2 * index) + 2

        // To get parent index from given heap-array
        private fun parentIndex(index: Int) = (index - 1) / 2

        // To swap values in given array
        private fun swap(idxA: Int, idxB: Int, arr: IntArray) {
            val tmp = arr[idxA]
            arr[idxA] = arr[idxB]
            arr[idxB] = tmp
        }
    }

    // To swap two values in this heap array
    private fun swap(idxA: Int, idxB: Int, arr: Array<Int?>) {
        val tmp = arr[idxA]
        arr[idxA] = arr[idxB]
        arr[idxB] = tmp
    }

    // Heapify function implementation
    private fun heapify(arr: Array<Int?>, idx: Int, size: Int) {
        val left = leftChildIndex(idx)
        val right = rightChildIndex(idx)

        var largest = idx
        if (left < size && arr[left]!! > arr[largest]!!)
            largest = left

        if (right < size && arr[right]!! > arr[largest]!!)
            largest = right

        if (largest != idx) {
            swap(idx, largest, arr)
            heapify(arr, largest, size)
        }
    }

    // Performs heap-sort on this heaps array copy and returns it
    fun getSortedHeap(): IntArray {
        val array = heapArray.copyOf(heapSize).requireNoNulls().toIntArray()
        heapSort(array)
        return array
    }

    // Inserts new element into this heap
    fun insert(key: Int): Boolean {
        if (heapSize == maxHeapSize) return false

        heapSize += 1
        var idx = heapSize - 1
        while (idx > 0 && heapArray[parentIndex(idx)]!! < key) {
            heapArray[idx] = heapArray[parentIndex(idx)]
            idx = parentIndex(idx)
        }
        heapArray[idx] = key
        return true
    }

    // Removes max element (root?) from this heap
    fun extractMax(): Int? {
        if (heapSize <= 0) return null

        val max = heapArray[0]
        heapArray[0] = heapArray[heapSize - 1]
        heapArray[heapSize - 1] = null;
        heapSize -= 1
        heapify(heapArray, idx = 0, heapSize)

        return max
    }

    // To display values stored in this heap in form of array
    private fun displayHeapArray() {
        for (i in 0 until heapSize)
            print("${heapArray[i]}, ")
        println()
    }

    // To display this heap values and (kinda abstract) structure
    private fun displayHeapStructure(idx: Int = 0, prefix: String = "", isRight: Boolean = false, hasLeft: Boolean = true) {
        if (idx < maxHeapSize && heapArray[idx] != null) {
            print(prefix)

            if (isRight && hasLeft) print("├──── ") else print("└──── ")

            println(heapArray[idx])

            val newHasLeft = if (leftChildIndex(idx) < maxHeapSize) {
                (heapArray[leftChildIndex(idx)] != null)
            } else false

            displayHeapStructure(
                rightChildIndex(idx), (prefix + (if (isRight && hasLeft) "│      " else "      ")),
                isRight = true, hasLeft = newHasLeft
            )

            displayHeapStructure(
                leftChildIndex(idx), (prefix + (if (isRight && hasLeft) "│      " else "      ")),
                isRight = false, hasLeft = newHasLeft
            )
        }
    }

    // To display both values and structure in a pleasing way
    fun displayAll() {
        println("\nHeap Array:")
        displayHeapArray()
        println("\nHeap Structure:")
        displayHeapStructure()
    }
}

// Test if works
fun main() {
    setCharsetToUTF8()
    val size = 20
    val heap = Heap(size)
    val arr = Array(size) { Random.nextInt(100) }
    arr.forEach { el -> println("Inserting $el node ...");
        heap.insert(el)
        heap.displayAll()
    }
    println()

    println("Sorted Heap Array")
    val sortedArr = heap.getSortedHeap()
    sortedArr.forEach { el -> print("${el}, ") }
    println()

    arr.forEach { _ ->
        println("Deleting ${heap.extractMax()} node ...")
        heap.displayAll()
    }
}
