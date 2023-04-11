package task_7

import org.bouncycastle.util.encoders.Hex
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.random.Random

val hexP = "CE369E8F9F2B0F43C0E837CCEC78439B97FF11D2E8DD3DDC57836F8DE11DF848D1CF99615C23BAA3BCF87D9D5DDDE981CFA885647780FEFA21CB07265561AF679BA170E9547E125ECC7B340DCAC3D9F6BF38AF243B01125D1CB0ADCDD80024A235CF25B8ABD5DAEC18AE0E063673DAE2DBFB416AF60E1233320490E1218DA5AD16C91527076E36A7DA9623715428F80010BB9F30477BFCC89F3183D343184A18E938CAB6EF364BE069FA7BE251AA267C6BFE62F247AC1A72BE7830EDB769E195E3CD6BB13DD684FE10DD9C042A465ADF46E0C5EF6458D0304DEE3437B940C904B235DB669A4013198A8184AE7F060F903EAFAB3150E24C011CBE57FAD7BAA1B62DEFB53B2DF0F51019DC339D2D25AA00F904E1AA17E100B"
val intP = BigInteger(hexP, 16)

private fun hashString(input: String): String {
    return Hex.toHexString(MessageDigest.getInstance("SHA-256").digest(input.toByteArray()))
}

class Alice(private val pass: String) {
    private val xa = Random.nextInt()
    private var xb: BigInteger = BigInteger("0")
    private var k: BigInteger = BigInteger("0")

    fun genXa(): BigInteger {
        return BigInteger(hashString(pass), 16).modPow(xa.toBigInteger(), intP)
    }

    fun getXb(xb: BigInteger) {
        this.xb = xb
    }

    fun computeK() {
        val z = xb.modPow(xa.toBigInteger(), intP).toString(16)
    }
}

fun main() {
    intP.toByteArray().forEachIndexed() { idx, b -> println("$b, $idx") }
}