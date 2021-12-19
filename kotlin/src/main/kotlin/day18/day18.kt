package day18

import java.io.File
import java.nio.file.Paths
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.test.assertEquals

fun main() {
    val path = Paths.get("").toAbsolutePath().toString()

    //First test that magnitude is calculated correctly
    assertEquals(143, parseNumber("[[1,2],[[3,4],5]]", 0).magnitude())
    assertEquals(1384, parseNumber("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", 0).magnitude())
    assertEquals(445, parseNumber("[[[[1,1],[2,2]],[3,3]],[4,4]]", 0).magnitude())
    assertEquals(791, parseNumber("[[[[3,0],[5,3]],[4,4]],[5,5]]", 0).magnitude())
    assertEquals(1137, parseNumber("[[[[5,0],[7,4]],[5,5]],[6,6]]", 0).magnitude())
    assertEquals(3488, parseNumber("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]", 0).magnitude())

    assertEquals(1384, execute(listOf("[[[[4,3],4],4],[7,[[8,4],9]]]","[1,1]")))
    assertEquals(445, execute(listOf("[1,1]","[2,2]","[3,3]","[4,4]")))
    assertEquals(791, execute(listOf("[1,1]","[2,2]","[3,3]","[4,4]","[5,5]")))
    assertEquals(1137, execute(listOf("[1,1]","[2,2]","[3,3]","[4,4]","[5,5]","[6,6]")))

    val test1 = File("$path\\src\\main\\kotlin\\day18\\testInput.txt").readLines()
    assertEquals(4140, execute(test1))
    assertEquals(3993, execute2(test1))

    println("Tests passed, attempting input")

    val input = File("$path\\src\\main\\kotlin\\day18\\input.txt").readLines()
    //Alternative to read whole file, use .readText()

    println("Final Result 1: ${execute(input)}")
    println("Final Result 2: ${execute2(input)}")
}

open class SnailNumber {
    var parent : SnailNumber? = null

    open fun add(num: SnailNumber): SnailNumber {
        //First make copies so we don't change the original
        //We're always adding together pairs
        val new1 = this.deepCopy() as SnailNumberPair
        val new2 = num.deepCopy() as SnailNumberPair

        //Increment nest level and make a new pair
        new1.incrementNestLevel()
        new2.incrementNestLevel()
        val result = SnailNumberPair(0, new1, new2)

        var changed = true
        while (changed) {
            //This will first try to explode (short-circuiting if true)
            //Otherwise it will try to split
            changed = result.explode() || result.split()
        }

        return result
    }

    open fun magnitude(): Long {
        throw Exception("Method should be overridden")
    }

    open fun split() : Boolean {
        throw Exception("Method should be overridden")
    }

    open fun explode() : Boolean { return false }

    open fun incrementNestLevel() { }

    open fun print() : String { return "" }

    open fun deepCopy() : SnailNumber { return SnailNumber() }
}

class SnailNumberLiteral(var amount: Int) : SnailNumber() {
    override fun magnitude(): Long {
        return amount.toLong()
    }

    override fun split() : Boolean {
        return if (amount > 9) {
            val left = SnailNumberLiteral(floor(amount.toDouble()/2).toInt())
            val right = SnailNumberLiteral(ceil(amount.toDouble()/2).toInt())

            //We need to replace ourselves with a pair
            //First determine if we're the left or right of the parent, which will always be a pair
            val pairParent = parent as SnailNumberPair
            if (pairParent.left == this) {
                pairParent.left = SnailNumberPair(pairParent.nestLevel + 1, left, right)
            } else {
                pairParent.right = SnailNumberPair(pairParent.nestLevel + 1, left, right)
            }
            true
        } else {
            false
        }
    }

    override fun print() : String { return amount.toString() }

    override fun deepCopy(): SnailNumber {
        return SnailNumberLiteral(amount)
    }
}

class SnailNumberPair(var nestLevel: Int, left: SnailNumber, right: SnailNumber): SnailNumber() {
    var left: SnailNumber = left
        set(value) {
            field = value
            field.parent = this
        }
    var right: SnailNumber = right
        set(value) {
            field = value
            field.parent = this
        }
    init {
        //Whenever we make a new pair, fix the parent reference of the children
        this.left.parent = this
        this.right.parent = this
    }

    override fun magnitude(): Long {
        return left.magnitude() * 3 + right.magnitude() * 2
    }

    override fun split(): Boolean {
        return left.split() || right.split()
    }

