package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.solveQuadratic
import kotlin.math.ceil
import kotlin.math.floor

class Day06(config: Config) : AocDay(config) {

    override fun part1(): String = cachedInput
        .parsePart1()
        .map { (t, d) -> countWaysToWin(t, d) }
        .reduce(Long::times)
        .toString()

    override fun part2(): String {
        val (t, d) = cachedInput.parsePart2()
        return countWaysToWin(t, d).toString()
    }

    private fun countWaysToWin(t: Long, d: Long): Long {
        val (h1, h2) = solveQuadratic(1.0, -t.toDouble(), d.toDouble())
        check(h1.isFinite())
        check(h2.isFinite())
        val minHoldTime = floor(h1 + 1).toLong()
        val maxHoldTime = ceil(h2 - 1).toLong()
        return maxHoldTime - minHoldTime + 1
    }

    private fun List<String>.parsePart1(): List<Pair<Long, Long>> {
        fun String.parseNumbers() = drop(10).split(' ').filter(String::isNotBlank).map { it.toLong() }
        val times = this[0].parseNumbers()
        val distances = this[1].parseNumbers()
        check(times.size == distances.size)
        return times.zip(distances)
    }

    private fun List<String>.parsePart2(): Pair<Long, Long> {
        fun String.parseNumber() = drop(10).replace(" ", "").toLong()
        return this[0].parseNumber() to this[1].parseNumber()
    }
}
