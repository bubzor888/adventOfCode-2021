package day8

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val path = Paths.get("").toAbsolutePath().toString()

    val testFile = "$path\\src\\main\\kotlin\\day8\\testInput.txt"
    val testInput = File(testFile).readLines()
    assertEquals(26, execute(testInput))
    assertEquals(61229, execute2(testInput))

    println("Tests passed, attempting input")

    val inputFile = File("$path\\src\\main\\kotlin\\day8\\input.txt")
    println("Final Result 1: ${execute(inputFile.readLines())}")
    println("Final Result 2: ${execute2(inputFile.readLines())}")

}

private fun execute(input: List<String>): Int {
    return input.fold(0) { acc, line ->
        acc + line.split(" | ")[1].split(" ").fold(0) { acc2, digit ->
            if (digit.length in listOf(2, 3, 4, 7)) acc2 + 1
            else acc2
        } }
}

private fun execute2(input: List<String>): Int {
    return input.fold(0) { total, line ->
        val (tests, output) = line.split(" | ")
        val digits = calculateDigits(tests)
        total + output.split(" ").fold("") { acc, d ->
            acc + digits[digits.keys.first { d.toSet() == it.toSet() }]
        }.toInt()
    }
}

private fun calculateDigits(input: String): Map<String, Int> {
    val byLength = mutableMapOf<Int, MutableList<String>>()
    //First group the test numbers by length
    input.split(" ").forEach {
        byLength.putIfAbsent(it.length, mutableListOf())
        byLength[it.length]!!.add(it)
    }

    //Now start figuring out individual circuits and numbers
    val circuits = mutableMapOf<Char, Char>()
    val numbers = mutableMapOf<Int, String>()

    //We know right away that L2=1, L4=4, L3=7, L7=8
    numbers[1] = byLength[2]!!.first()
    numbers[4] = byLength[4]!!.first()
    numbers[7] = byLength[3]!!.first()
    numbers[8] = byLength[7]!!.first()

    //7 - 1 is circuit a
    circuits['a'] = numbers[7]!!.toSet().subtract(numbers[1]!!.toSet()).first()

    //Subtract each L6 from 1, if there is a character left then it's 6
    //And the missing character is c
    byLength[6]!!.forEach {
        val diff = numbers[1]!!.toSet().subtract(it.toSet())
        if (diff.size == 1) {
            numbers[6] = it
            circuits['c'] = diff.first()
        }
    }

    //1 - c = f
    circuits['f'] = numbers[1]!!.toSet().subtract( setOf( circuits['c']!!)).first()

    //The L5 without c is 5, the L5 without f is 2
    byLength[5]!!.forEach {
        if (!it.contains(circuits['c']!!)) {
            numbers[5] = it
        } else if (!it.contains(circuits['f']!!)) {
            numbers[2] = it
        }
    }
    //The other L5 is 3
    numbers[3] = byLength[5]!!.first { it != numbers[5]!! && it != numbers[2]!! }

    //8 - (2 + f) = b
    circuits['b'] = numbers[8]!!.toSet().subtract(numbers[2]!!.toSet().plus(circuits['f']!!)).first()
    //8 - (5 + c) = e
    circuits['e'] = numbers[8]!!.toSet().subtract(numbers[5]!!.toSet().plus(circuits['c']!!)).first()

    //The L6 w/o e is 9
    numbers[9] = byLength[6]!!.first { !it.contains(circuits['e']!!) }

    //Now we know 6 and 9, so the last L6 is 0
    numbers[0] = byLength[6]!!.first { it != numbers[6]!! && it != numbers[9]!! }

    //Now flip the map
    return numbers.map { (key, value) -> value to key }.toMap()
}