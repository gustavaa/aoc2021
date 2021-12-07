package aoc

import java.io.File
import kotlin.math.abs

fun main() {
    val input = File("input.txt").readLines().flatMap { it.split(",").map { it.toInt() } }
    when (System.getenv("part")) {"part2" -> println(getSolution2(input))else -> println(getSolution1(input)) }
}

fun getSolution1(input: List<Int>): Int {
    val min = input.minOrNull() ?: 0; val max = input.maxOrNull() ?: 0
    return (min..max).toList().minOf { pos -> input.sumOf { abs(pos - it) } }
}

fun getSolution2(input: List<Int>): Int {
    val min = input.minOrNull() ?: 0; val max = input.maxOrNull() ?: 0
    return (min..max).toList().minOf { pos -> input.sumOf { abs(it - pos) * (abs(it-pos) + 1)/2 } }
}


