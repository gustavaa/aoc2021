package aoc

import java.io.File
import kotlin.math.max

data class Pos(val x: Int = 0, val y: Int = 0)
operator fun Pos.plus(velocity: Velocity): Pos = Pos(this.x + velocity.xVel, this.y + velocity.yVel)
data class Target(val xRange: IntRange, val yRange: IntRange)
data class Velocity(val xVel: Int, val yVel: Int)
operator fun Target.contains(pos: Pos): Boolean = xRange.contains(pos.x) && yRange.contains(pos.y)
fun main() {
    val target = File("input.txt").readLines().first().let { Target(getRangeFromInput(it, "x"), getRangeFromInput(it, "y")) }
    when (System.getenv("part")) {"part2" -> println(getSolutionPart2(target)) else -> println(getSolutionPart1(target)) }
}

fun getRangeFromInput(input: String, key: String): IntRange = getRangeValue(input, key, 0)..getRangeValue(input, key, 1)

fun getRangeValue(input: String, key: String, groupIndex: Int): Int = Regex("$key=([0-9-]+)..([0-9-]+)").find(input)!!.groups[groupIndex + 1]!!.value.toInt()

fun Target.minimumXVelocity(): Int = (0..xRange.first).takeWhile { (0..it).sum() < xRange.first}.last()

fun getSolutionPart1(target: Target): Int = getPossibleTrajectoriesMaxHeights(target).maxOf { it }
fun getSolutionPart2(target: Target): Int = getPossibleTrajectoriesMaxHeights(target).size

fun getPossibleTrajectoriesMaxHeights(target: Target): List<Int> =
    buildList { (target.minimumXVelocity()..target.xRange.last).forEach { x ->
        (target.yRange.first..-1 * target.yRange.first).forEach { y ->
            testTrajectory(Velocity(x,y), target)?.let { add(it) }
        }
    }}

fun testTrajectory(initialVelocity: Velocity, target: Target): Int? {
    var pos = Pos()
    var velocity = initialVelocity
    var maxY = Int.MIN_VALUE
    while (pos.x <= target.xRange.last && pos.y >= target.yRange.first) {
        pos += velocity
        if (maxY < pos.y) maxY = pos.y
        if (pos in target) { return maxY }
        velocity = Velocity(max(velocity.xVel - 1, 0),velocity.yVel - 1)
    }
    return null
}
