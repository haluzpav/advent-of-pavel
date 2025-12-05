package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.merge
import cz.veleto.aoc.core.overlapsWith

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

    override fun part2(): String = input
        .parseAsRanges()
        .fold(emptySet<LongRange>()) { ranges, range -> ranges.mergeIn(range) }
        .sumOf { it.last - it.first + 1 }
        .toString()

    private fun Sequence<String>.parseAsRanges(): Sequence<LongRange> = this
        .takeWhile { it.isNotEmpty() }
        .map { range ->
            range.split('-').map { it.toLong() }
        }
        .map { (a, b) -> a..b }

    private fun Set<LongRange>.mergeIn(range: LongRange): Set<LongRange> = this
        .fold(emptySet<LongRange>() to range) { (untouched, merged), considered ->
            if (merged.overlapsWith(considered)) {
                untouched to merged.merge(considered)
            } else {
                untouched.plusElement(considered) to merged
            }
        }
        .let { (untouched, merged) -> untouched.plusElement(merged) }
}
