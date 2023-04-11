package lab4

import java.io.File
import kotlin.math.log2
import kotlin.math.min
import kotlin.math.max

class Pixel(var r: Int = 0, var g: Int = 0, var b: Int = 0) {
    fun addPixel(pixel: Pixel): Pixel = Pixel((r + pixel.r), (g + pixel.g), (b + pixel.b))
    fun subPixel(pixel: Pixel): Pixel = Pixel((r - pixel.r), (g - pixel.g), (b - pixel.b))
    fun modPixel(num: Int): Pixel = Pixel(r.mod(num), g.mod(num), b.mod(num))
    fun divPixel(num: Int): Pixel = Pixel((r / num), (g / num), (b / num))
}

fun jpegLS(imagePixels: ArrayList<ArrayList<Pixel>>, schemeFun: (Pixel, Pixel, Pixel) -> Pixel): ArrayList<ArrayList<Pixel>> {
    val result = ArrayList<ArrayList<Pixel>>()
    var encodedRow: ArrayList<Pixel>

    var n: Pixel; var w: Pixel; var nw: Pixel

    for ((rowIdx, row) in imagePixels.withIndex()) {
        encodedRow = ArrayList()
        for((colIdx, pixel) in row.withIndex()) {
            n = if (rowIdx == 0) Pixel() else imagePixels[rowIdx - 1][colIdx]
            w = if (colIdx == 0) Pixel() else imagePixels[rowIdx][colIdx - 1]
            nw = if(colIdx == 0 || rowIdx == 0) Pixel() else imagePixels[rowIdx - 1][colIdx - 1]

            encodedRow.add(pixel.subPixel(schemeFun(n, w, nw)).modPixel(256))
        }
        result.add(encodedRow)
    }

    return result
}

fun calcEntropy(imagePixels: ArrayList<ArrayList<Pixel>>, color: String): Double {
    val freqTable = IntArray(256); var length = 0
    for(row in imagePixels) {
        for (pixel in row) {
            when(color) {
                "r" -> { freqTable[pixel.r]++; length++ }
                "g" -> { freqTable[pixel.g]++; length++ }
                "b" -> { freqTable[pixel.b]++; length++ }

                else -> {
                    freqTable[pixel.r]++
                    freqTable[pixel.g]++
                    freqTable[pixel.b]++
                    length += 3
                }
            }
        }
    }

    var entropy = 0.0; var byteProb: Double
    for (freq in freqTable) {
        if (freq != 0) {
            byteProb = (freq.toDouble() / length.toDouble())
            entropy +=  byteProb * -log2(byteProb)
        }
    }

    return entropy
}

fun toPosInt(byte: Byte): Int {
    return if (byte.toInt() >= 0) { byte.toInt() } else { (byte.toInt() + 256) }
}

fun tgaToImagePixels(fileName: String): ArrayList<ArrayList<Pixel>> {
    val tgaBytes = File(fileName).readBytes()

    val width = (toPosInt(tgaBytes[13]) * 256) + toPosInt(tgaBytes[12])
    val height = (toPosInt(tgaBytes[15]) * 256) + toPosInt(tgaBytes[14])

    val noHeader = tgaBytes.slice(18..(tgaBytes.size - 1))
    val pixelsAmount = width * height * 3

    val imageTable = ArrayList<ArrayList<Pixel>>()
    var row = ArrayList<Pixel>()

    var i = 0
    while (i < pixelsAmount) {
        row.add(Pixel(toPosInt(noHeader[i + 2]), toPosInt(noHeader[i + 1]), toPosInt(noHeader[i])))

        if (row.size == width) {
            imageTable.add(0, row)
            row = ArrayList()
        }
        i += 3
    }

    return imageTable
}

fun main(args: Array<String>) {
    val imagePixels = tgaToImagePixels(fileName = "src\\main\\java\\lab4\\${args[0]}")
    val encodedImagePixels = ArrayList<ArrayList<ArrayList<Pixel>>>()

    encodedImagePixels.add(jpegLS(imagePixels) { _, w, _ -> w })
    encodedImagePixels.add(jpegLS(imagePixels) { n, _, _ -> n })
    encodedImagePixels.add(jpegLS(imagePixels) { _, _, nw -> nw })
    encodedImagePixels.add(jpegLS(imagePixels) { n, w, nw -> n.addPixel(w).subPixel(nw) })
    encodedImagePixels.add(jpegLS(imagePixels) { n, w, _ -> n.addPixel(w).divPixel(num = 2) })
    encodedImagePixels.add(jpegLS(imagePixels) { n, w, nw -> n.addPixel(w.subPixel(nw)).divPixel(num = 2) })
    encodedImagePixels.add(jpegLS(imagePixels) { n, w, nw -> w.addPixel(n.subPixel(nw)).divPixel(num = 2) })
    encodedImagePixels.add(jpegLS(imagePixels) { n, w, nw ->
       Pixel(
           r = if (nw.r >= max(n.r, w.r)) { max(n.r, w.r) } else if (nw.r <= min(n.r, w.r)) { min(n.r, w.r) } else { n.r + w.r - nw.r },
           g = if (nw.g >= max(n.g, w.r)) { max(n.g, w.g) } else if (nw.g <= min(n.g, w.g)) { min(n.g, w.g) } else { n.g + w.g - nw.g },
           b = if (nw.r >= max(n.b, w.b)) { max(n.b, w.b) } else if (nw.b <= min(n.b, w.b)) { min(n.b, w.b) } else { n.b + w.b - nw.b }
       )
    })

    for ((nr, el) in encodedImagePixels.withIndex()) {
        println("Scheme ${nr + 1}, red entropy = ${calcEntropy(el, "r")}")
        println("Scheme ${nr + 1}, green entropy = ${calcEntropy(el, "g")}")
        println("Scheme ${nr + 1}, blue entropy = ${calcEntropy(el, "b")}")
        println("Scheme ${nr + 1}, total entropy = ${calcEntropy(el, "")}\n")
    }
}
