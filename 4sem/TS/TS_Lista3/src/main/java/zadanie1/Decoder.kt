package zadanie1

import java.io.File
import java.io.FileReader
import java.util.zip.CRC32

class Decoder(private val inFilePath: String = "src\\main\\java\\test_files\\encodedFile.txt") {
    private val CRC = CRC32()

    fun decodeFile(): ArrayList<String> {
        val output = ArrayList<String>()
        val input = FileReader(File(inFilePath))

        // should be only one line as this is how the encoder works
        val data = input.readLines()[0]

        dataToFrames(data).forEach { frame ->
            output.add(decodeFrame(frame))
        }

        return output
    }

    private fun decodeFrame(frame: String): String {
        val unPaddedFrame = bitUnPadding(frame)
        val oldCRC = unPaddedFrame.subSequence(unPaddedFrame.length - 32, unPaddedFrame.length).toString() // save old CRC
        val decodedFrame = unPaddedFrame.subSequence(0, unPaddedFrame.length - 32).toString() // delete old CRC

        CRC.reset()
        CRC.update(decodedFrame.toByteArray())

        return if (oldCRC == CRC.value.toInt().to32bitString()) decodedFrame else ""
    }

    private fun bitUnPadding(input: String): String {
        var onesCounter = 0
        val output = StringBuilder()

        for (i in input.indices) {
            if (input.substring(i, i + 1) == "1") {
                output.append("1")
                onesCounter++
            }
            else {
                if (onesCounter < 5) {
                    output.append("0")
                }
                onesCounter = 0
            }
        }

        return output.toString()
    }

    private fun dataToFrames(data: String) : ArrayList<String> {
        val frames = ArrayList<String>()
        val frame = StringBuilder()
        var onesCounter = 0

        var i = 0
        while (i < data.length) {
            if (data.substring(i, i + 1) == "1") {
                onesCounter++
            }
            else {
                onesCounter = 0
            }

            // start reading frame when TERMINATE sequence was reached
            if (onesCounter == 6) {
                onesCounter = 0

                i += 2 // skip 0 after TERMINATE sequence
                while (onesCounter < 6) {
                    if (data.substring(i, i + 1) == "1") {
                        frame.append("1")
                        onesCounter++
                    }
                    else {
                        frame.append("0")
                        onesCounter = 0
                    }
                    i++
                }
                // add frame with removed end terminator
                frames.add(frame.toString().replace("0111111", ""))
                frame.clear()
            }

            i++
        }

        return frames
    }

    private fun Int.to32bitString(): String {
        return Integer.toBinaryString(this).padStart(Int.SIZE_BITS, '0')
    }
}