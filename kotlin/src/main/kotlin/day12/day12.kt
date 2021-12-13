package day12

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val path = Paths.get("").toAbsolutePath().toString()

//    val test1 = listOf("")
    val test1 = File("$path\\src\\main\\kotlin\\day12\\testInput.txt").readLines()
    val test2 = File("$path\\src\\main\\kotlin\\day12\\testInput2.txt").readLines()
    val test3 = File("$path\\src\\main\\kotlin\\day12\\testInput3.txt").readLines()

    assertEquals(10, execute(test1))
    assertEquals(19, execute(test2))
    assertEquals(226, execute(test3))

    assertEquals(36, execute2(test1))
    assertEquals(103, execute2(test2))
    assertEquals(3509, execute2(test3))

    println("Tests passed, attempting input")

    val inputFile = File("$path\\src\\main\\kotlin\\day12\\input.txt")
    println("Final Result 1: ${execute(inputFile.readLines())}")
    println("Final Result 2: ${execute2(inputFile.readLines())}")

    //Alternative to read whole file, when splitting on \n is easier
    //inputFile.readText()
}

private fun buildConnections(input: List<String>): Map<String, List<String>> {
    val connectionMap = mutableMapOf<String, List<String>>().withDefault { emptyList() }
    input.forEach {
        val (left, right) = it.split("-")
        //Don't need to track connections from "end"
        //And don't need to map back to "start"
        if (left != "end" && right != "start") {
            connectionMap[left] = connectionMap.getValue(left).plus(right)
        }
        if (right != "end" && left != "start") {
            connectionMap[right] = connectionMap.getValue(right).plus(left)
        }
    }
    return connectionMap
}

private fun execute(input: List<String>): Int {
    val connectionMap = buildConnections(input)

    var totalPaths = 0
    var unfinishedPaths = listOf(listOf("start"))
    while (unfinishedPaths.isNotEmpty()) {
        val newPaths = unfinishedPaths.toMutableList()
        unfinishedPaths.forEach { path ->
            connectionMap.getValue(path.last()).forEach { cave ->
                if (cave == "end") {
                    totalPaths++
                }
                if (cave[0].isUpperCase() || !path.contains(cave)) {
                    newPaths.add(path.plus(cave))
                }
            }
            newPaths.remove(path)
        }
        unfinishedPaths = newPaths
    }

    return totalPaths
}

private fun checkSmallCaves(path: List<String>, cave: String): Boolean {
    //If the cave isn't in the path, then it's fine
    if (!path.contains(cave)) return true

    //Otherwise, we need to check if another small cave already appears twice
    val smallCaves = mutableListOf<String>()
    path.forEach { cave ->
        if (cave[0].isLowerCase()) {
            if (smallCaves.contains(cave)) return false
            else smallCaves.add(cave)
        }
    }

    return true
}

private fun execute2(input: List<String>): Int {
    val connectionMap = buildConnections(input)

    var totalPaths = 0
    var unfinishedPaths = listOf(listOf("start"))
    while (unfinishedPaths.isNotEmpty()) {
        val newPaths = unfinishedPaths.toMutableList()
        unfinishedPaths.forEach { path ->
            connectionMap.getValue(path.last()).forEach { cave ->
                if (cave == "end") {
                    totalPaths++
                }
                if (cave[0].isUpperCase() || checkSmallCaves(path, cave)) {
                    newPaths.add(path.plus(cave))
                }
            }
            newPaths.remove(path)
        }
        unfinishedPaths = newPaths
    }

    return totalPaths
}