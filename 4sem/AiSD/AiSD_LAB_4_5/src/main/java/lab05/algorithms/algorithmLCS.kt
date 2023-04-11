package lab05.algorithms

import kotlin.random.Random

// Longest Common Subsequence
class LCS {
    companion object {
        // To find length of the longest common subsequence
        fun findLengthOfLCS(X: Array<Int>, Y: Array<Int>): Pair<Int, Array<IntArray>> {
            val m = X.size; val n = Y.size
            val lcsArr = Array((m + 1)) { IntArray((n + 1)) }

            for (i in 0..m) {
                for (j in 0..n) {
                    if (i == 0 || j == 0) lcsArr[i][j] = 0
                    else if (X[i - 1] == Y[j - 1]) lcsArr[i][j] = lcsArr[i - 1][j - 1] + 1
                    else lcsArr[i][j] = max(lcsArr[i - 1][j], lcsArr[i][j - 1])
                }
            }

            return Pair(lcsArr[m][n], lcsArr)
        }

        // To get the longest common subsequence as array
        fun getLCS(X: Array<Int>, Y: Array<Int>, lcsArr: Array<IntArray>): IntArray {
            val m = X.size; val n = Y.size

            var index: Int = lcsArr[m][n]
            val lcs = IntArray(index)

            var i = m; var j = n
            while (i > 0 && j > 0) {
                if (X[i - 1] == Y[j - 1]) {
                    lcs[index - 1] = X[i - 1]
                    index--; i--; j--
                }
                else if (lcsArr[i - 1][j] > lcsArr[i][j - 1]) i--
                else j--
            }

            return lcs
        }

        // To find max from two given values
        private fun max(a: Int, b: Int): Int {
            return if (a > b) a else b
        }
    }
}

// To test if works
fun main() {
    val size1 = 10; val size2 = 15
    val sequence1 = Array(size1) { Random.nextInt(10) }
    val sequence2 = Array(size2) { Random.nextInt(10) }

    println("\nSequence 1:")
    sequence1.forEach { el -> print("$el, ") }
    println("\nSequence 2:")
    sequence2.forEach { el -> print("$el, ") }
    println()

    val result = LCS.findLengthOfLCS(sequence1, sequence2)
    println("The longest common subsequence: ")
    LCS.getLCS(sequence1, sequence2, result.second)
    println("The subsequence length = ${result.first}")
}
