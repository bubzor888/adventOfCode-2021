package day14

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val path = Paths.get("").toAbsolutePath().toString()

//    val test1 = listOf("")
    val testFile = File("$path\\src\\main\\kotlin\\day14\\testInput.txt")
    val test1 = testFile.readLines()

    assertEquals(1588, execute(test1, 10))
    assertEquals(2188189693529, execute(test1, 40))

    println("Tests passed, attempting input")

    val inputFile = File("$path\\src\\main\\kotlin\\day14\\input.txt")
    println("Final Result 1: ${execute(inputFile.readLines(), 10)}")
    println("Final Result 2: ${execute(inputFile.readLines(), 40)}")

    //Alternative to read whole file, when splitting on \n is easier
    //inputFile.readText()
}

private fun execute(input: List<String>, steps: Int): Long {
    val pattern = """(\w{2}) -> (\w)""".toRegex()
    var currentPairs = mutableMapOf<String, Long>().withDefault { 0L }
    for (i in 1 until input[0].length) {
        val pair = input[0][i-1].toString() + input[0][i]
        currentPairs[pair] = currentPairs.getValue(pair) + 1
    }

    val instructions = mutableMapOf<String, Pair<String, String>>()
    input.subList(2, input.size).forEach {
        val (key, value) = pattern.matchEntire(it)!!.destructured
        //NN -> C turns into NN -> (NC, CN)
        instructions[key] = Pair(key[0] + value, value + key[1])
    }

    //do the pairs for all depths
    for (step in 0 until steps) {
        val newPairs = mutableMapOf<String, Long>().withDefault { 0L }
        currentPairs.forEach { (pair, count) ->
            newPairs[instructions.getValue(pair).first] = newPairs.getValue(instructions.getValue(pair).first) + count
            newPairs[instructions.getValue(pair).second] = newPairs.getValue(instructions.getValue(pair).second) + count
        }
        currentPairs = newPairs
    }

    //Now figure out how much of each element
    val elementMap = mutableMapOf<Char, Long>().withDefault { 0 }
    currentPairs.forEach { (pair, count) ->
        elementMap[pair[1]] = elementMap.getValue(pair[1]) + count
    }
    //And finally add the very first element
    elementMap[input[0][0]] = elementMap.getValue(input[0][0]) + 1
    val sorted = elementMap.values.sortedDescending()

    return sorted.first() - sorted.last()
}