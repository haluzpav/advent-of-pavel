package cz.veleto.aoc.year2024

import cz.veleto.aoc.core.AocDay
import kotlin.math.absoluteValue
import kotlin.math.sign

class Day02(config: Config) : AocDay(config) {

    override fun part1(): String {
        return parseLevels().count { it.isSafe() }.toString()
    }

    override fun part2(): String {
        // TODO
        return ""
    }

    private fun parseLevels(): Sequence<List<Int>> = input.map { line ->
        line.split(' ').map { it.toInt() }
    }

    private fun List<Int>.isSafe(): Boolean = zipWithNext { a, b -> a - b }.let { diffs ->
        diffs.all { it.absoluteValue in 1..3 } && (diffs.all { it.sign == 1 } || diffs.all { it.sign == -1 })
    }
}
