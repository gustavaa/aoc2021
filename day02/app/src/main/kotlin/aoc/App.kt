package aoc

import java.io.File

data class Command(val command: String, val units: Int)
fun main() {
    val input = File("input.txt").readLines().map { Command(it.split(" ")[0], it.split(" ")[1].toInt()) }
    when (System.getenv("part")) {"part2" -> println(solutionPart2(input)) else -> println(solutionPart1(input)) }
}

fun solutionPart1(input: List<Command>): Int {
    var depth = 0; var horizontalPos = 0
    input.forEach { when (it.command) {"forward" -> horizontalPos += it.units; "down" -> depth += it.units; "up" -> depth -= it.units } }
    return depth * horizontalPos
}

fun solutionPart2(input: List<Command>): Int {
    var depth = 0; var horizontalPos = 0; var aim = 0
    input.forEach { when (it.command) {"forward" -> { horizontalPos += it.units; depth += aim * it.units; } "down" -> aim += it.units; "up" -> aim -= it.units } }
    return depth * horizontalPos
}