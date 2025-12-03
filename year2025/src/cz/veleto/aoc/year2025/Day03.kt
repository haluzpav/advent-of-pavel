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
        cache: MutableMap<Any, String> = mutableMapOf(),
    ): String {
        if (isEmpty()) return ""
        if (batteryCount == 0) return ""

        val cacheKey = length to batteryCount
        cache[cacheKey]
            ?.also { if (config.verboseLog) println("\t\t$this -> found in cache $it") }
            ?.let { return it }

        return sortedByValue
            .filter { length - it.index >= batteryCount }
            .maxOf { chosen ->
                val maxChild = substring(chosen.index + 1).findMaxJolts(
                    sortedByValue = sortedByValue.mapNotNull {
                        val newIndex = it.index - chosen.index - 1
                        if (newIndex < 0) return@mapNotNull null
                        it.copy(index = newIndex)
                    },
                    batteryCount = batteryCount - 1,
                    cache = cache,
                )
                chosen.value.toString() + maxChild
            }
            .also { cache[cacheKey] = it }
            .also {
                if (config.verboseLog) println("\t\t$this -> found sub-max $it")
            }
    }
}
