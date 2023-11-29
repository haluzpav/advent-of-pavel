package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay

class Day06(config: Config) : AocDay(config) {

    override fun part1(): String = findRepetitions(4)

    override fun part2(): String = findRepetitions(14)

    private fun findRepetitions(count: Int): String = input.map { s ->
        (0..s.length - count)
            .first { s.substring(it until it + count).toSet().size == count }
            .let { it + count }
    }.toList().toString()
}
