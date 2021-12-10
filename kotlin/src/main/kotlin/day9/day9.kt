package day9

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val path = Paths.get("").toAbsolutePath().toString()

    val test1 = listOf("2199943210","3987894921","9856789892","8767896789","9899965678")
//    val testFile = File("$path\\src\\main\\kotlin\\dayX\\input.txt")
//    val test1 = testFile.readLines()

    assertEquals(15, execute(test1))
    assertEquals(1134, execute2(test1))

    println("Tests passed, attempting input")

    val inputFile = File("$path\\src\\main\\kotlin\\day9\\input.txt")
    println("Final Result 1: ${execute(inputFile.readLines())}")
    println("Final Result 2: ${execute2(inputFile.readLines())}")

    //Alternative to read whole file, when splitting on \n is easier
    //inputFile.readText()
}

private fun execute(input: List<String>): Int {
    val grid = mutableMapOf<Pair<Int, Int>, Int>().withDefault { Int.MAX_VALUE }
    input.forEachIndexed { y, row -> row.forEachIndexed { x, num -> grid[Pair(x, y)] = Character.getNumericValue(num)} }

    return grid.keys.fold(0) { acc, point ->
        if (grid[point]!! < grid.getValue(Pair(point.first, point.second-1)) &&
                grid[point]!! < grid.getValue(Pair(point.first, point.second+1)) &&
                grid[point]!! < grid.getValue(Pair(point.first-1, point.second)) &&
                grid[point]!! < grid.getValue(Pair(point.first+1, point.second))) {
            acc + grid[point]!! + 1
        } else {
            acc
        }
    }
}

private fun execute2(input: List<String>): Int {
    val grid = mutableMapOf<Pair<Int, Int>, Int>().withDefault { -1 }
    input.forEachIndexed { y, row -> row.forEachIndexed { x, num -> grid[Pair(x, y)] = Character.getNumericValue(num)} }

    val basinSizeList = mutableListOf<Int>()
    grid.keys.forEach { point ->
        var basinSize = 0
        val toBeProcessed = ArrayDeque(listOf(point))
        while (toBeProcessed.size > 0) {
            val current = toBeProcessed.removeFirst()
            if (grid.getValue(current) != -1 && grid.getValue(current) != 9) {
                //Add the neighbors to the list to for processing
                toBeProcessed.add(Pair(current.first, current.second+1))
                toBeProcessed.add(Pair(current.first, current.second-1))
                toBeProcessed.add(Pair(current.first+1, current.second))
                toBeProcessed.add(Pair(current.first-1, current.second))

                //Increase basin size and mark node as processed
                basinSize++
                grid[current] = -1
            }
        }
        basinSizeList.add(basinSize)
    }

    basinSizeList.sortDescending()
    return basinSizeList[0] * basinSizeList[1] * basinSizeList[2]
}