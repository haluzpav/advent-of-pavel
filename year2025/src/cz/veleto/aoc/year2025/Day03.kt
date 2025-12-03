package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay

class Day03(override val config: Year2025Config) : AocDay(config) {

    override fun part1(): String = input
        .map { it.findMaxJolts(batteryCount = 2) }
        .sum()
        .toString()

    override fun part2() = input
        .map { it.findMaxJolts(batteryCount = 12) }
        .sum()
        .toString()

    private fun String.findMaxJolts(batteryCount: Int): Long {
        check(isNotEmpty())
        check(batteryCount > 1)

        val sortedByValue = this
            .map { it.digitToInt() }
            .withIndex()
            .sortedByDescending { it.value }
            .map { IndexedValue(it.index, it.value.digitToChar()) }

        return this
            .findMaxJolts(sortedByValue = sortedByValue, batteryCount = batteryCount)
            .toLong()
            .also { if (config.verboseLog) println("\tfound max $it") }
    }

    private fun String.findMaxJolts(
        sortedByValue: List<IndexedValue<Char>>,
        batteryCount: Int,
        indexShift: Int = 0,
        cache: MutableMap<Any, String> = mutableMapOf(),
    ): String {
        if (isEmpty()) return ""
        if (batteryCount == 0) return ""

        val cacheKey = batteryCount to indexShift
        cache[cacheKey]
            ?.also { if (config.verboseLog) println("${" ".repeat(indexShift)}$this -> found in cache $it") }
            ?.let { return it }

        return sortedByValue
            .filter { length - it.index + indexShift >= batteryCount }
            .maxOf { chosen ->
                val newIndexShift = chosen.index + 1
                val maxChild = substring(newIndexShift - indexShift).findMaxJolts(
                    sortedByValue = sortedByValue.filter { it.index >= newIndexShift },
                    batteryCount = batteryCount - 1,
                    indexShift = newIndexShift,
                    cache = cache,
                )
                chosen.value.toString() + maxChild
            }
            .also { cache[cacheKey] = it }
            .also {
                if (config.verboseLog) println("${" ".repeat(indexShift)}$this -> found sub-max $it")
            }
    }
}
