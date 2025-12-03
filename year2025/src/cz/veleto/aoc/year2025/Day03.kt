package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay

class Day03(override val config: Year2025Config) : AocDay(config) {

    override fun part1(): String = input
        .map { it.findMaxJolts(batteryCount = 2).toLong() }
        .sum()
        .toString()

    override fun part2() = input
        .map { it.findMaxJolts(batteryCount = 12).toLong() }
        .sum()
        .toString()

    private fun String.findMaxJolts(
        batteryCount: Int,
        cache: MutableMap<Any, String> = mutableMapOf(),
    ): String = when {
        isEmpty() -> ""
        batteryCount == 0 -> ""
        else -> cache.getOrPut(key = length to batteryCount) {
            (0..length - batteryCount)
                .maxOf { index ->
                    val maxChild = this
                        .drop(index + 1)
                        .findMaxJolts(batteryCount = batteryCount - 1, cache = cache)
                    this[index].toString() + maxChild
                }
                .also { if (config.verboseLog) println("\t\t$this -> found max $it") }
        }
    }
}
