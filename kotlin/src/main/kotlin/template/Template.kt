package template

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val path = Paths.get("").toAbsolutePath().toString()

    val test1 = listOf("")
//    val testFile = File("$path\\src\\main\\kotlin\\dayX\\testInput.txt")
//    val test1 = testFile.readLines()

    assertEquals(0, execute(test1))

    println("Tests passed, attempting input")

    val inputFile = File("$path\\src\\main\\kotlin\\dayX\\input.txt")
//    println("Final Result 1: ${execute(inputFile.readLines())}")
//    println("Final Result 2: ${execute2(inputFile.readLines())}")

    //Alternative to read whole file, when splitting on \n is easier
    //inputFile.readText()
}

private fun execute(input: List<String>): Int {
    return 0
}

private fun execute2(input: List<String>): Int {
    return 0
}