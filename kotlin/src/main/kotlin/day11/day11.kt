package day11

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val path = Paths.get("").toAbsolutePath().toString()

//    val test1 = listOf("")
    val testFile = File("$path\\src\\main\\kotlin\\day11\\testInput.txt")
    val test1 = testFile.readLines()

    assertEquals(1656, execute(test1))
    assertEquals(195, execute2(test1))

    println("Tests passed, attempting input")

    val inputFile = File("$path\\src\\main\\kotlin\\day11\\input.txt")
    println("Final Result 1: ${execute(inputFile.readLines())}")
    println("Final Result 2: ${execute2(inputFile.readLines())}")

    //Alternative to read whole file, when splitting on \n is easier
    //inputFile.readText()
}

private fun adjacentPoints(grid: Map<Pair<Int, Int>, Int>, point: Pair<Int, Int>): List<Pair<Int, Int>> {
    val points = mutableListOf<Pair<Int, Int>>()
    if (grid.getValue(Pair(point.first-1, point.second-1)) != -1) points.add(Pair(point.first-1, point.second-1))
    if (grid.getValue(Pair(point.first, point.second-1)) != -1) points.add(Pair(point.first, point.second-1))
    if (grid.getValue(Pair(point.first+1, point.second-1)) != -1) points.add(Pair(point.first+1, point.second-1))
    if (grid.getValue(Pair(point.first-1, point.second)) != -1) points.add(Pair(point.first-1, point.second))
    if (grid.getValue(Pair(point.first+1, point.second)) != -1) points.add(Pair(point.first+1, point.second))
    if (grid.getValue(Pair(point.first-1, point.second+1)) != -1) points.add(Pair(point.first-1, point.second+1))
    if (grid.getValue(Pair(point.first, point.second+1)) != -1) points.add(Pair(point.first, point.second+1))
    if (grid.getValue(Pair(point.first+1, point.second+1)) != -1) points.add(Pair(point.first+1, point.second+1))
    return points
}

private fun execute(input: List<String>): Int {
    val grid = mutableMapOf<Pair<Int, Int>, Int>().withDefault { -1 }
    input.forEachIndexed { y, row -> row.forEachIndexed { x, num -> grid[Pair(x, y)] = Character.getNumericValue(num)} }

    var totalFlashes = 0
    for (step in 1..100) {
        //First increase everything by 1, and track flashes
        val toBeProcessed = ArrayDeque<Pair<Int, Int>>()
        grid.keys.forEach { point ->
            if (grid.getValue(point) + 1 > 9) {
                totalFlashes++
                grid[point] = 0
                toBeProcessed.addAll(adjacentPoints(grid, point))
            } else {
                grid[point] = grid.getValue(point) + 1
            }
        }

        //Now work through the flashes
        while (toBeProcessed.isNotEmpty()) {
            val point = toBeProcessed.removeFirst()
            if (grid.getValue(point) != 0) {
                if (grid.getValue(point) + 1 > 9) {
                    totalFlashes++
                    grid[point] = 0
                    toBeProcessed.addAll(adjacentPoints(grid, point))
                } else {
                    grid[point] = grid.getValue(point) + 1
                }
            }
        }
    }

    return totalFlashes
}

private fun execute2(input: List<String>): Int {
    val grid = mutableMapOf<Pair<Int, Int>, Int>().withDefault { -1 }
    input.forEachIndexed { y, row -> row.forEachIndexed { x, num -> grid[Pair(x, y)] = Character.getNumericValue(num)} }

    var flashes = 0
    var step = 0
    while(flashes != 100) {
        step++
        flashes = 0
        //First increase everything by 1, and track flashes
        val toBeProcessed = ArrayDeque<Pair<Int, Int>>()
        grid.keys.forEach { point ->
            if (grid.getValue(point) + 1 > 9) {
                flashes++
                grid[point] = 0
                toBeProcessed.addAll(adjacentPoints(grid, point))
            } else {
                grid[point] = grid.getValue(point) + 1
            }
        }

        //Now work through the flashes
        while (toBeProcessed.isNotEmpty()) {
            val point = toBeProcessed.removeFirst()
            if (grid.getValue(point) != 0) {
                if (grid.getValue(point) + 1 > 9) {
                    flashes++
                    grid[point] = 0
                    toBeProcessed.addAll(adjacentPoints(grid, point))
                } else {
                    grid[point] = grid.getValue(point) + 1
                }
            }
        }
    }

    return step
}