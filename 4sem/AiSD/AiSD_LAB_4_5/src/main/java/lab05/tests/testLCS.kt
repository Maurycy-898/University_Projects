package lab05.tests

import lab05.utils.Stats

class TestLCS {
    var stats: Stats = Stats()

    fun findLengthOfLCS(X: Array<Int>, Y: Array<Int>): Int {
        val m = X.size; val n = Y.size
        val lcsArr = Array((m + 1)) { IntArray((n + 1)) }

        for (i in 0..m) {
            for (j in 0..n) {
                stats.cmp += 2
                if (i == 0 || j == 0) {
                    lcsArr[i][j] = 0
                    stats.mem ++
                }
                else if (X[i - 1] == Y[j - 1]) {
                    lcsArr[i][j] = lcsArr[i - 1][j - 1] + 1
                    stats.cmp ++
                    stats.mem ++
                }
                else {
                    lcsArr[i][j] = max(lcsArr[i - 1][j], lcsArr[i][j - 1])
                    stats.cmp ++
                    stats.mem ++
                }
                stats.cmp ++
            }
            stats.cmp ++
        }

        return lcsArr[m][n]
    }

    private fun max(a: Int, b: Int): Int {
        stats.cmp ++
        return if (a > b) a else b
    }
}
