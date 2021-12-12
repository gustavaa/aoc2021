package aoc

import java.io.File

fun main() {
    val graph = mutableMapOf<String, MutableSet<String>>()
    File("input.txt").readLines().map { it.split("-") }.forEach { line ->
        graph.getOrPut(line[0], ::mutableSetOf) += line[1]
        graph.getOrPut(line[1], ::mutableSetOf) += line[0]
    }
    when (System.getenv("part")) {"part2" -> findPaths("start", "end", graph, 2) else -> findPaths("start", "end", graph).size }
}
fun findPaths(start: String, end: String, graph: Map<String, Set<String>>, mostSmallPathVisits: Int = 1, currentPath: MutableList<String> = mutableListOf(), allPaths: MutableSet<List<String>> = mutableSetOf()): MutableSet<List<String>> {
    currentPath.add(start)
    if (start == end) {
        allPaths.add(currentPath)
        return allPaths
    }
    for (neighbour in graph[start] ?: emptyList()) {
        if (neighbour[0].isLowerCase() && !shouldVisitNeighbour(currentPath, mostSmallPathVisits, neighbour)) continue
        findPaths(neighbour, end, graph, mostSmallPathVisits, currentPath.toMutableList(), allPaths)
    }
    return allPaths
}

fun shouldVisitNeighbour(currentPath: List<String>, mostSmallPathVisits: Int, neighbour: String): Boolean {
    val currentVisits = currentPath.count { it == neighbour }
    if (neighbour == "start" || neighbour == "end") return currentVisits == 0
    val hasVisitedASmallCaveTwice = currentPath.filter { it[0].isLowerCase() }.hasDuplicates()
    return currentVisits == 0 || currentVisits < mostSmallPathVisits && !hasVisitedASmallCaveTwice
}

fun List<String>.hasDuplicates(): Boolean {
    val set = mutableSetOf<String>()
    for (node in this){ if (!set.add(node)) return true }
    return false
}
