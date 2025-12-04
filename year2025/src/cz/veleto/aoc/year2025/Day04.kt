package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.contains
import cz.veleto.aoc.core.get
import cz.veleto.aoc.core.plus

class Day04(override val config: Year2025Config) : AocDay(config) {

    private val neighborDirections = listOf(
        -1 to -1,
        -1 to 0,
        -1 to 1,
        0 to -1,
        0 to 1,
        1 to -1,
        1 to 0,
        1 to 1,
    )

    private val paperRoll = '@'

    override fun part1(): String = cachedInput
        .withIndex()
        .sumOf { (lineIndex, line) ->
            line
                .indices
                .filter { line[it] == paperRoll }
                .count { charIndex ->
                    val pos = lineIndex to charIndex
                    neighborDirections
                        .map { pos + it }
                        .filter { it in cachedInput }
                        .count { cachedInput[it] == paperRoll } < 4
                }
        }
        .toString()

    override fun part2(): String {
        // TODO
        return ""
    }
}
