package aoc

import java.io.File

fun main() {
    var startingPoint = ""
    val pairSubstitutionMap = mutableMapOf<String, String>()
    for (line in File("input.txt").readLines()) {
        when {
            line.isBlank() -> continue
            '>' in line -> line.split("->").let {pairSubstitutionMap[it[0].trim()] = it[1].trim()}
            else -> startingPoint = line.trim()
        }
    }
    when (System.getenv("part")) {"part2" -> println(getSolution(startingPoint, pairSubstitutionMap, 40)) else -> println(getSolution(startingPoint, pairSubstitutionMap, 10)) }
}

fun getSolution(startingPoint: String, substitutionMap: Map<String, String>, steps: Int): Long {
    var pairCount = mutableMapOf<String, Long>()

    startingPoint.indices.forEach { index ->
        if (index + 2 > startingPoint.length) return@forEach
        startingPoint.substring(index, index+2).let { pair -> pairCount.merge(pair, 1, Long::plus) }
    }

    repeat((1..steps).count()) {
        val newPairs = mutableMapOf<String, Long>() // Copy
        substitutionMap.forEach { (pair, subst) ->
            val currentPairCount = pairCount[pair] ?: return@forEach
            val newPair1 = pair[0] + subst
            val newPair2 = subst + pair[1]
            newPairs.merge(newPair1, currentPairCount, Long::plus)
            newPairs.merge(newPair2, currentPairCount, Long::plus)
        }
        pairCount = newPairs
    }
    val letterCount = mutableMapOf<Char, Long>()
    pairCount.forEach { (pair, count) -> letterCount.merge(pair[1], count, Long::plus) }
    return letterCount.values.maxOrNull()!! - letterCount.values.minOrNull()!!
}