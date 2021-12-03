package aoc

import java.io.File

fun main() {
    val input = File("input.txt").readLines().map { it.toCharArray() }
    when (System.getenv("part")) {"part2" -> println(solutionPart2(input))else -> println(solutionPart1(input)) }
}

fun solutionPart1(input: List<CharArray>): Int {
    val gammaRate = CharArray(input.first().size); val epsilon = CharArray(input.first().size)
    (input.first().indices).forEach { bit ->
        gammaRate[bit] = if (input.count { it[bit] == '0' } > input.size / 2) '0' else '1'
        epsilon[bit] = if (gammaRate[bit] == '0') '1' else '0' }
    return Integer.parseInt(gammaRate.joinToString(""), 2) * Integer.parseInt(epsilon.joinToString(""), 2)
}

fun solutionPart2(input: List<CharArray>): Int {
    val oxygenRatingCandidates = input.toMutableList(); val co2ScrubberCandidates = input.toMutableList()
    (input.first().indices).forEach { bit ->
        if (oxygenRatingCandidates.size > 1) filterCandidates('0', false, oxygenRatingCandidates, bit)
        if (co2ScrubberCandidates.size > 1) filterCandidates('1', true, co2ScrubberCandidates, bit) }
    return Integer.parseInt(oxygenRatingCandidates.first().joinToString(""), 2) * Integer.parseInt(co2ScrubberCandidates.first().joinToString(""), 2)
}

fun filterCandidates(charToKeepIfMcbEqual: Char, keepMcb: Boolean, list: MutableList<CharArray>, bit: Int){
    val zeros = list.count { it[bit] == '0' }
    val mcb = when { zeros > list.size / 2 -> '0'; zeros == list.size / 2 -> '-' else -> '1' }
    if (mcb == '-') list.removeIf { it[bit] == charToKeepIfMcbEqual } else list.removeIf { if (keepMcb) it[bit] == mcb else it[bit] != mcb}
}