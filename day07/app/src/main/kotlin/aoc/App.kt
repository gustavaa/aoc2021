package aoc

import java.io.File
import kotlin.math.abs

fun main() = File("input.txt").readLines().flatMap { it.split(",").map { it.toInt() } }.let { input ->
    when (System.getenv("part")) {
        "part2" -> println(((input.minOrNull() ?: 0)..(input.maxOrNull() ?: 0)).toList().minOf { pos -> input.sumOf { abs(it - pos) * (abs(it - pos) + 1) / 2 } })
        else -> println(((input.minOrNull() ?: 0)..(input.maxOrNull() ?: 0)).toList().minOf { pos -> input.sumOf { abs(pos - it) } }) } }