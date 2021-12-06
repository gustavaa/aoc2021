package aoc

import java.io.File

fun main() {
    val input = File("input.txt").readLines().flatMap { it.split(",").map { it.toInt() } }
    when (System.getenv("part")) {"part2" -> println(getSolution(input, 256))else -> println(getSolution(input, 80)) }
}
const val SLOT_SIZE = 10
fun getSolution(input: List<Int>, daysToSimulate: Int): Long {
    val population = mutableMapOf<Int, Long>()
    for (timer in input) population.merge(timer, 1, Long::plus)
    (0..daysToSimulate).forEach { day ->
        val birthSlot = (day + SLOT_SIZE - 1) % SLOT_SIZE
        val slotPopulation = population.remove(birthSlot) ?: 0
        population.merge((birthSlot + SLOT_SIZE - 1) % SLOT_SIZE, slotPopulation, Long::plus)
        population.merge((birthSlot + SLOT_SIZE - 3) % SLOT_SIZE, slotPopulation, Long::plus) }
    return population.values.sum()
}


