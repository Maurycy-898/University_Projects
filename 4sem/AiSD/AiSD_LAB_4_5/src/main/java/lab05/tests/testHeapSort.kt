package lab05.tests

import lab05.utils.Stats

class TestHeapSort {
    var stats: Stats = Stats()

    fun heapSort(arr: IntArray) {
        buildHeap(arr)

        for (i in (arr.size - 1) downTo 1) {
            swap(0, i, arr)
            heapify(arr, 0, i)
            stats.cmp ++
        }
    }

    private fun buildHeap(arr: IntArray) {
        val arrLen = arr.size - 1
        stats.mem ++
        for (i in (arrLen / 2) downTo 0) {
            heapify(arr, arrLen, i)
            stats.cmp ++
        }
    }

    private fun heapify(arr: IntArray, idx: Int, size: Int) {
        val left = leftChildIndex(idx)
        val right = rightChildIndex(idx)
        stats.mem += 2

        var largest = idx
        stats.mem ++
        if (left < size && arr[left] > arr[largest]) {
            largest = left
            stats.mem ++
        }
        stats.cmp += 2

        if (right < size && arr[right] > arr[largest]) {
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

    private fun swap(idxA: Int, idxB: Int, arr: IntArray) {
        val tmp = arr[idxA]
        arr[idxA] = arr[idxB]
        arr[idxB] = tmp
        stats.mem += 3
    }
}
