package aoc

import java.io.File
import java.util.*

typealias Map<T> = Array<Array<T>>

data class Node(val x: Int, val y: Int, val weight: Int)

fun main() {
    val input = File("input.txt").readLines().mapIndexed { row, line ->
        line.map(Character::getNumericValue).mapIndexed { col, risk -> Node(row, col, risk) }.toTypedArray()
    }.toTypedArray()
    when (System.getenv("part")) {"part2" -> println(getSolutionPart2(input))else -> println(getSolutionPart1(input)) }
}


fun getSolutionPart1(input: Map<Node>): Int = findLowestRiskThroughDjikstra(input[0][0], input[input.size - 1][input[0].size - 1], input)

fun getSolutionPart2(input: Map<Node>): Int = expand(input, 5).let { expandedMap -> findLowestRiskThroughDjikstra(
        expandedMap[0][0],
        expandedMap[expandedMap.size - 1][expandedMap[0].size - 1],
        expandedMap
    )
}

fun findLowestRiskThroughDjikstra(source: Node, target: Node, inputMap: Map<Node>): Int {
    // Initialize single source
    val riskMap = mutableMapOf<Node, Int>()
    inputMap.flatten().forEach { riskMap[it] = Int.MAX_VALUE }

    val settledNodes = mutableSetOf<Node>()
    val queue = PriorityQueue<Node> { node1, node2 -> riskMap[node1]!!.compareTo(riskMap[node2]!!) }
    riskMap[source] = 0
    queue.add(source)

    while (settledNodes.size != riskMap.size) {
        if (queue.isEmpty()) break
        val currentNode: Node = queue.remove()
        if (currentNode in settledNodes) continue
        settledNodes.add(currentNode)
        currentNode.getNeighbourNodes(inputMap).filter { it !in settledNodes }.forEach { coordinate ->
            (riskMap[currentNode]!! + coordinate.weight).takeIf { riskMap[coordinate]!! > it  }?.let { alt -> riskMap[coordinate] = alt }
            queue.add(coordinate)
        }
    }
    return riskMap[target]!!
}

fun expand(map: Map<Node>, expansionFactor: Int): Map<Node> {
    val originalWidth = map[0].size
    val originalHeight = map.size
    return Array(originalHeight * expansionFactor) { Array(originalHeight * expansionFactor) { Node(-1,-1,1) } }.also { resultMap ->
        (0 until expansionFactor).forEach { expansionRow ->
            (0 until expansionFactor).forEach { expansionCol ->
                ((0 + expansionRow * originalHeight) until (originalHeight + expansionRow * originalHeight)).forEach { row ->
                    ((0 + expansionCol * originalWidth) until (originalWidth + expansionCol * originalWidth)).forEach { col ->
                        val originalValue = when {
                            expansionRow > 0 -> resultMap[row - originalHeight][col].weight
                            expansionCol > 0 -> resultMap[row][col - originalWidth].weight
                            else -> map[row % originalHeight][col % originalWidth].weight - 1
                        }
                        resultMap[row][col] = Node(row,col,(originalValue + 1).takeIf { it <= 9 } ?: (originalValue + 1 - 9))
                    }
                }
            }
        }
    }
}

fun Node.getNeighbourNodes(map: Array<Array<Node>>): Set<Node> = mutableSetOf<Node>().also { nodes ->
    nodes.addAll((-1..1)
        .filter { it != 0 && this.y + it >= 0 && this.y + it < map[0].size }
        .map { map[this.x][this.y + it] })
    nodes.addAll((-1..1)
        .filter { it != 0 && this.x + it >= 0 && this.x + it < map.size }
        .map { map[this.x + it][this.y] })
}
