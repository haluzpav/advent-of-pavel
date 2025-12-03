package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay
import kotlin.math.pow

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
        val minJolts = 10.0.pow(batteryCount - 1).toLong()
        val maxJolts = 10.0.pow(batteryCount).toLong() - 1
        if (config.verboseLog) println("$this -> minJolts $minJolts maxJolts $maxJolts")
        return (minJolts..maxJolts)
            .reversed()
            .first { jolts ->
                jolts
                    .toString()
                    .asSequence()
                    .joinToString(prefix = "^.*", separator = ".*", postfix = ".*$")
                    .toRegex()
                    .matches(this)
            }
            .also { if (config.verboseLog) println("\tfound max $it") }
    }
}
