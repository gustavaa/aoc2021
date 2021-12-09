package aoc

import java.io.File

fun main() {
    val input = File("input.txt").readLines().map { it.split("").filter { it.isNotEmpty() }.map { it.toInt() }.toTypedArray() }.toTypedArray()
    when (System.getenv("part")) {"part2" -> println(getSolutionPart2(input))else -> println(getSolutionPart1(input)) }
}

data class Point(val x: Int, val y: Int)
operator fun Array<Array<Int>>.get(point: Point) = this[point.x][point.y]
operator fun Array<Array<Int>>.set(point: Point, value: Int) {
    this[point.x][point.y] = value
}

fun getSolutionPart1(input: Array<Array<Int>>): Int = getLowPoints(input).map { input[it] + 1 }.sum()
fun getSolutionPart2(input: Array<Array<Int>>): Int = getLowPoints(input).map { getBasins(it, input) }.sortedByDescending { it.size }.subList(0,3).fold(1) { acc, set -> acc * set.size }

fun getLowPoints(input: Array<Array<Int>>): List<Point> = input.flatMapIndexed { row, values -> values.mapIndexedNotNull { col, height -> if (getAdjacentPoints(Point(row, col), input).none { input[it] <= height }) Point(row, col) else null } }

fun getBasins(point: Point, map: Array<Array<Int>>, basins: MutableSet<Point> = mutableSetOf()): Set<Point> {
    if (map[point] == 9) return basins
    basins.add(point)
    getAdjacentPoints(point, map).forEach { adjacent -> if (map[adjacent] > map[point]) basins.addAll(getBasins(adjacent, map, basins)) }
    return basins
}

fun getAdjacentPoints(point: Point, map: Array<Array<Int>>): List<Point> {
    val adjacentPoints = mutableListOf<Point>()
    if (point.x > 0) adjacentPoints.add(Point(point.x - 1, point.y))
    if (point.y > 0) adjacentPoints.add(Point(point.x, point.y - 1))
    if (point.x < map.size - 1) adjacentPoints.add(Point(point.x + 1, point.y))
    if (point.y < map.first().size - 1) adjacentPoints.add(Point(point.x, point.y + 1))
    return adjacentPoints
}