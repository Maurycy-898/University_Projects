package lab05.tests

import lab05.utils.Stats

class TestHeapPQ(private val maxHeapSize: Int) {
    private val heapArray: Array<Int?> = arrayOfNulls(maxHeapSize)
    private var heapSize = 0

    var stats: Stats = Stats()

    fun insert(key: Int): Boolean {
        stats.cmp ++
        if (heapSize == maxHeapSize)
            return false

        heapSize += 1
        var idx = heapSize - 1
        stats.mem ++
        stats.cmp += 2
        while (idx > 0 && heapArray[parentIndex(idx)]!! < key) {
            heapArray[idx] = heapArray[parentIndex(idx)]
            idx = parentIndex(idx)
            stats.mem += 2
            stats.cmp += 2
        }
        heapArray[idx] = key
        stats.mem ++
        return true
    }

    fun popMax(): Int? {
        stats.cmp ++
        if (heapSize <= 0) return null

        val max = heapArray[0]
        heapArray[0] = heapArray[heapSize - 1]
        heapArray[heapSize - 1] = null; heapSize -= 1
        stats.mem += 3

        heapify(heapArray, idx = 0, heapSize)

        return max
    }

    private fun heapify(arr: Array<Int?>, idx: Int, size: Int) {
        val left = leftChildIndex(idx)
        val right = rightChildIndex(idx)
        stats.mem += 2

        var largest = idx
        stats.mem ++
        if (left < size && arr[left]!! > arr[largest]!!) {
            largest = left
            stats.mem ++
        }
        stats.cmp += 2

        if (right < size && arr[right]!! > arr[largest]!!) {
            largest = right
            stats.mem ++
        }
        stats.cmp += 2

        if (largest != idx) {
            swap(idx, largest, arr)
            heapify(arr, largest, size)
        }
        stats.cmp ++
    }

    private fun leftChildIndex(index: Int) = (2 * index) + 1

    private fun rightChildIndex(index: Int) = (2 * index) + 2

    private fun parentIndex(index: Int) = (index - 1) / 2

    private fun swap(idxA: Int, idxB: Int, arr: Array<Int?>) {
        val tmp = arr[idxA]
        arr[idxA] = arr[idxB]
        arr[idxB] = tmp
        stats.mem += 3
    }
}
