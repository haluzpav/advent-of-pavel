package cz.veleto.aoc.year2024

import cz.veleto.aoc.core.AocDay
import kotlin.math.abs

class Day01(config: Config) : AocDay(config) {

    override fun part1(): String {
        val (a, b) = parseRawLists()
        return a.sorted().zip(b.sorted()).sumOf { (a, b) -> abs(a - b) }.toString()
    }

    override fun part2(): String {
        val (a, b) = parseRawLists()
        a.sumOf { a -> a * b.count { it == a } }
        return ""
    }

    private fun parseRawLists(): Pair<List<Int>, List<Int>> = input
        .map { line ->
            line.split(' ').filter { it.isNotBlank() }.map { it.toInt() }
        }
        .fold(emptyList<Int>() to emptyList()) { (aCum, bCum), (a, b) ->
            aCum + a to bCum + b
        }
}
