package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay

class Day05(override val config: Year2025Config) : AocDay(config) {

    override fun part1(): String {
        val ranges = input
            .parseAsRanges()
            .toList()
        return input
            .drop(ranges.size + 1)
            .map { it.toLong() }
            .count { id ->
                ranges.any { range -> id in range }
            }
            .toString()
    }

    // TODO java.lang.OutOfMemoryError: Java heap space LOL
    override fun part2(): String = input
        .parseAsRanges()
        .flatMap { it.asSequence() }
        .toSet()
        .size
        .toString()

    private fun Sequence<String>.parseAsRanges(): Sequence<LongRange> = this
        .takeWhile { it.isNotEmpty() }
        .map { range ->
            range.split('-').map { it.toLong() }
        }
        .map { (a, b) -> a..b }
}
