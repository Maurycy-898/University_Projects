package lab05.tests

import lab05.utils.Stats
import java.io.File
import kotlin.random.Random

fun randArr(n: Int): IntArray {
    return IntArray(n) { Random.nextInt((2 * n)) }
}

fun testHeapSort(from: Int, to: Int, inc: Int) {
    val statsFile = File("src\\main\\java\\lab05\\tests\\results\\csvs\\HeapSort_stats2.csv")

    var heapSort: TestHeapSort
    var hSortStats: Stats;
    var randArr: IntArray

    statsFile.appendText("N_SIZE;CMP;SWP;CMP/N;SWP/N;\n")

    for (i in from..to step inc) {
        hSortStats = Stats()
        for (j in 0..100) {
            heapSort = TestHeapSort()

            randArr = randArr(i)
            heapSort.heapSort(randArr)

            hSortStats.addStats(heapSort.stats)
        }

        hSortStats.divStats(100);
        statsFile.appendText("$i;${hSortStats.cmp};${hSortStats.mem};${hSortStats.cmp.toDouble() / i.toDouble()};${hSortStats.mem.toDouble() / i.toDouble()};\n")
    }
}

fun testHeapPQ(from: Int, to: Int, inc: Int) {
    val statsFile = File("src\\main\\java\\lab05\\tests\\results\\csvs\\HeapPQ_stats2.csv")

    var heapPQ: TestHeapPQ
    var insHpqStats: Stats;
    var delHpqStats: Stats;
    var keys: IntArray

    statsFile.appendText("N_SIZE;INS_CMP;INS_MEM;DEL_CMP;DEL_MEM;\n")

    for (i in from..to step inc) {
        insHpqStats = Stats()
        delHpqStats = Stats()

        for (j in 0..10) {
            heapPQ = TestHeapPQ(to + 10)

            keys = randArr(i)
            keys.forEach { key ->
                heapPQ.insert(key);
            }
            insHpqStats.addStats(heapPQ.stats)

            heapPQ.stats.resetStats()
            for (k in keys.indices) {
                heapPQ.popMax()
            }
            delHpqStats.addStats(heapPQ.stats)
        }

        insHpqStats.divStats(10);
        delHpqStats.divStats(10)
        statsFile.appendText("$i;${insHpqStats.cmp};${insHpqStats.mem};${delHpqStats.cmp};${delHpqStats.mem};\n")
    }
}

fun testLCS(from: Int, to: Int, inc: Int) {
    val statsFile = File("src\\main\\java\\lab05\\tests\\results\\csvs\\LCS_stats2.csv")

    var lcs: TestLCS
    var lcsStats: Stats;
    var sequence1: Array<Int>
    var sequence2: Array<Int>

    statsFile.appendText("N_SIZE;CMP;SWP;\n")

    for (i in from..to step inc) {
        lcsStats = Stats()
        for (j in 0..10) {
            lcs = TestLCS()

            sequence1 = Array(i) { Random.nextInt(100) }
            sequence2 = Array(i) { Random.nextInt(100) }

            lcs.findLengthOfLCS(sequence1, sequence2)

            lcsStats.addStats(lcs.stats)
        }

        lcsStats.divStats(10);
        statsFile.appendText("$i;${lcsStats.cmp};${lcsStats.mem};\n")
    }
}

fun main() {
    testHeapSort(10,10000,10)
    testHeapPQ(100,100000,100)
    testLCS(10, 10000, 10)
}
