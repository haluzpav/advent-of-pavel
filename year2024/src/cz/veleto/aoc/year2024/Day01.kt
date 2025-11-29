package cz.veleto.aoc.year2024

import cz.veleto.aoc.core.AocDay
import kotlin.math.abs

class Day01(override val config: Year2024Config) : AocDay(config) {

    override fun part1(): String {
        val (aList, bList) = parseRawLists()
        return aList.sorted().zip(bList.sorted()).sumOf { (a, b) -> abs(a - b) }.toString()
    }

    override fun part2(): String {
        val (aList, bList) = parseRawLists()
        return aList.sumOf { a -> a * bList.count { it == a } }.toString()
    }

    private fun parseRawLists(): Pair<List<Int>, List<Int>> = input
        .map { line ->
            line.split(' ').filter { it.isNotBlank() }.map { it.toInt() }
        }
        .fold(emptyList<Int>() to emptyList()) { (aCum, bCum), (a, b) ->
            aCum + a to bCum + b
        }
}
