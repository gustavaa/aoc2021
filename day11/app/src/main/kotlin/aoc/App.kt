package aoc

import java.io.File

fun main() {
    val input = File("input.txt").readLines().map { it.map(Character::getNumericValue).toTypedArray() }.toTypedArray()
    when (System.getenv("part")) {"part2" -> println(simulate(input, 1000, true)) else -> println(simulate(input, 100)) }
}

data class Point(val x: Int, val y: Int)
operator fun Array<Array<Int>>.get(point: Point) = this[point.x][point.y]
operator fun Array<Array<Int>>.set(point: Point, value: Int) { this[point.x][point.y] = value }

fun simulate(input: Array<Array<Int>>, stepsToSimulate: Int, breakOnSynchronized: Boolean = false): Int {
    var totalFlashes = 0
    for (step in 1..stepsToSimulate) {
        val flashed = mutableSetOf<Point>()
        (input.indices).forEach { row -> (input[0].indices).forEach { col -> if (++input[row][col] > 9 && !flashed.contains(Point(row, col))) flash(Point(row, col), input, flashed) } }
        totalFlashes += flashed.size
        for (point in flashed) input[point] = 0
        if (breakOnSynchronized && input.flatMap { it.toList() }.all { it == 0 }) { return step }
    }
    return totalFlashes
}

fun flash(point: Point, map: Array<Array<Int>>, flashed: MutableSet<Point>) {
    flashed.add(point)
    for (adjacent in point.adjacent(map)) { if (++map[adjacent] > 9 && !flashed.contains(adjacent)) flash(adjacent, map, flashed) }
}

fun Point.adjacent(map: Array<Array<Int>>): List<Point> {
    val adjacentPoints = mutableSetOf<Point>()
    for (row in -1..1) { for (col in -1..1) { if (row != 0 || col != 0) adjacentPoints.add(Point(this.x + row, this.y + col)) } }
    return adjacentPoints.filter { it.x >= 0 && it.y >= 0 && it.x < map.size && it.y < map[0].size }
}