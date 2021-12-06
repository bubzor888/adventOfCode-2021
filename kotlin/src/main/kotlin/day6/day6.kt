package day6

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val test1 = "3,4,3,1,2".split(",").map { it.toInt() }
    assertEquals(5934, execute(test1, 80))
    assertEquals(26984457539, execute(test1, 256))

    println("Tests passed, attempting input")

    val path = Paths.get("").toAbsolutePath().toString()
    val fileName = "$path\\src\\main\\kotlin\\day6\\input.txt"
//
//    println("Final Result 1: ${execute(File(fileName).readLines())}")
//    println("Final Result 2: ${execute2(File(fileName).readLines())}")

//    Alternative to read whole file, when splitting on \n is easier:
    val input = File(fileName).readText().split(",").map { it.toInt() }
    println("Final Result 1: ${execute(input, 80)}")
    println("Final Result 2: ${execute(input, 256)}")
}

private fun execute(input: List<Int>, days: Int): Long {
    val fish : MutableMap<Int, Long> = mutableMapOf()
    for (i in 0..6) {
        fish[i] = 0
    }
    input.forEach { fish[it] = fish[it]!! + 1 }

    var spawnDay = 0
    var twoDayFish = 0L
    var oneDayFish = 0L
    for (i in 0 until days) {
        //All fish that spawn today will make new fish
        val newFish = fish[spawnDay]

        //Move the fish not yet in the breeding cycle
        fish[spawnDay] = fish[spawnDay]!! + oneDayFish
        oneDayFish = twoDayFish
        twoDayFish = newFish!!

        //Change the spawn day
        if (spawnDay == 6) {
            spawnDay = 0
        } else {
            spawnDay++
        }
    }

    return fish.keys.fold(0L) { acc, i -> acc + fish[i]!! } + oneDayFish + twoDayFish
}