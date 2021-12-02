package aoc

import java.io.File

fun main() { val input = File("input.txt").readLines().map { it.toInt() }; when (System.getenv("part")) {"part2" -> println(solutionPart2(input)) else -> println(solutionPart1(input))}}
fun solutionPart1(input: List<Int>): Int = input.foldIndexed(0) { index, acc, number -> if (index > 0 && input[index - 1] < number) acc + 1 else acc }
const val SLIDING_WINDOW_SIZE = 3
fun solutionPart2(input: List<Int>): Int { var answer = 0; val windowSums = mutableListOf<Int>(); input.indices.forEach { index -> if (index < input.size - SLIDING_WINDOW_SIZE + 1) { input.subList(index, index + SLIDING_WINDOW_SIZE).sum().let { windowSums.add(it) }; if (index > 0 && windowSums[index - 1] < windowSums[index]) answer++ } }; return answer }
