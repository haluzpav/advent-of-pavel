package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay

class Day02(override val config: Year2025Config) : AocDay(config) {

    override fun part1(): String = input
        .getProductIds()
        .filter { it.toString().isRepeated(repetition = 2) }
        .sum()
        .toString()

    override fun part2(): String = input
        .getProductIds()
        .filter { it.toString().isRepeated() }
        .onEach { if (config.verboseLog) println("Found invalid ID: $it") }
        .sum()
        .toString()

    private fun Sequence<String>.getProductIds(): Sequence<Long> = this
        .flatMap { it.split(',') }
        .map { range ->
            range.split('-').map { it.toLong() }
        }
        .map { (a, b) -> a..b }
        .flatMap { it.asSequence() }

    private fun String.isRepeated(): Boolean = (2..length).any { isRepeated(it) }

    private fun String.isRepeated(repetition: Int): Boolean {
        if (length % repetition != 0) return false
        return this
            .chunked(length / repetition)
            .toSet()
            .size == 1
    }
}
