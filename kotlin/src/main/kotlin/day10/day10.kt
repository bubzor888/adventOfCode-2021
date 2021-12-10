package day10

import java.io.File
import java.nio.file.Paths
import kotlin.math.exp
import kotlin.test.assertEquals

fun main() {
    val path = Paths.get("").toAbsolutePath().toString()

    val testFile = File("$path\\src\\main\\kotlin\\day10\\testInput.txt")
    val test1 = testFile.readLines()

    assertEquals(26397, execute(test1))
    assertEquals(288957, execute2(test1))

    println("Tests passed, attempting input")

    val inputFile = File("$path\\src\\main\\kotlin\\day10\\input.txt")
    println("Final Result 1: ${execute(inputFile.readLines())}")
    println("Final Result 2: ${execute2(inputFile.readLines())}")

    //Alternative to read whole file, when splitting on \n is easier
    //inputFile.readText()
}

private fun execute(input: List<String>): Int {
    val charMap = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
    val charValue = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)

    return input.fold(0) { acc, line ->
        val expected = ArrayDeque<Char>()
        var newPoints = 0
        for (ch in line) {
            when (ch) {
                in charMap.keys -> expected.addFirst(charMap[ch]!!)
                expected.first() -> expected.removeFirst()
                else -> {
                    newPoints = charValue[ch]!!
                    break;
                }
            }
        }
        acc + newPoints
    }
}

private fun execute2(input: List<String>): Long {
    val charMap = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
    val charValue = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
    val lineScores = mutableListOf<Long>()

    input.forEach { line ->
        val expected = ArrayDeque<Char>()
        var corrupted = false
        //First determine corrupted or incomplete
        for (ch in line) {
            when (ch) {
                in charMap.keys -> expected.addFirst(charMap[ch]!!)
                expected.first() -> expected.removeFirst()
                else -> {
                    corrupted = true
                    break;
                }
            }
        }
        //Now score if incomplete
        if (!corrupted && expected.size > 0) {
            lineScores.add(expected.fold(0L) { acc, ch -> acc * 5 + charValue[ch]!! } )
        }
    }

    lineScores.sort()
    return lineScores[lineScores.size/2]
}