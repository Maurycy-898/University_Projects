package zadanie1

import java.io.File
import java.lang.StringBuilder
import java.util.zip.CRC32


class Encoder(private val inFilePath: String = "src\\main\\java\\test_files\\inFile.txt") {
    private val TERMINATOR = "01111110"
    private val CRC = CRC32()

    fun encodeFile(): String {
        val output = StringBuilder()

        File(inFilePath).forEachLine { frame ->
            output.append(encodeFrame(frame))
        }

        return output.toString()
    }

    private fun encodeFrame(frame: String): String {
        val lineWithCRC = StringBuilder()

        CRC.reset()
        CRC.update(frame.toByteArray())

        lineWithCRC.append(frame)
        lineWithCRC.append(CRC.value.toInt().to32bitString())

        return bitPadding(lineWithCRC.toString())
    }

    private fun bitPadding(input: String): String {
        var onesCounter = 0
        val output = StringBuilder()

        output.append(TERMINATOR)
        for (i in input.indices) {
            if (onesCounter == 5) {
                output.append("0")
                onesCounter = 0
            }
            if (input.substring(i, i + 1) == "1") {
                output.append("1")
                onesCounter++
            }
            else {
                output.append("0")
                onesCounter = 0
            }
        }
        output.append(TERMINATOR)

        return output.toString()
    }

    private fun Int.to32bitString(): String {
        return Integer.toBinaryString(this).padStart(Int.SIZE_BITS, '0')
    }
}