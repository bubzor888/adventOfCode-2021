package day14

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val path = Paths.get("").toAbsolutePath().toString()

//    val test1 = listOf("")
    val testFile = File("$path\\src\\main\\kotlin\\day14\\testInput.txt")
    val test1 = testFile.readLines()

    assertEquals(1588, execute3(test1, 10))
    assertEquals(2188189693529, execute3(test1, 40))

    println("Tests passed, attempting input")

    val inputFile = File("$path\\src\\main\\kotlin\\day14\\input.txt")
    println("Final Result 1: ${execute3(inputFile.readLines(), 10)}")
    println("Final Result 2: ${execute3(inputFile.readLines(), 40)}")

    //Alternative to read whole file, when splitting on \n is easier
    //inputFile.readText()
}

private fun execute(input: List<String>, steps: Int): Int {
    val pattern = """(\w{2}) -> (\w)""".toRegex()
    var polymer = input[0].toList()
    val instructions = mutableMapOf<List<Char>, List<Char>>().withDefault { emptyList() }
    input.subList(2, input.size).forEach {
        val (key, value) = pattern.matchEntire(it)!!.destructured
        //Map looks like CH -> CB, since the H will be added in the next lookup
        instructions[key.toList()] = listOf(key[0], value.toCharArray()[0])
    }

    //First build the polymer
    for (step in 1..steps) {
        var newPolymer = mutableListOf<Char>()
        for (i in 1 until polymer.size) {
            newPolymer.addAll(instructions.getValue(listOf(polymer[i-1], polymer[i])))
        }
        newPolymer.add(polymer.last())

        polymer = newPolymer.toMutableList()
    }

    //Now count occurrences of elements
    val elementMap = mutableMapOf<Char, Int>().withDefault { 0 }
    polymer.forEach { elementMap[it] = elementMap.getValue(it) + 1 }
    val sorted = elementMap.values.sortedDescending()

    return sorted.first() - sorted.last()
}

private fun calculatePolymer(instructions: MutableMap<String, String>, target: String): String {
    //Size 2 will always be found here, since we have all those to start
    if (instructions.getValue(target).isNotEmpty()) {
        return instructions.getValue(target);
    } else {
        //So here we're really just dealing with size 3 or greater
        val newPolymer = when {
            target.length >= 4 -> {
                //Cut it half, find both halves, but also do the joint of the two halves
                val beginning = calculatePolymer(instructions, target.substring(0, target.length/2))
                val middle = calculatePolymer(instructions, target.substring(target.length/2-1, target.length/2+1))
                beginning.substring(0, beginning.length-1)
                    .plus(middle.substring(0, middle.length-1))
                    .plus(calculatePolymer(instructions, target.substring(target.length/2, target.length)))
            }
            else -> {
                //Size 3
                val beginning = instructions.getValue(target.substring(0, 2))
                beginning.substring(0, beginning.length-1).plus(instructions.getValue(target.substring(1, 3)))
            }
        }
        //Add it to the map for future use
        instructions[target] = newPolymer
        return newPolymer
    }
}

private fun execute2(input: List<String>, steps: Int): Long {
    val pattern = """(\w{2}) -> (\w)""".toRegex()
    var polymerList = mutableListOf(input[0])
    val basicInstructions = mutableMapOf<String, String>().withDefault { "" }
    val instructions = mutableMapOf<String, String>().withDefault { "" }
    input.subList(2, input.size).forEach {
        val (key, value) = pattern.matchEntire(it)!!.destructured
        //Map looks like CH -> CB, since the H will be added in the next lookup
        instructions[key] = key[0].toString() + value + key[1].toString()
        basicInstructions[key] = value
    }

    //First build the polymer
    for (step in 1..steps) {
        //The polymer may get bigger than we can hold in a single string
        //If the length is bigger than 1,000 then split it
        //They all grow at the same rate, so just check the first one
        var newPolymerList = mutableListOf<String>()
        if (polymerList[0].length > 1000) {
            polymerList.forEach {
                newPolymerList.add(it.substring(0, it.length/2))
                newPolymerList.add(it.substring(it.length/2))
            }
            polymerList = newPolymerList
        }
        //Reset the new list for processing
        newPolymerList = mutableListOf()
        polymerList.forEach {
            newPolymerList.add(calculatePolymer(instructions, it))
        }
        polymerList = newPolymerList

        println("Step $step")
        if (step == 25) {
            print("test")
        }
    }

    //Now we also need to calculate the elements between each split
    var moreElements = ""
    for (i in 1 until polymerList.size) {
        moreElements += basicInstructions.getValue(polymerList[i-1].last().toString() + polymerList[i].first())
    }
    polymerList.add(moreElements)

    //Now count occurrences of elements
    val elementMap = mutableMapOf<Char, Long>().withDefault { 0 }
    polymerList.forEach { polymer -> polymer.forEach { elementMap[it] = elementMap.getValue(it) + 1 } }
    val sorted = elementMap.values.sortedDescending()

    return sorted.first() - sorted.last()
}

class Node (val data: String, val left: String, val right: String)



private fun execute3(input: List<String>, steps: Int): Long {
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