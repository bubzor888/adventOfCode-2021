package day7

import java.io.File
import java.nio.file.Paths
import kotlin.math.abs
import kotlin.test.assertEquals

fun main() {
    val test1 = "16,1,2,0,4,2,7,1,2,14".split(",").map { it.toInt() }
    assertEquals(37, execute(test1))
    assertEquals(168, execute2(test1))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day7\\input.txt"
//
//    println("Final Result 1: ${execute(File(fileName).readLines())}")
//    println("Final Result 2: ${execute2(File(fileName).readLines())}")

//    Alternative to read whole file, when splitting on \n is easier:
    val input = File(fileName).readText().split(",").map { it.toInt() }
    println("Final Result 1: ${execute(input)}")
    println("Final Result 2: ${execute2(input)}")
}

private fun calculateFuel(crabs: Map<Int, Int>, targetPos: Int): Int {
    var fuel = 0;

    for ((crabPos, crabCount) in crabs) {
        fuel += abs(targetPos - crabPos) * crabCount
    }

    return fuel
}

private fun execute(input: List<Int>): Int {
    val crabs : MutableMap<Int, Int> = mutableMapOf()
    var min = Integer.MAX_VALUE
    var max = 0
    input.forEach {
        crabs[it] = crabs.getOrDefault(it, 0) + 1
        if (it < min) min = it
        if (it > max) max = it
    }

    var minFuel = Integer.MAX_VALUE
    for (targetPos in min..max) {
        val fuel = calculateFuel(crabs, targetPos)
        if (fuel < minFuel) minFuel = fuel
    }

    return minFuel
}

private fun calculateFuel2(crabs: Map<Int, Int>, targetPos: Int): Int {
    var fuel = 0;

    for ((crabPos, crabCount) in crabs) {
        val n = abs(targetPos - crabPos)
        fuel += ((n * (n+1)) / 2) * crabCount
    }

    return fuel
}

private fun execute2(input: List<Int>): Int {
    val crabs : MutableMap<Int, Int> = mutableMapOf()
    var min = Integer.MAX_VALUE
    var max = 0
    input.forEach {
        crabs[it] = crabs.getOrDefault(it, 0) + 1
        if (it < min) min = it
        if (it > max) max = it
    }

    var minFuel = Integer.MAX_VALUE
    for (targetPos in min..max) {
        val fuel = calculateFuel2(crabs, targetPos)
        if (fuel < minFuel) minFuel = fuel
    }

    return minFuel
}