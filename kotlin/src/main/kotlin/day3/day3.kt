package day1

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val test1 = listOf("00100","11110","10110","10111","10101","01111","00111","11100","10000","11001","00010","01010")
    assertEquals(198, execute(test1))
//    assertEquals(5, execute2(test1))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day3\\input.txt"

    println("Final Result 1: ${execute(File(fileName).readLines())}")
//    println("Final Result 2: ${execute2(File(fileName).readLines())}")

//    Alternative to read whole file, when splitting on \n is easier:
//    println("Final Result 1: ${execute(File(fileName).readText())}")
//    println("Final Result 2: ${execute2(File(fileName).readText())}")
}

private fun transpose(input: List<String>): Array<IntArray> {
    val row = input.size
    val column = input[0].length

    // Transpose the matrix
    val transpose = Array(column) { IntArray(row) }
    for (i in 0 until row) {
        for (j in 0 until column) {
            transpose[j][i] = Character.getNumericValue(input[i][j])
        }
    }
    return transpose
}

private fun execute(input: List<String>): Int {
    //First transpose the array to make it easier to sum things up
    val array = transpose(input)

    //Now just sum up each row. If more than 1/2 the length then 1, otherwise 0
    val g = array.joinToString("") { row -> if (row.sum() > row.size / 2) "1" else "0" }.toInt(2)
    val e = array.joinToString("") { row -> if (row.sum() < row.size / 2) "1" else "0" }.toInt(2)

    return g * e
}

private fun execute2(input: List<String>): Int {
    //Again, transpose first
    val array = transpose(input)

    val oxCriteria = array.map { row -> if (row.sum() >= row.size / 2) "1" else "0" }

    input.forEachIndexed { index, string ->  }

    return 0
}