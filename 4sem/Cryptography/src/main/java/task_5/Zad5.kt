package task_5

import java.io.File
import java.math.BigInteger
import java.util.*

object RSA {
    fun encrypt(
        m: BigInteger,
        N: BigInteger,
        e: BigInteger
    ): BigInteger = m.modPow(e, N)

    fun decryptWithoutCRT(
        c: BigInteger,
        N: BigInteger,
        d: BigInteger
    ): BigInteger = c.modPow(d, N)

    fun decryptWithCRT(
        c: BigInteger,
        p: BigInteger,
        q: BigInteger,
        dp: BigInteger,
        dq: BigInteger,
        qi: BigInteger
    ): BigInteger {
        val x1 = c.modPow(dp, p)
        val x2 = c.modPow(dq, q)
        return x2 + q * (qi * (x1 - x2)).mod(p)
    }
}


fun generateRandomBigInt(min: BigInteger, max: BigInteger): BigInteger {
    val rand = Random()
    var randomNumber: BigInteger
    do {
        randomNumber = min + BigInteger(max.bitLength(), rand)
    } while (randomNumber >= max)

    return randomNumber
}


fun generatePair(bitLength: Int): Pair<BigInteger, BigInteger> {
    val num65537 = BigInteger("65537")
    val one = BigInteger("1")
    val rnd = Random()

    var p: BigInteger; var q: BigInteger

    while (true) {
        p = BigInteger.probablePrime(bitLength, rnd)
        // check if coprime with 65537
        if ((p - one).gcd(num65537) == one)
            break
    }

    while (true) {
        q = BigInteger.probablePrime(bitLength, rnd)
        // check if coprime with 65537
        if (((q - one).gcd(num65537) == one) && (p != q))
            break
    }

    return Pair(p, q)
}


fun testAvgTime(bitLength: Int, iterations: Int) {
    val num65537 = BigInteger("65537")
    val two = BigInteger("2")
    val one = BigInteger("1")

    var totalTimeWithCRT: Long = 0
    var totalTimeWithoutCRT: Long = 0
    var startTime: Long

    for (i in 0 until iterations) {
        val pairPQ = generatePair(bitLength)

        val p = pairPQ.first
        val q = pairPQ.second

        val phi = (p - one) * (q - one)
        val n = p * q
        val d = num65537.modPow((-one), phi)
        val dp = d.mod((p - one))
        val dq = d.mod((q - one))
        val qi = q.modPow((-one), p)

        // input = random in {2, ..., N - 1}
        val c = generateRandomBigInt(two, (n - one))

        startTime = System.nanoTime()
        RSA.decryptWithCRT(c, p, q, dp, dq, qi)
        totalTimeWithCRT += System.nanoTime() - startTime

        startTime = System.nanoTime()
        RSA.decryptWithoutCRT(c, n, d)
        totalTimeWithoutCRT += System.nanoTime() - startTime
    }

    // Times are in nanoseconds
    val avgTimeWithCRT = totalTimeWithCRT / iterations
    val avgTimeWithoutCRT = totalTimeWithoutCRT / iterations

    File("src\\task_5.main\\java\\task_5\\times_CRT.txt").appendText(
        text = "Bit-length: $bitLength ,average time: $avgTimeWithCRT ns\n"
    )
    File("src\\task_5.main\\java\\task_5\\times_NO_CRT.txt").appendText(
        text = "Bit-length: $bitLength ,average time: $avgTimeWithoutCRT ns\n"
    )
}


fun main() {
    val bitLengths = arrayOf(128, 256, 512, 1024, 1536, 2048, 3072, 4096)
    val iterations = 1000

    bitLengths.forEach { bitLength ->
        testAvgTime(bitLength, iterations)
    }
}
