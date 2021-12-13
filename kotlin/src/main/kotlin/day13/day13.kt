package day13

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val path = Paths.get("").toAbsolutePath().toString()

//    val test1 = listOf("")
    val testFile = File("$path\\src\\main\\kotlin\\day13\\testInput.txt")
    val test1 = testFile.readLines()

    assertEquals(17, execute(test1))
    assertEquals(0, execute2(test1))

    println("Tests passed, attempting input")

    val inputFile = File("$path\\src\\main\\kotlin\\day13\\input.txt")
    println("Final Result 1: ${execute(inputFile.readLines())}")
    println("Final Result 2: ${execute2(inputFile.readLines())}")

    //Alternative to read whole file, when splitting on \n is easier
    //inputFile.readText()
}

private fun printMap(dotMap: Map<Pair<Int, Int>, Boolean>, xMax: Int, yMax: Int) {
    for (y in 0 .. yMax) {
        for (x in 0 .. xMax ) {
            if (dotMap.getValue(Pair(x, y))) print("#")
            else print(".")
        }
        println()
    }
}

private fun execute(input: List<String>): Int {
    var dotSection = true
    var (xMax, yMax) = listOf(0, 0)
    val dotMap = mutableMapOf<Pair<Int, Int>, Boolean>().withDefault { false }
    val foldInstructions = mutableListOf<String>()
    input.forEach { line ->
        if (dotSection) {
            if (line.isBlank()) dotSection = false
            else {
                val (x, y) = line.split(",").map { it.toInt() }
                dotMap[Pair(x, y)] = true
                if (x > xMax) xMax = x
                if (y > yMax) yMax = y
            }
        } else {
            foldInstructions.add(line.substring(11))
        }
    }

    val (axis, fold) = foldInstructions[0].split("=")
    when (axis) {
        "y" -> {
            for (x in 0 .. xMax) {
                for (y in 0..yMax) {
                    if (y < fold.toInt()) {
                        if (dotMap.getValue(Pair(x, yMax - y))) {
                            dotMap[Pair(x, y)] = true
                        }
                    } else {
                        dotMap.remove(Pair(x, y))
                    }
                }
            }
            yMax = fold.toInt() - 1
        }
        "x" -> {
            for (x in 0 .. xMax) {
                for (y in 0..yMax) {
                    if (x < fold.toInt()) {
                        if (dotMap.getValue(Pair(xMax - x, y))) {
                            dotMap[Pair(x, y)] = true
                        }
                    } else {
                        dotMap.remove(Pair(x, y))
                    }
                }
            }
            yMax = fold.toInt() - 1
        }
    }

    return dotMap.values.fold(0) { acc, dot -> if (dot) acc + 1 else acc }
}

private fun execute2(input: List<String>): Int {
    var dotSection = true
    var (xMax, yMax) = listOf(0, 0)
    val dotMap = mutableMapOf<Pair<Int, Int>, Boolean>().withDefault { false }
    val foldInstructions = mutableListOf<String>()
    input.forEach { line ->
        if (dotSection) {
            if (line.isBlank()) dotSection = false
            else {
                val (x, y) = line.split(",").map { it.toInt() }
                dotMap[Pair(x, y)] = true
                if (x > xMax) xMax = x
                if (y > yMax) yMax = y
            }
        } else {
            foldInstructions.add(line.substring(11))
        }
    }

    //Account for odd maxes
    if (xMax % 2 == 1) xMax++
    if (yMax % 2 == 1) yMax++

    foldInstructions.forEach {
        val (axis, fold) = it.split("=")
        when (axis) {
            "y" -> {
                for (x in 0..xMax) {
                    for (y in 0..yMax) {
                        if (y < fold.toInt()) {
                            if (dotMap.getValue(Pair(x, yMax - y))) {
                                dotMap[Pair(x, y)] = true
                            }
                        } else {
                            dotMap.remove(Pair(x, y))
                        }
                    }
                }
                yMax = fold.toInt() - 1
            }
            "x" -> {
                for (x in 0..xMax) {
                    for (y in 0..yMax) {
                        if (x < fold.toInt()) {
                            if (dotMap.getValue(Pair(xMax - x, y))) {
                                dotMap[Pair(x, y)] = true
                            }
                        } else {
                            dotMap.remove(Pair(x, y))
                        }
                    }
                }
                xMax = fold.toInt() - 1
            }
        }
    }

    printMap(dotMap, xMax, yMax)
    return 0
}