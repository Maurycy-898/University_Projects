package lab05.utils

import java.io.PrintStream
import java.nio.charset.Charset

fun setCharsetToUTF8() {
    System.setOut(PrintStream(System.out, true, Charset.defaultCharset()))
}

class Stats {
    var mem:Long = 0
    var cmp: Long = 0

    fun addStats(stats: Stats) {
        mem += stats.mem
        cmp += stats.cmp
    }

    fun divStats(n: Int) {
        mem /= n
        cmp /= n
    }

    fun resetStats() {
        this.mem = 0
        this.cmp = 0
    }
}