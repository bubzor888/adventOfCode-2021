package day3

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val test1 = listOf("00100","11110","10110","10111","10101","01111","00111","11100","10000","11001","00010","01010")
    assertEquals(198, execute(test1))
    assertEquals(230, execute2(test1))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day3\\input.txt"

    println("Final Result 1: ${execute(File(fileName).readLines())}")
    println("Final Result 2: ${execute2(File(fileName).readLines())}")

//    Alternative to read whole file, when splitting on \n is easier:
//    println("Final Result 1: ${execute(File(fileName).readText())}")
//    println("Final Result 2: ${execute2(File(fileName).readText())}")
}

private fun transpose(input: List<IntArray>): Array<IntArray> {
    val row = input.size
    val column = input[0].size

    // Transpose the matrix
    val transpose = Array(column) { IntArray(row) }
    for (i in 0 until row) {
        for (j in 0 until column) {
            transpose[j][i] = input[i][j]
        }
    }
    return transpose
}

private fun execute(input: List<String>): Int {
    //Initialize an empty array with the right dimensions
    val array = transpose(input.map { s -> s.chars().map { ch -> Character.getNumericValue(ch) }.toArray() })

    //Now just sum up each row. If more than 1/2 the length then 1, otherwise 0
    val g = array.joinToString("") { row -> if (row.sum() > row.size / 2) "1" else "0" }.toInt(2)
    val e = array.joinToString("") { row -> if (row.sum() < row.size / 2) "1" else "0" }.toInt(2)

    return g * e
}

private fun execute2(input: List<String>): Int {
    //Convert to intArray first
    var oxygenArray = input.map { s -> s.chars().map { ch -> Character.getNumericValue(ch) }.toArray() }
    var co2Array = input.map { s -> s.chars().map { ch -> Character.getNumericValue(ch) }.toArray() }

    for (i in 0 until oxygenArray[0].size) {
        val transposed = transpose(oxygenArray)
        val bitCriteria = if (transposed[i].sum() >= transposed[i].size / 2.toDouble()) 1 else 0
        oxygenArray = oxygenArray.filter { row -> row[i] == bitCriteria }

        if (oxygenArray.size == 1) {
            break
        }
    }
    for (i in 0 until co2Array[0].size) {
        val transposed = transpose(co2Array)
        val bitCriteria = if (transposed[i].sum() < transposed[i].size / 2.toDouble()) 1 else 0
        co2Array = co2Array.filter { row -> row[i] == bitCriteria }

        if (co2Array.size == 1) {
            break
        }
    }

    return oxygenArray[0].joinToString("").toInt(2) * co2Array[0].joinToString("").toInt(2)
}