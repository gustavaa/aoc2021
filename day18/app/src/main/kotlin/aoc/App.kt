package aoc

import java.io.File
import kotlin.math.ceil
import kotlin.math.floor

sealed class Element {
    abstract fun magnitude(): Int
    data class SnailFishNumber(val x: Element, val y: Element): Element() {
        constructor(x: Int, y: Int): this(Number(x), Number(y))
        override fun toString(): String = "[$x,$y]"
        override fun magnitude(): Int = 3 * x.magnitude() + 2 * y.magnitude()
    }
    data class Number(val value: Int): Element() {
        override fun toString(): String = value.toString()
        override fun magnitude(): Int = value
    }
}

fun main() {
    val input = File("input.txt").readLines()
    when (System.getenv("part")) {"part2" -> println(getSolutionPart2(input)) else -> println(getSolutionPart1(input)) }
}

fun reduce(snailFishString: String): String {
    var result = snailFishString
    var doneExploding = false
    var doneSplitting = false
    while (!doneExploding || !doneSplitting) {
        explode(result).let {
            doneExploding = result == it
            result = it
        }
        if (!doneExploding) continue

        split(result).let {
            doneSplitting = result == it
            result = it
        }
    }
    return result
}

val pairRegex = Regex("\\[(\\d+),(\\d+)\\]")
val numberRegex = Regex("\\d+")
fun explode(snailFishString: String): String {
    var levelCount = 0
    snailFishString.forEachIndexed { index, c ->
        if (c == '[') levelCount++
        if (c == ']') levelCount--
        if (levelCount == 5) {
            var newNumber = snailFishString
            val matchToExplode = pairRegex.find(snailFishString,index)!!
            val nextNumberMatch = numberRegex.find(snailFishString, matchToExplode.range.last + 1)
            nextNumberMatch?.let { newNumber = newNumber.replaceRange(nextNumberMatch.range,(matchToExplode.groupValues[2].toInt() + nextNumberMatch.value.toInt()).toString()) }
            val prevNumberMatch = numberRegex.findAll(snailFishString.substring(0,matchToExplode.range.first - 1)).takeIf {  it.toList().isNotEmpty() }?.last()
            var offset = 0
            prevNumberMatch?.let {
                val mergeResult = matchToExplode.groupValues[1].toInt() + prevNumberMatch.value.toInt()
                newNumber = newNumber.replaceRange(prevNumberMatch.range, mergeResult.toString())
                offset = mergeResult.toString().length - prevNumberMatch.value.length
            }
            newNumber = newNumber.replaceRange((matchToExplode.range.first + offset)..(matchToExplode.range.last + offset), "0")
            return newNumber
        }
    }
    return snailFishString
}

val digitLargerThan10Regex = Regex("\\d{2,}")
fun split(snailFishString: String): String {
    digitLargerThan10Regex.find(snailFishString)?.let { match ->
        val matchNumber = match.value.toInt()
        val newPair = Element.SnailFishNumber(floor(matchNumber.toDouble()/2).toInt(),ceil(matchNumber.toDouble()/2).toInt())
        return snailFishString.replaceRange(match.range, newPair.toString())
    }
    return snailFishString
}

fun parseSnailFishNumber(string: String): Element.SnailFishNumber {
    var number = string.removePrefix("[").removeSuffix("]")
    val elements = Array<Element?>(2){null}
    (0..1).forEach { elementCount ->
        if (elementCount == 1) number = number.drop(1) //drop separating comma
        when {
            number.startsWith('[') -> {
                val elementToParse = number.substring(0,number.getIndexForNextClosingBracket())
                number = number.drop(elementToParse.length)
                elements[elementCount] = parseSnailFishNumber(elementToParse)
            }
            else -> {
                val parsedNumber = number.getNextNumber()
                elements[elementCount] = Element.Number(parsedNumber)
                number = number.drop(parsedNumber.toString().length)
            }
        }
    }

    return Element.SnailFishNumber(elements[0]!!, elements[1]!!)
}

fun String.getNextNumber(): Int = numberRegex.find(this)!!.value.toInt()

fun String.getIndexForNextClosingBracket(): Int {
    var levelCount = 0
    this.forEachIndexed { index, c ->
        if (c == '[') levelCount++
        if (c == ']') levelCount--
        if (levelCount == 0) return index + 1
    }
    return -1
}

fun addSnailFishNumbers(snailFishString1: String, snailFishString2: String): String = "[$snailFishString1,$snailFishString2]"

fun getSolutionPart1(input: List<String>): Int {
    val iterator = input.listIterator()
    var result: String = reduce(iterator.next())
    while (iterator.hasNext()) result = reduce(addSnailFishNumbers(result, iterator.next()))
    return parseSnailFishNumber(result).magnitude()
}

fun getSolutionPart2(input: List<String>): Int {
    var max = 0
    for (x in input) {
        for (y in input){
            if (x == y) continue
            ((parseSnailFishNumber(reduce(addSnailFishNumbers(x, y)))).magnitude()).let { if ( it > max) max = it }
        }
    }
    return max
}