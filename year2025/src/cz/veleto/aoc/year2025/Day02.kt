package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay

class Day02(override val config: Year2025Config) : AocDay(config) {

    override fun part1(): String = input
        .getProductIds()
        .filter { it.toString().isRepeatedTwice() }
        .sum()
        .toString()

    override fun part2(): String {
        // TODO
        return ""
    }

    private fun Sequence<String>.getProductIds(): Sequence<Long> = this
        .flatMap { it.split(',') }
        .map { range ->
            range.split('-').map { it.toLong() }
        }
        .map { (a, b) -> a..b }
        .flatMap { it.asSequence() }

    private fun String.isRepeatedTwice(): Boolean = when {
        length % 2 != 0 -> false
        substring(0..<length / 2) == substring(length / 2..lastIndex) -> true
        else -> false
    }
}
