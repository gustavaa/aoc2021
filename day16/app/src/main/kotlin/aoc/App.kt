package aoc

import java.io.File
import java.lang.UnsupportedOperationException

fun main() {
    val binaryInput = File("input.txt").readLines().mapInput()
    when (System.getenv("part")) {
        "part2" -> println(getSolutionPart2(binaryInput))
        else -> println(getSolutionPart1(binaryInput))
    }
}

enum class LengthTypeId(val associatedChar: Char, val lengthBitCount: Int) {
    TOTAL_LENGTH('0', 15),
    PACKET_COUNT('1', 11)
}

open class Packet(val type: Int, val version: Int, val lengthTypeId: LengthTypeId?, val length: Int, val body: String) {
    open fun executeCalculation(): Long = throw UnsupportedOperationException()
    open val versionSum: Int = version
}

class LiteralPacket(packet: Packet, private val value: Long) : Packet(packet.type, packet.version, packet.lengthTypeId, packet.length, packet.body) {
    override fun executeCalculation(): Long = value
}

class OperatorPacket(packet: Packet, private val subPackets: MutableList<Packet>) : Packet(packet.type, packet.version, packet.lengthTypeId, packet.length, packet.body) {
    override val versionSum: Int = subPackets.sumOf { it.versionSum } + version
    override fun executeCalculation(): Long {
        return when (type) {
            0 -> subPackets.sumOf { it.executeCalculation() }
            1 -> subPackets.fold(1) { acc, packet -> acc * packet.executeCalculation() }
            2 -> subPackets.minOf { it.executeCalculation() }
            3 -> subPackets.maxOf { it.executeCalculation() }
            5 -> if (subPackets[0].executeCalculation() > subPackets[1].executeCalculation()) 1 else 0
            6 -> if (subPackets[0].executeCalculation() < subPackets[1].executeCalculation()) 1 else 0
            7 -> if (subPackets[0].executeCalculation() == subPackets[1].executeCalculation()) 1 else 0
            else -> throw UnsupportedOperationException()
        }
    }
}
data class ParseResult(val packet: Packet, val residualBody: String)

fun List<String>.mapInput(): String = this.first().map { String.format("%4s", it.digitToInt(16).toString(2)).replace(' ', '0') }.joinToString("")

fun getVersion(packet: String): Int = packet.substring(0, 3).toInt(2)
fun getType(packet: String): Int = packet.substring(0, 3).toInt(2)

fun getLengthTypeId(packet: String): LengthTypeId = LengthTypeId.values().find { it.associatedChar == packet[0] }!!

fun parseRawInput(packetInput: String): Packet {
    var packetBody = packetInput
    val version = getVersion(packetBody)
    packetBody = packetBody.drop(3)
    val type: Int = getType(packetBody)
    packetBody = packetBody.drop(3)
    return when (type) {
        4 -> Packet(type, version, null, -1, packetBody)
        else -> {
            val lengthTypeId = getLengthTypeId(packetBody)
            packetBody = packetBody.drop(1)
            val bodyLength = packetBody.substring(0, lengthTypeId.lengthBitCount).toInt(2)
            packetBody = packetBody.drop(lengthTypeId.lengthBitCount)
            Packet(type, version, lengthTypeId, bodyLength, packetBody)
        }
    }
}

fun parsePacket(packet: Packet): ParseResult = when (packet.type) {
    4 -> parseLiteralValue(packet)
    else -> parseOperatorPacket(packet)
}

fun parseOperatorPacket(packet: Packet): ParseResult {
    val subPackets = mutableListOf<Packet>()
    var unParsedBody = packet.body
    when (packet.lengthTypeId) {
        LengthTypeId.PACKET_COUNT -> {
            val subPacketCount = packet.length
            repeat((0 until subPacketCount).count()) {
                val subPacketToParse = parseRawInput(unParsedBody)
                parsePacket(subPacketToParse).let { (packet, residual) ->
                    subPackets.add(packet)
                    unParsedBody = residual
                }
            }
        }
        LengthTypeId.TOTAL_LENGTH -> {
            val targetLength = packet.length
            var unParsedPackets = unParsedBody.substring(0, targetLength)
            while (!unParsedPackets.all { it == '0' }) {
                val subPacketToParse = parseRawInput(unParsedPackets)
                parsePacket(subPacketToParse).let { (packet, residual) ->
                    subPackets.add(packet)
                    unParsedPackets = residual
                }
            }
            unParsedBody = unParsedBody.drop(targetLength)

        }
    }
    return ParseResult(OperatorPacket(packet, subPackets), unParsedBody)
}

fun parseLiteralValue(packet: Packet): ParseResult {
    var lastGroup = false
    var literal = ""
    var body = packet.body
    while (!lastGroup) {
        val group = body.substring(0, 5)
        lastGroup = group.startsWith('0')
        literal += group.drop(1)
        body = body.drop(5)
    }
    return ParseResult(LiteralPacket(packet, literal.toLong(2)), body)
}

fun getSolutionPart1(input: String): Int = parsePacket(parseRawInput(input)).packet.versionSum

fun getSolutionPart2(input: String): Long = parsePacket(parseRawInput(input)).packet.executeCalculation()