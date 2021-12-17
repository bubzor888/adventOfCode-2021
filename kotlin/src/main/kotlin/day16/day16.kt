package day16

import java.io.File
import java.nio.file.Paths
import kotlin.math.pow
import kotlin.test.assertEquals

fun main() {
    val path = Paths.get("").toAbsolutePath().toString()

    assertEquals(16, execute("8A004A801A8002F478"))
    assertEquals(12, execute("620080001611562C8802118E34"))
    assertEquals(23, execute("C0015000016115A2E0802F182340"))
    assertEquals(31, execute("A0016C880162017C3686B18A3D4780"))

    println("Tests passed, attempting result 1")

    val input = File("$path\\src\\main\\kotlin\\day16\\input.txt").readText()

    println("Final Result 1: ${execute(input)}")

    assertEquals(3, execute2("C200B40A82"))
    assertEquals(54, execute2("04005AC33890"))
    assertEquals(7, execute2("880086C3E88112"))
    assertEquals(9, execute2("CE00C43D881120"))
    assertEquals(1, execute2("D8005AC2A8F0"))
    assertEquals(0, execute2("F600BC2D8F"))
    assertEquals(0, execute2("9C005AC2F8F0"))
    assertEquals(1, execute2("9C0141080250320F1802104A08"))

    println("Tests passed, attempting result 2")
    println("Final Result 2: ${execute2(input)}")
}

open class Packet(private val version: Int, val typeId: Int, val length: Int) {
    open fun totalVersion(): Int {
        return version
    }

    open fun totalValue(): Long { return 0L }
}
class LiteralPacket(version: Int, typeId: Int, length: Int, private val literal: Long) : Packet(version, typeId, length) {
    override fun totalValue(): Long {
        return literal
    }
}

class OperatorPacket(version: Int, typeId: Int, length: Int, private val subPackets: List<Packet>) : Packet(version, typeId, length) {
    override fun totalVersion(): Int {
        return super.totalVersion() + subPackets.fold(0) { acc, packet -> acc + packet.totalVersion() }
    }

    override fun totalValue(): Long {
        return when (typeId) {
            0 -> subPackets.fold(0L) { acc, packet -> acc + packet.totalValue() }
            1 -> subPackets.fold(1L) { acc, packet -> acc * packet.totalValue() }
            2 -> subPackets.fold(Long.MAX_VALUE) { min, packet ->
                if (packet.totalValue() < min) packet.totalValue()
                else min
            }
            3 -> subPackets.fold(0L) { max, packet ->
                if (packet.totalValue() > max) packet.totalValue()
                else max
            }
            5 -> if (subPackets[0].totalValue() > subPackets[1].totalValue()) 1 else 0
            6 -> if (subPackets[0].totalValue() < subPackets[1].totalValue()) 1 else 0
            7 -> if (subPackets[0].totalValue() == subPackets[1].totalValue()) 1 else 0
            else -> throw Exception("Id Type $typeId not valid")
        }
    }
}

fun toDecimal(num: String): Long {
    return num.reversed().foldIndexed(0L) {
        index, sum, bit -> sum + Character.getNumericValue(bit) * (2.0).pow(index).toLong()
    }
}

fun processPacket(message: String): Packet {
    var pointer = 0
    //First 3 are version
    val version = toDecimal(message.substring(pointer, 3)).toInt()
    pointer += 3
    //Next 3 are typeId
    val typeId = toDecimal(message.substring(pointer, pointer + 3)).toInt()
    pointer += 3

    return when (typeId) {
        4 -> {
            //literal value
            var group = message.substring(pointer, pointer + 5)
            var literal = group.substring(1)
            pointer += 5
            while (group[0] != '0') {
                group = message.substring(pointer, pointer + 5)
                literal += group.substring(1)
                pointer += 5
            }

            //Return the packet
            LiteralPacket(version, typeId, pointer, toDecimal(literal))
        }
        else -> {
            //Operator, check which type
            val lengthTypeId = message[pointer]
            pointer += 1
            when (lengthTypeId) {
                '0' -> {
                    val subPacketLength = toDecimal(message.substring(pointer, pointer + 15))
                    pointer += 15
                    val subPacketEnd = pointer + subPacketLength
                    val subPackets = mutableListOf<Packet>()
                    while (pointer < subPacketEnd) {
                        subPackets.add(processPacket(message.substring(pointer)))
                        pointer += subPackets.last().length
                    }

                    //Return the packet
                    OperatorPacket(version, typeId, pointer, subPackets)
                }
                '1' -> {
                    val numPackets = toDecimal(message.substring(pointer, pointer + 11))
                    pointer += 11
                    val subPackets = mutableListOf<Packet>()
                    for (i in 0 until numPackets) {
                        subPackets.add(processPacket(message.substring(pointer)))
                        pointer += subPackets.last().length
                    }

                    //Return the packet
                    OperatorPacket(version, typeId, pointer, subPackets)
                }
                else -> throw Exception("Unrecognized lengthTypeId: $lengthTypeId")
            }
        }
    }
}

private fun execute(input: String): Int {
    //First explode the hex out to binary
    val message = input.fold("") { acc, ch ->
        acc + Integer.toBinaryString(ch.toString().toInt(16)).padStart(4, '0')
    }

    return processPacket(message).totalVersion()
}

private fun execute2(input: String): Long {
    //First explode the hex out to binary
    val message = input.fold("") { acc, ch ->
        acc + Integer.toBinaryString(ch.toString().toInt(16)).padStart(4, '0')
    }

    return processPacket(message).totalValue()
}