    override fun explode() : Boolean {
        return if (nestLevel > 3) {
            //First we have to see if there are deeper ones to explode
            if (this.left is SnailNumberPair) {
                this.left.explode()
            } else if (this.right is SnailNumberPair) {
                this.right.explode()
            } else {
                //First thing, figure out of this node is the left or right node of its parent
                if ((parent as SnailNumberPair).left == this) {
                    //Travel down the left of that right node to update the value
                    var rightChild = (parent as SnailNumberPair).right
                    while (rightChild !is SnailNumberLiteral) {
                        rightChild = (rightChild as SnailNumberPair).left
                    }
                    rightChild.amount += (this.right as SnailNumberLiteral).amount

                    //Check if there's anything to our left
                    var nextParent = this.parent
                    var lastLeft = this
                    while (nextParent != null) {
                        if ((nextParent as SnailNumberPair).left != lastLeft) {
                            //Found something, now go back down to find the literal we need to change
                            var leftChild = nextParent.left
                            while (leftChild !is SnailNumberLiteral) {
                                leftChild = (leftChild as SnailNumberPair).right
                            }
                            leftChild.amount += (this.left as SnailNumberLiteral).amount
                            break
                        } else {
                            lastLeft = nextParent
                            nextParent = nextParent.parent
                        }
                    }

                    //Finally, parent's left node with a literal of zero
                    (parent as SnailNumberPair).left = SnailNumberLiteral(0)
                } else {
                    //Travel down the right of that left node to update the value
                    var leftChild = (parent as SnailNumberPair).left
                    while (leftChild !is SnailNumberLiteral) {
                        leftChild = (leftChild as SnailNumberPair).right
                    }
                    leftChild.amount += (this.left as SnailNumberLiteral).amount

                    //check if there's anything to our right
                    var nextParent = this.parent
                    var lastRight = this
                    while (nextParent != null) {
                        if ((nextParent as SnailNumberPair).right != lastRight) {
                            //Found something, now go back down to find the literal we need to change
                            var rightChild = nextParent.right
                            while (rightChild !is SnailNumberLiteral) {
                                rightChild = (rightChild as SnailNumberPair).left
                            }
                            rightChild.amount += (this.right as SnailNumberLiteral).amount
                            break
                        } else {
                            lastRight = nextParent
                            nextParent = nextParent.parent
                        }
                    }

                    //Finally, replace parent's right node with literal of zero
                    (parent as SnailNumberPair).right = SnailNumberLiteral(0)
                }

                true
            }
        } else {
            left.explode() || right.explode()
        }
    }

    override fun incrementNestLevel() {
        nestLevel++
        left.incrementNestLevel()
        right.incrementNestLevel()
    }

    override fun print() : String {
        return "[${left.print()},${right.print()}]"
    }

    override fun deepCopy(): SnailNumber {
        return SnailNumberPair(nestLevel, left.deepCopy(), right.deepCopy())
    }
}

private fun parseNumber(num: String, nestLevel: Int): SnailNumber {
    //First strip away the outer []
    val s = num.substring(1, num.length-1)
    return if (s.length == 3) {
        //#,# -> two literals
        val left = SnailNumberLiteral(Character.getNumericValue(s.first()))
        val right = SnailNumberLiteral(Character.getNumericValue(s.last()))
        SnailNumberPair(nestLevel, left, right)
    } else if (s.first() != '[') {
        //#,[...] -> left literal, parse right
        val left = SnailNumberLiteral(Character.getNumericValue(s.first()))
        val right = parseNumber(s.substring(2), nestLevel + 1)
        SnailNumberPair(nestLevel, left, right)
    } else if (s.last() != ']') {
        //[...],# -> parse left, right literal
        val left = parseNumber(s.substring(0, s.length-2), nestLevel + 1)
        val right = SnailNumberLiteral(Character.getNumericValue(s.last()))
        SnailNumberPair(nestLevel, left, right)
    } else {
        //[...],[...] -> parse both
        //Need to find the comman in the center to split
        var leftBracketCount = 0
        var split = 0
        for (i in s.indices) {
            if (s[i] == '[') leftBracketCount++
            else if (s[i] == ']') {
                leftBracketCount--
                if (leftBracketCount == 0) {
                    split = i+1
                    break
                }
            }
        }
        val left = s.substring(0,split)
        val right = s.substring(split+1)
        SnailNumberPair(nestLevel, parseNumber(left, nestLevel + 1), parseNumber(right, nestLevel + 1))
    }
}

private fun execute(input: List<String>): Long {
    return input.map { parseNumber(it, 0) }.reduce{ acc, number -> acc.add(number) }.magnitude()
}

private fun execute2(input: List<String>): Long {
    val snailNumbers = input.map { parseNumber(it, 0) }
    var maxMagnitude = 0L
    //Try every combination
    snailNumbers.forEachIndexed { i, num ->
        val newList = snailNumbers.toMutableList()
        newList.remove(num)
        newList.forEach { testNum ->
            //First try using the number as the first number
            val magnitude = num.add(testNum).magnitude()
            if (magnitude > maxMagnitude) maxMagnitude = magnitude
            //Then as the second number
            val magnitude2 = testNum.add(num).magnitude()
            if (magnitude2 > maxMagnitude) maxMagnitude = magnitude2
        }
    }

    return maxMagnitude
}