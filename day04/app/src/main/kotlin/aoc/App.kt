package aoc

import java.io.File

fun main() {
    val input = File("input.txt").readLines().toMutableList()
    val drawnNumbers = input.removeFirst().trim().split(",").map { it.toInt() }; input.removeIf { it.isBlank() }
    val boardSize = input.first().split("\\s+".toRegex()).also { println("Board: $it") }.size
    val numberOfBoards = input.size / boardSize
    val boards: List<BingoBoard> = (0 until numberOfBoards).map { board -> (0 until boardSize).map { row -> input[board * boardSize + row].trim().split("\\s+".toRegex()).map { BingoCell(it.toInt()) }.toTypedArray() }.toTypedArray() }
    when (System.getenv("part")) {"part2" -> println(getWinningBoard(boards.toMutableList(), drawnNumbers, true)) else -> println(getWinningBoard(boards.toMutableList(), drawnNumbers, false)) }
}

data class BingoCell(val number: Int, var marked: Boolean = false)
typealias BingoBoard = Array<Array<BingoCell>>

fun BingoBoard.hasBingo(): Boolean {
    forEachIndexed { index, row ->
        if (row.fold(true) { acc, bingoCell -> acc && bingoCell.marked }) return true
        if (fold(true) { acc, arrayOfBingoCells -> acc && arrayOfBingoCells[index].marked }) return true }
    return false
}

fun BingoBoard.score(drawnNumber: Int): Int = flatten().filter { !it.marked }.sumOf { it.number } * drawnNumber

fun getWinningBoard(boards: MutableList<BingoBoard>, numbersToDraw: List<Int>, getLastWinningBoard: Boolean): Int {
    numbersToDraw.forEach { drawnNumber ->
        boards.flatMap { it.flatten() }.filter { it.number == drawnNumber }.forEach { it.marked = true }
        if (getLastWinningBoard) {
            if (boards.size == 1 && boards.first().hasBingo()) return boards.first().score(drawnNumber)
            boards.removeIf { it.hasBingo() }
        } else boards.find { it.hasBingo() }?.let { return it.score(drawnNumber) }
    }
    return -1
}