package aoc

import java.io.File

data class Point(val x: Int, val y: Int)
data class Line(val start: Point, val end: Point)

fun Line.points(): List<Point> {
    val yRange = if (start.y < end.y) (start.y..end.y).toMutableList() else (start.y downTo end.y).toMutableList()
    val xRange = if (start.x < end.x) (start.x..end.x).toMutableList() else (start.x downTo end.x).toMutableList()
    val minLength = kotlin.math.max(yRange.size, xRange.size)
    while (xRange.size != minLength) xRange.add(xRange.first())
    while (yRange.size != minLength) yRange.add(yRange.first())
    return (0 until minLength).map { Point(xRange[it], yRange[it]) }
}

fun Line.isHorizontalOrVertical(): Boolean = start.x == end.x || start.y == end.y
operator fun Array<IntArray>.get(point: Point) = this[point.x][point.y]
operator fun Array<IntArray>.set(point: Point, value: Int) { this[point.x][point.y] = value }

fun main() {
    val input = File("input.txt").readLines().map { line ->
        line.split(" -> ").let { points -> Line(
            start = points[0].split(",").let { Point(x = it[0].toInt(), y = it[1].toInt()) },
            end = points[1].split(",").let { Point(x = it[0].toInt(), y = it[1].toInt()) }) }
    }
    when (System.getenv("part")) {"part2" -> println(getSolution(input)) else -> println(getSolution(input.filter { it.isHorizontalOrVertical() })) }
}

fun getSolution(input: List<Line>): Int {
    val maxY = input.maxOf { maxOf(it.start.y, it.end.y) } + 1
    val maxX = input.maxOf { maxOf(it.start.x, it.end.x) } + 1
    val diagram: Array<IntArray> = Array(maxX) { IntArray(maxY) { 0 } }
    for (line in input) { for (point in line.points()) { diagram[point] += 1 } }
    return diagram.flatMap { it.asIterable() }.filter { it >= 2 }.size
}


