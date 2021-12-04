package day1

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val test1 = listOf("199","200","208","210","200","207","240","269","260","263")
    assertEquals(7, execute(test1))
    assertEquals(5, execute2(test1))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day1\\input.txt"

    println("Final Result 1: ${execute(File(fileName).readLines())}")
    println("Final Result 2: ${execute2(File(fileName).readLines())}")

//    Alternative to read whole file, when splitting on \n is easier:
//    println("Final Result 1: ${execute(File(fileName).readText())}")
//    println("Final Result 2: ${execute2(File(fileName).readText())}")
}

private fun execute(input: List<String>): Int {
    return countDepth(0, input.map { it.toInt() })
}

private fun countDepth(total: Int, remaining: List<Int>): Int {
    return if (remaining.size < 2) {
        total
    } else if (remaining[0] < remaining[1]) {
        countDepth(total + 1, remaining.subList(1, remaining.size))
    } else {
        countDepth(total, remaining.subList(1, remaining.size))
    }
}

private fun execute2(input: List<String>): Int {
    return countDepthBy3(0, input.map { it.toInt() })
}

private fun countDepthBy3(total: Int, remaining: List<Int>): Int {
    return if (remaining.size < 4) {
        total
    } else if (remaining[0] + remaining[1] + remaining[2] < remaining[1] + remaining[2] + remaining[3]) {
        countDepthBy3(total + 1, remaining.subList(1, remaining.size))
    } else {
        countDepthBy3(total, remaining.subList(1, remaining.size))
    }
}