package aoc

import java.io.File
import java.util.*

val openingChars = setOf('(', '[', '{', '<')
val closingChars = setOf(')', ']', '}', '>')
val closingCharMap = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')

fun main() {
    val input = File("input.txt").readLines()
    when (System.getenv("part")) {"part2" -> println(getSolutionPart2(input)) else -> println(getSolutionPart1(input))}
}

fun getSolutionPart1(input: List<String>): Int {
val scoreMap = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    var score = 0
    for (line in input) {
        val openingCharStack = Stack<Char>()
        for (char in line) {
            when {
                openingChars.contains(char) -> openingCharStack.add(char)
                closingChars.contains(char) -> {
                    if (closingCharMap[openingCharStack.pop()] != char) {
                        score += scoreMap[char]!!
                        break
                    }
                }
            }
        }
    }
    return score
}

fun getSolutionPart2(input: List<String>): Long {
    val scores = mutableListOf<Long>()
    for (line in input) {
        val openingCharStack = Stack<Char>()
        var corrupted = false
        for (char in line) {
            when {
                openingChars.contains(char) -> openingCharStack.add(char)
                closingChars.contains(char) -> {
                    if (closingCharMap[openingCharStack.pop()] != char) {
                        corrupted = true
                        break
                    }
                }
            }
        }
        if (openingCharStack.isNotEmpty() && !corrupted) {
            var score = 0L
            while (openingCharStack.isNotEmpty()) {
                when(closingCharMap[openingCharStack.pop()]){
                    ')' -> score = score * 5 + 1
                    ']' -> score = score * 5 + 2
                    '}' -> score = score * 5 + 3
                    '>' -> score = score * 5 + 4
                }
            }
            scores.add(score)
        }
    }
    return scores.sorted().let { it[it.size/2] }
}
