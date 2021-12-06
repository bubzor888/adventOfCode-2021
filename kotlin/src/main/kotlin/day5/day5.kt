package day5

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val path = Paths.get("").toAbsolutePath().toString()
    val testInput = File("$path\\src\\main\\kotlin\\day5\\sampleInput.txt").readLines()

    assertEquals(5, execute(testInput))
    assertEquals(12, execute2(testInput))

    println("Tests passed, attempting input")

    val fileName = "$path\\src\\main\\kotlin\\day5\\input.txt"
    println("Final Result 1: ${execute(File(fileName).readLines())}")
    println("Final Result 2: ${execute2(File(fileName).readLines())}")

//    Alternative to read whole file, when splitting on \n is easier:
//    println("Final Result 1: ${execute(File(fileName).readText())}")
//    println("Final Result 2: ${execute2(File(fileName).readText())}")
}

private fun parseData(input: List<String>): List<List<Int>> {
    val pattern = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()
    return input.map { line -> pattern.matchEntire(line)!!.destructured.toList().map { it.toInt() }}
}

private fun makeGrid(input: List<List<Int>>): List<IntArray> {
    var xMax = 0
    var yMax = 0
    input.forEach {
        if (it[0] >= it[2] && it[0] > xMax) {
            xMax = it[0]
        } else if (it[2] > it[0] && it[2] > xMax) {
            xMax = it[2]
        }

        if (it[1] >= it[3] && it[1] > yMax) {
            yMax = it[1]
        } else if (it[3] > it[1] && it[3] > yMax) {
            yMax = it[3]
        }
    }
    return List(yMax+1) { IntArray(xMax+1) }
}

private fun execute(input: List<String>): Int {
    val lines = parseData(input).filter { it[0] == it[2] || it[1] == it[3] }
    val grid = makeGrid(lines)

    lines.forEach {
        if (it[0] == it[2]) {
            if (it[1] < it[3]) {
                for (y in it[1]..it[3]) {
                    grid[y][it[0]]++
                }
            } else {
                for (y in it[1]downTo it[3]) {
                    grid[y][it[0]]++
                }
            }
        } else {
            if (it[0] < it[2]) {
                for (x in it[0]..it[2]) {
                    grid[it[1]][x]++
                }
            } else {
                for (x in it[0]downTo it[2]) {
                    grid[it[1]][x]++
                }
            }
        }
    }

    return grid.fold(0) { acc, row ->
        acc + row.fold(0) { acc2, point -> if (point > 1) acc2 + 1 else acc2} }
}

private fun execute2(input: List<String>): Int {
    val lines = parseData(input)
    val grid = makeGrid(lines)

    lines.forEach {
        if (it[0] == it[2]) {
            if (it[1] < it[3]) {
                for (y in it[1]..it[3]) {
                    grid[y][it[0]]++
                }
            } else {
                for (y in it[1]downTo it[3]) {
                    grid[y][it[0]]++
                }
            }
        } else if (it[1] == it[3]) {
            if (it[0] < it[2]) {
                for (x in it[0]..it[2]) {
                    grid[it[1]][x]++
                }
            } else {
                for (x in it[0]downTo it[2]) {
                    grid[it[1]][x]++
                }
            }
        } else {
            if (it[0] < it[2]) {
                if (it[1] < it[3]) {
                    var x = it[0]
                    var y = it[1]
                    while (x <= it[2] && y <= it[3]) {
                        grid[y][x]++
                        x++
                        y++
                    }
                } else {
                    var x = it[0]
                    var y = it[1]
                    while (x <= it[2] && y >= it[3]) {
                        grid[y][x]++
                        x++
                        y--
                    }
                }
            } else {
                if (it[1] < it[3]) {
                    var x = it[0]
                    var y = it[1]
                    while (x >= it[2] && y <= it[3]) {
                        grid[y][x]++
                        x--
                        y++
                    }
                } else {
                    var x = it[0]
                    var y = it[1]
                    while (x >= it[2] && y >= it[3]) {
                        grid[y][x]++
                        x--
                        y--
                    }
                }
            }
        }
    }

    return grid.fold(0) { acc, row ->
        acc + row.fold(0) { acc2, point -> if (point > 1) acc2 + 1 else acc2} }
}