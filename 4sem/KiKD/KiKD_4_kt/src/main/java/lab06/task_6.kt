package lab06

import java.io.File

class Pixel(var r: Int = 0, var g: Int = 0, var b: Int = 0) {
    fun addPixel(pixel: Pixel): Pixel = Pixel((r + pixel.r), (g + pixel.g), (b + pixel.b))
    fun subPixel(pixel: Pixel): Pixel = Pixel((r - pixel.r), (g - pixel.g), (b - pixel.b))
    fun modPixel(num: Int): Pixel = Pixel(r.mod(num), g.mod(num), b.mod(num))
    fun divPixel(num: Int): Pixel = Pixel((r / num), (g / num), (b / num))
}

fun toPosInt(byte: Byte): Int {
    return if (byte.toInt() >= 0) { byte.toInt() } else { (byte.toInt() + 256) }
}

fun readTgaImage(fileName: String): Pair<ArrayList<ArrayList<Pixel>>, List<Byte>> {
    val tgaBytes = File(fileName).readBytes()
    val header = tgaBytes.slice(0 until 18)

    val width = (toPosInt(tgaBytes[13]) * 256) + toPosInt(tgaBytes[12])
    val height = (toPosInt(tgaBytes[15]) * 256) + toPosInt(tgaBytes[14])

    val noHeader = tgaBytes.slice(18 until tgaBytes.size)
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
    return Pair(imageTable, header)
}


fun encode(inFilename: String = "src\\main\\java\\lab4\\example0.tga", outFile: String = "result_encoded", k: Int = 4) {
    val parsedImage = readTgaImage(inFilename)

    val imagePixels = parsedImage.first
    val header = parsedImage.second


}

fun decode(inFilename: String = "result_encoded", outFile:String = "result_decoded.tga") {

}

fun calcAndShowErrors(
    original: String = "src\\main\\java\\lab4\\example0.tga",
    result: String = "src\\main\\java\\lab06\\example0.tga"
) {

}


fun main(args: Array<String>) {
    encode()
    decode()
}