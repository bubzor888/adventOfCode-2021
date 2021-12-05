package day4

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val path = Paths.get("").toAbsolutePath().toString()
    val testInput = File("$path\\src\\main\\kotlin\\day4\\sampleInput.txt").readLines()

    assertEquals(4512, execute(testInput))
    assertEquals(1924, execute2(testInput))

    println("Tests passed, attempting input")

    val fileName = "$path\\src\\main\\kotlin\\day4\\input.txt"
    println("Final Result 1: ${execute(File(fileName).readLines())}")
    println("Final Result 2: ${execute2(File(fileName).readLines())}")

//    Alternative to read whole file, when splitting on \n is easier:
//    println("Final Result 1: ${execute(File(fileName).readText())}")
//    println("Final Result 2: ${execute2(File(fileName).readText())}")
}

private fun parseBoards(input: List<String>): List<List<List<String>>> {
    val pattern = """(\d+)""".toRegex()
    val result : MutableList<MutableList<List<String>>> = mutableListOf()
    input.subList(2, input.size)
        .filter { string -> string.isNotBlank() }
        .forEachIndexed() { index, string ->
            val row = pattern.findAll(string).toList().map { result -> result.value }
            if (index % 5 == 0) {
                //First row on each board makes a new board
                result.add(mutableListOf(row))
            } else {
                //Otherwise add a row to a board
                result.last().add(row)
            }
        }
    return result
}

fun <T> transpose(input: List<List<T>>) :List<List<T>> {
    return List(input[0].size) { index ->
        input.map { row ->
            row[index]
        }
    }
}

private fun checkWinner(board : List<List<String>>) : Boolean {
    return board.fold(false) { result, row -> result || row.joinToString("") == "XXXXX" }
            || transpose(board).fold(false) { result, row -> result || row.joinToString("") == "XXXXX" }
}

private fun execute(input: List<String>): Int {
    val draws = input[0].split(",")
    var boards = parseBoards(input)

    for (draw in draws) {
        boards = boards.map { board ->
            board.map { row ->
                row.map { number ->
                    if (number == draw) "X" else number
                }
            }
        }

        //Now check rows for a winner
        val winner = boards.filter { board -> checkWinner(board) }
        if (winner.isNotEmpty()) {
            return draw.toInt() * winner[0].fold(0) { acc, row ->
                acc + row.fold(0) { acc2, num ->
                    if (num != "X") acc2 + num.toInt() else acc2} }
        }
    }

    return 0
}

private fun execute2(input: List<String>): Int {
    val draws = input[0].split(",")
    var boards = parseBoards(input)

    for (draw in draws) {
        boards = boards.map { board ->
            board.map { row ->
                row.map { number ->
                    if (number == draw) "X" else number
                }
            }
        }

        val winners = boards.filter { board -> checkWinner(board) }
        if (winners.isNotEmpty()) {
            boards = boards.subtract(winners.toSet()).toList()
            if (boards.isEmpty()) {
                return draw.toInt() * winners[0].fold(0) { acc, row ->
                    acc + row.fold(0) { acc2, num ->
                        if (num != "X") acc2 + num.toInt() else acc2
                    }
                }
            }
        }
    }

    return 0
}