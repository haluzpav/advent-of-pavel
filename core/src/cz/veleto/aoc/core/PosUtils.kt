package cz.veleto.aoc.core

import kotlin.math.abs

typealias Pos = Pair<Int, Int>
typealias Pos3 = Triple<Int, Int, Int>

operator fun List<String>.get(pos: Pos): Char = this[pos.first][pos.second]

operator fun List<String>.contains(pos: Pos): Boolean = pos.first in indices && pos.second in this[pos.first].indices

operator fun Pos.plus(other: Pos): Pos = first + other.first to second + other.second

operator fun Pos.minus(other: Pos): Pos = first - other.first to second - other.second

fun Pos.manhattanTo(other: Pos): Int =
    abs(first - other.first) + abs(second - other.second)

fun Pos3.manhattanTo(other: Pos3): Int =
    abs(first - other.first) + abs(second - other.second) + abs(third - other.third)

operator fun Pair<IntRange, IntRange>.contains(pos: Pos): Boolean {
    val (xRange, yRange) = this
    val (x, y) = pos
    return x in xRange && y in yRange
}

fun Pos.rotateClockwise(): Pos = second to -first

fun Pos.flip(): Pos = -first to -second

fun Iterable<Pos>.move(pos: Pos): List<Pos> = map { it + pos }
