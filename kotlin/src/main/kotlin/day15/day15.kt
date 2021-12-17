package day15

import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

fun main() {
    val path = Paths.get("").toAbsolutePath().toString()

//    val test1 = listOf("")
    val test1 = File("$path\\src\\main\\kotlin\\day15\\testInput.txt").readLines();

    assertEquals(40, execute(test1))
    assertEquals(315, execute2(test1))

    println("Tests passed, attempting input")

    val inputFile = File("$path\\src\\main\\kotlin\\day15\\input.txt")
    println("Final Result 1: ${execute(inputFile.readLines())}")
    println("Final Result 2: ${execute2(inputFile.readLines())}")

    //Alternative to read whole file, when splitting on \n is easier
    //inputFile.readText()
}

class Node (val nodeRisk: Int, var totalRisk: Int, var neighbors: List<Node>) {
    fun updateTotalRisk(pathRisk: Int) {
        if (pathRisk + nodeRisk < totalRisk) {
            totalRisk = pathRisk + nodeRisk
        }
    }
}

private fun calculateNeighbors(graph: Map<Pair<Int, Int>, Node>, node: Pair<Int, Int>): List<Node> {
    val neighbors = mutableListOf<Node>()
    graph[Pair(node.first, node.second-1)]?.let { neighbors.add(it) }
    graph[Pair(node.first, node.second+1)]?.let { neighbors.add(it) }
    graph[Pair(node.first-1, node.second)]?.let { neighbors.add(it) }
    graph[Pair(node.first+1, node.second)]?.let { neighbors.add(it) }
    return neighbors
}

private fun execute(input: List<String>): Int {
    val graph = mutableMapOf<Pair<Int, Int>, Node>()
    input.forEachIndexed { y, row ->
        row.forEachIndexed{ x, riskValue ->
            //Set the total risk of everything except starting node to max
            var totalRisk = if (x == 0 && y == 0) 0 else Integer.MAX_VALUE
            //We'll use a map at first, and then build the neighbors later
            graph[Pair(x, y)] = Node(Character.getNumericValue(riskValue), totalRisk, mutableListOf())
        }
    }

    //Now populate the neighbors
    graph.forEach { (key, value) -> value.neighbors = calculateNeighbors(graph, key) }

    //We need to go through it enough to make sure we have the best path
    for (i in graph.keys.indices) {
        graph.values.forEach { currentNode ->
            currentNode.neighbors.forEach { it.updateTotalRisk(currentNode.totalRisk) }
        }
    }

    return graph.getValue(Pair(input[0].length-1, input.size-1)).totalRisk
}

private fun execute2(input: List<String>): Int {
    val baseGraph = mutableMapOf<Pair<Int, Int>, Node>()
    input.forEachIndexed { y, row ->
        row.forEachIndexed{ x, riskValue ->
            //Set the total risk of everything except starting node to max
            var totalRisk = if (x == 0 && y == 0) 0 else Integer.MAX_VALUE
            //We'll use a map at first, and then build the neighbors later
            baseGraph[Pair(x, y)] = Node(Character.getNumericValue(riskValue), totalRisk, mutableListOf())
        }
    }

    //Now create the full graph
    val baseX = input[0].length
    val baseY = input.size
    val graph = baseGraph.toMutableMap()
    for (boardX in 0..4) {
        for (boardY in 0..4) {
            //Skip over the first one
            if (boardX != 0 || boardY != 0) {
                baseGraph.forEach { (key, value) ->
                    val newX = (baseX * boardX) + key.first
                    val newY = (baseY * boardY) + key.second
                    var newRisk = (value.nodeRisk + boardX + boardY) % 9
                    if (newRisk == 0) newRisk = 9
                    graph[Pair(newX, newY)] = Node(newRisk, Integer.MAX_VALUE, mutableListOf())
                }
            }
        }
    }

    //Now populate the neighbors
    graph.forEach { (key, value) -> value.neighbors = calculateNeighbors(graph, key) }

    //We need to go through it enough to make sure we have the best path
    for (i in graph.keys.indices) {
        graph.values.forEach { currentNode ->
            currentNode.neighbors.forEach { it.updateTotalRisk(currentNode.totalRisk) }
        }
        if (i % 2000 == 0) println("iteration $i : ${graph.getValue(Pair(baseX * 5 - 1, baseY * 5 - 1)).totalRisk}")
    }

    return graph.getValue(Pair(baseX * 5 - 1, baseY * 5 - 1)).totalRisk
}