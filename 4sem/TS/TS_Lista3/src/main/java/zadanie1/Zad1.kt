package zadanie1

import java.io.File

fun createTestFile() {

}

fun test() {
    File("src\\main\\java\\test_files\\decodedFile.txt").writeText("")

    val encoder = Encoder()
    val encodedData = encoder.encodeFile()
    File("src\\main\\java\\test_files\\encodedFile.txt").writeText(encodedData)

    val decoder = Decoder()
    val decodedData = decoder.decodeFile()
    decodedData.forEach { line ->
        File("src\\main\\java\\test_files\\decodedFile.txt").appendText(line + "\n")
    }
}

fun main() {
    createTestFile()
    test()
}