package day21

import kotlin.test.assertEquals

fun main() {
    assertEquals(739785, execute(4, 8))
    assertEquals(444356092776315, execute2(4, 8))

    println("Tests passed, attempting input")

    println("Final Result 1: ${execute(7, 10)}")
    println("Final Result 2: ${execute2(7, 10)}")
}

private fun execute(player1Start: Int, player2Start: Int): Long {
    var dice = 0
    var player1Pawn = player1Start
    var player2Pawn = player2Start
    var player1Score = 0
    var player2Score = 0
    var turn = 0

    while (player1Score < 1000 && player2Score < 1000) {
        turn++
        val roll = ++dice + ++dice + ++dice
        if (turn % 2 == 1) {
            player1Pawn = (player1Pawn + roll) % 10
            player1Score += if (player1Pawn == 0) 10 else player1Pawn
        } else {
            player2Pawn = (player2Pawn + roll) % 10
            player2Score += if (player2Pawn == 0) 10 else player2Pawn
        }
    }

    return if (player1Score > player2Score) {
        turn.toLong() * 3 * player2Score
    } else {
        turn.toLong() * 3 * player1Score
    }
}

class BoardState(val player1Pos: Int, val player1Score: Int, val player2Pos: Int, val player2Score: Int, private val player1Turn: Boolean) {
    fun newRoll(roll: Int) : BoardState {
        return if (player1Turn) {
            val newPos = (player1Pos + roll) % 10
            val newScore = if (newPos == 0) player1Score + 10 else player1Score + newPos
            BoardState(newPos, newScore, player2Pos, player2Score, false)
        } else {
            val newPos = (player2Pos + roll) % 10
            val newScore = if (newPos == 0) player2Score + 10 else player2Score + newPos
            BoardState(player1Pos, player1Score, newPos, newScore, true)
        }
    }

    fun checkWinner() : Int {
        return if (player1Score > 20) {
            1
        } else if (player2Score > 20) {
            2
        } else {
            0
        }
    }

}

private fun execute2(player1Start: Int, player2Start: Int): Long {
    //Keep track of BoardState -> in how many universes it's happening
    var boardStates = mapOf(BoardState(player1Start, 0, player2Start, 0, true) to 1L)

    //After adding up the 27 possibilities, each turn this is how many of each score is rolled
    //Total roll -> # of universes it happened in
    val rolls = mapOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)

    var player1Wins = 0L
    var player2Wins = 0L
    while (boardStates.isNotEmpty()) {
        val newBoardStates = mutableMapOf<BoardState, Long>().withDefault { 0L }
        boardStates.forEach { (state, stateOccur) ->
            rolls.forEach { (roll, rollOccur) ->
                val newBoardState = state.newRoll(roll)
                if (newBoardState.checkWinner() == 1) {
                    player1Wins += (stateOccur * rollOccur)
                } else if (newBoardState.checkWinner() == 2) {
                    player2Wins += (stateOccur * rollOccur)
                } else {
                    //Keep playing
                    newBoardStates[newBoardState] = newBoardStates.getValue(newBoardState) + (stateOccur * rollOccur)
                }
            }
        }
        boardStates = newBoardStates
    }

    return if (player1Wins > player2Wins) player1Wins else player2Wins
}