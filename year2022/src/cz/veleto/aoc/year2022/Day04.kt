package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay

class Day04(config: Config) : AocDay(config) {

    override fun part1(): String = input
        .map(::parseRanges)
        .count { (a, b) -> a.fullyIn(b) || b.fullyIn(a) }
        .toString()

    override fun part2(): String = input
        .map(::parseRanges)
        .count { (a, b) -> b.first in a || a.first in b }
        .toString()

    private fun parseRanges(s: String): Pair<UIntRange, UIntRange> = s
        .split(',')
        .map { sRange ->
            sRange.split('-')
                .map { it.toUInt() }
                .also { check(it.size == 2) }
                .let { (a, b) -> a..b }
                .also { check(!it.isEmpty()) }
        }
        .also { check(it.size == 2) }
        .let { (a, b) -> a to b }

    private fun <T : Comparable<T>> ClosedRange<T>.fullyIn(other: ClosedRange<T>): Boolean =
        start in other && endInclusive in other
}
