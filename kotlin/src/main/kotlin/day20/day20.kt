package day20

import java.io.File
import java.nio.file.Paths
import kotlin.math.pow
import kotlin.test.assertEquals

fun main() {
    val path = Paths.get("").toAbsolutePath().toString()

    val test1 = File("$path\\src\\main\\kotlin\\day20\\testInput.txt").readLines()

    assertEquals(35, execute(test1, 2))
    assertEquals(3351, execute(test1, 50))

    println("Tests passed, attempting input")

    val input = File("$path\\src\\main\\kotlin\\day20\\input.txt").readLines()
    //Alternative to read whole file, use .readText()

    println("Final Result 1: ${execute(input, 2)}")
    println("Final Result 2: ${execute(input, 50)}")


}

fun binaryToDecimal(num: String): Int {
    return num.reversed().foldIndexed(0) {
            index, sum, bit -> sum + Character.getNumericValue(bit) * (2.0).pow(index).toInt()
    }
}

private fun pixelOutput(image: Map<Pair<Int, Int>, Char>, pos: Pair<Int, Int>): Int {
    val result = mutableListOf<Char>()
    result.add(image.getValue(Pair(pos.first-1, pos.second-1)))
    result.add(image.getValue(Pair(pos.first, pos.second-1)))
    result.add(image.getValue(Pair(pos.first+1, pos.second-1)))
    result.add(image.getValue(Pair(pos.first-1, pos.second)))
    result.add(image.getValue(Pair(pos.first, pos.second)))
    result.add(image.getValue(Pair(pos.first+1, pos.second)))
    result.add(image.getValue(Pair(pos.first-1, pos.second+1)))
    result.add(image.getValue(Pair(pos.first, pos.second+1)))
    result.add(image.getValue(Pair(pos.first+1, pos.second+1)))

    return binaryToDecimal(result.joinToString("") { if (it == '#') "1" else "0" })
}

private fun execute(input: List<String>, applications: Int): Long {
    val enhancements = input[0]
    var image = mutableMapOf<Pair<Int, Int>, Char>().withDefault { '.' }
    input.subList(2, input.size).forEachIndexed { y, row ->
        row.forEachIndexed{ x, pixel ->
            image[Pair(x,y)] = pixel
        }
    }

    var (xMin, yMin, xMax, yMax) = listOf(-1, -1, input[2].length, input.size-2)
    for (i in 0 until applications) {
        val newImage = mutableMapOf<Pair<Int, Int>, Char>().withDefault {
            if (enhancements.first() == '.') '.'
            else if (i%2 == 0) enhancements.first()
            else enhancements.last()
        }
        for (x in xMin..xMax) {
            for (y in yMin..yMax) {
                newImage[Pair(x,y)] = enhancements[pixelOutput(image, Pair(x, y))]
            }
        }
        image = newImage
//        printImage(image, xMin, yMin, xMax, yMax)
        xMin--
        yMin--
        xMax++
        yMax++
    }

    return image.values.fold(0L) { acc, pixel -> if (pixel == '#') acc + 1 else acc }
}

private fun printImage(image: Map<Pair<Int, Int>, Char>, xMin: Int, yMin: Int, xMax: Int, yMax: Int) {
    println()
    for (x in xMin..xMax) {
        for (y in yMin..yMax) {
            print(image.getValue(Pair(y,x)))
        }
        println()
    }
    println()
}