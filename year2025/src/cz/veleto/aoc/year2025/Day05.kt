package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay

class Day05(override val config: Year2025Config) : AocDay(config) {

    override fun part1(): String {
        val ranges = input
            .takeWhile { it.isNotEmpty() }
            .map { range ->
                range.split('-').map { it.toLong() }
            }
            .map { (a, b) -> a..b }
            .toList()
        return input
            .drop(ranges.size+1)
            .map { it.toLong() }
            .count { id ->
                ranges.any { range -> id in range }
            }
            .toString()
    }

    override fun part2(): String {
        // TODO
        return ""
    }
}
