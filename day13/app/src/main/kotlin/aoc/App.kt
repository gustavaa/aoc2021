package aoc

import java.io.File

data class Coordinate(var x: Int, var y: Int)
enum class Axis{X, Y;}
data class Fold(val axis: Axis, val line: Int)
typealias Paper = Array<Array<Char>>
fun Paper.print() = forEach { println(it.joinToString("")) }

fun main() {
    val folds = mutableSetOf<Fold>()
    val initialDotCoordinates = mutableSetOf<Coordinate>()
    for (line in File("input.txt").readLines()) {
        when {
            line.isBlank() -> continue
            ',' in line -> line.split(",").let { coordinates -> initialDotCoordinates.add(Coordinate(coordinates[0].toInt(), coordinates[1].toInt())) }
            else -> line.split(" ")[2].split("=").let { folds.add(Fold(if (it[0] == "y") Axis.Y else Axis.X, it[1].toInt())) }
        }
    }
    when (System.getenv("part")) {"part2" -> printCode(folds, initialDotCoordinates) else -> println(fold(folds.first(), initialDotCoordinates).size) }
}

fun fold(fold: Fold, coordinates: Set<Coordinate>): Set<Coordinate> {
    return when(fold.axis) {
        Axis.Y -> coordinates.map { coordinate -> if (coordinate.y > fold.line) coordinate.also { it.y = fold.line * 2 - it.y } else coordinate }.toSet()
        Axis.X -> coordinates.map { coordinate -> if (coordinate.x > fold.line) coordinate.also { it.x = fold.line * 2 - it.x } else coordinate }.toSet()
    }
}

fun printCode(folds: Set<Fold>, initialDots: Set<Coordinate>) {
    var coordinates = initialDots
    for (fold in folds) coordinates = fold(fold, coordinates)
    val paper = Paper(coordinates.maxOf { it.y } + 1) { col -> Array(coordinates.maxOf { it.x } + 1) { row -> if (Coordinate(row, col) in coordinates) '#' else '.' } }
    paper.print()
}



