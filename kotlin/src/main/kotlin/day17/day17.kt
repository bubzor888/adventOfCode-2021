package day17

import kotlin.math.abs
import kotlin.test.assertEquals

fun main() {
    // target area: x=20..30, y=-10..-5
    assertEquals(45, execute(20..30, -10..-5))
    assertEquals(112, execute2(20..30, -10..-5))
    println("Tests passed, attempting input")

    //target area: x=269..292, y=-68..-44"
    println("Final Result 1: ${execute(269..292,-68..-44)}")
    println("Final Result 2: ${execute2(269..292,-68..-44)}")

}


enum class ShotResult { HIT, UNDETERMINED, X_SHORT, X_LONG, Y_LONG }
class Shot(val shotResult: ShotResult, val yHeight: Long)

private fun checkResult(pos: Pair<Long, Long>, vel: Pair<Int, Int>, xTarget: IntRange, yTarget: IntRange): ShotResult {
    return if (pos.first in xTarget && pos.second in yTarget) {
        ShotResult.HIT
    } else if (vel.first == 0) {
        return if (pos.first < xTarget.first) {
            ShotResult.X_SHORT
        } else if(pos.first > xTarget.last) {
            ShotResult.X_LONG
        } else if (pos.second < yTarget.first) {
            ShotResult.Y_LONG
        } else {
            ShotResult.UNDETERMINED
        }
    } else if (pos.first > xTarget.last) {
        ShotResult.X_LONG
    } else if (pos.second < yTarget.first) {
        ShotResult.Y_LONG
    } else {
        ShotResult.UNDETERMINED
    }
}

private fun takeShot(startPos: Pair<Long, Long>, vel: Pair<Int, Int>, xTarget: IntRange, yTarget: IntRange) : Shot {
    var pos = startPos
    var xVol = vel.first
    var yVol = vel.second
    var maxY = 0L

    var result = checkResult(pos, Pair(xVol, yVol), xTarget, yTarget)
    while (result == ShotResult.UNDETERMINED) {
//        println("(${pos.first},${pos.second})")

        //Move the position
        pos = Pair(pos.first + xVol, pos.second + yVol)
        if (pos.second > maxY) maxY = pos.second

        //Modify the velocity
        if (xVol > 0) xVol--
        else if (xVol < 0) xVol++
        yVol--

        result = checkResult(pos, Pair(xVol, yVol), xTarget, yTarget)
    }
//    println("(${vel.first},${vel.second}) -> : $result")
    return Shot(result, maxY)
}

private fun execute(xTarget: IntRange, yTarget: IntRange): Long {
    var yVelBase = abs(yTarget.first - yTarget.last) * 100
    var xVel = 1
    var yVel = yVelBase
    var result = takeShot(Pair(0, 0), Pair(xVel, yVel), xTarget, yTarget)
    while (result.shotResult != ShotResult.HIT) {
        when (result.shotResult) {
            ShotResult.X_SHORT -> {
                xVel++
                yVel = yVelBase
            }
            ShotResult.X_LONG -> {
                xVel = 1
                yVel = --yVelBase
            }
            ShotResult.Y_LONG -> {
                xVel = 1
                yVel = --yVelBase
            }
        }
        result = takeShot(Pair(0, 0), Pair(xVel, yVel), xTarget, yTarget)
    }

    return result.yHeight
}

private fun execute2(xTarget: IntRange, yTarget: IntRange): Int {
    var xVelMax = abs(xTarget.last - xTarget.first) * 50
    var yVelMax = abs(yTarget.first - yTarget.last) * 50
//    var yVelMax = 10
    var hits = 0L
    var hitList = mutableSetOf<String>()
    for (x in 0..xVelMax) {
        for (y in yVelMax * -1..yVelMax) {
            if (takeShot(Pair(0, 0), Pair(x, y), xTarget, yTarget).shotResult == ShotResult.HIT) {
                hits++
                hitList.add("($x,$y)")
            }
        }
    }

    return hitList.size
}