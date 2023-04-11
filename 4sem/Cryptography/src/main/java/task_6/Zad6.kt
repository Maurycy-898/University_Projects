package task_6

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.encoders.Hex
import java.security.MessageDigest
import java.security.Security

private fun hashString(input: String, algorithm: String): String {
    return Hex.toHexString(MessageDigest.getInstance(algorithm).digest(input.toByteArray()))
}


fun main() {
    println("Enter the message: ")
    val message = readLine() ?: return
    Security.addProvider(BouncyCastleProvider())

    println(hashString(message, "MD4"))
    println(hashString(message, "MD5"))
    println(hashString(message, "SHA-1"))
    println(hashString(message, "SHA-256"))
    println(hashString(message, "SHA-512"))
}