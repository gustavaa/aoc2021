package aoc

import java.io.File
import kotlin.math.abs

data class Input(val signalPatterns: List<String>, val digitOutputValues: List<String>)

fun main() {
    val input = File("input.txt").readLines().map { it.split("|").let { Input(it[0].split(" "), it[1].split(" ")) } }
    when (System.getenv("part")) {"part2" -> println(getSolutionPart1(input)) else -> println(getSolutionPart2(input)) }
}

val segmentCountToDigitMap = mapOf( 2 to 1, 4 to 4, 3 to 7, 7 to 8)
fun getSolutionPart1(input: List<Input>): Int {
    return input.fold(0) { acc, input ->
        acc + input.digitOutputValues.count { segmentCountToDigitMap.containsKey(it.trim().length)}
    }
}

fun getSolutionPart2(input: List<Input>): Int {
    var total = 0
    for (inputLine in input) {
        val segments = Array(10){""}
        while (segments.any { it == "" }) {
            for (signalPattern in inputLine.signalPatterns) {
                println(signalPattern)
                when(signalPattern.length) {
                    2 -> segments[1] = signalPattern.toSortedSet().toString()
                    3 -> segments[7] = signalPattern.toSortedSet().toString()
                    4 -> segments[4] = signalPattern.toSortedSet().toString()
                    7 -> segments[8] = signalPattern.toSortedSet().toString()
                    5 -> {
                        val fourSevenJoined = segments[4].toSortedSet().union(segments[7].toSortedSet())
                        when {
                            signalPattern.toSortedSet().intersect(segments[1].toSortedSet()).size == 2 -> segments[3] = signalPattern.toSortedSet().toString()
                            signalPattern.toSortedSet().intersect(fourSevenJoined).size == 3 -> segments[2] = signalPattern.toSortedSet().toString()
                            signalPattern.toSortedSet().intersect(fourSevenJoined).size == 4 -> segments[5] = signalPattern.toSortedSet().toString()
                        }
                    }
                    6 -> {
                        when {
                            signalPattern.toSortedSet().intersect(segments[1].toSortedSet()).size == 1 -> segments[6] = signalPattern.toSortedSet().toString()
                            signalPattern.toSortedSet().intersect(segments[4].toSortedSet()).size == 4 -> segments[9] = signalPattern.toSortedSet().toString()
                            signalPattern.toSortedSet().intersect(segments[4].toSortedSet()).size == 3 -> segments[0] = signalPattern.toSortedSet().toString()
                        }
                    }
                }
            }
        }
        val signalToDigitMap = mutableMapOf<String, Int>()
        segments.forEachIndexed { index, s -> signalToDigitMap[s] = index }
        total += inputLine.digitOutputValues.map { signalToDigitMap[it.toSortedSet().toString()]?.toString() ?: "" }.joinToString("").toInt()
    }
    return total
}