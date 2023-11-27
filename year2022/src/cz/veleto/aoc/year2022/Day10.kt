package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import kotlin.math.abs

class Day10(config: Config) : AocDay(config) {
    
    private val xReg: Sequence<Int> = input
        .flatMap {
            val parts = it.split(" ")
            when (val op = parts[0]) {
                "noop" -> listOf(null)
                "addx" -> listOf(null, parts[1].toInt())
                else -> error("Unknown operation $op")
            }
        }
        .runningFold(1) { acc, i -> i?.let { acc + it } ?: acc }

    override fun part1(): String = xReg
        .mapIndexed { index, i -> index to i }
        .filter { (index, _) -> (index + 1 - 20).rem(40) == 0 }
        .sumOf { (index, i) -> i * (index + 1) }
        .toString()

    override fun part2(): String = xReg
        .mapIndexed { index, spritePos ->
            val cyclePos = index.rem(40)
            if (abs(cyclePos - spritePos) <= 1) '█' else ' '
        }
        .joinToString(separator = "")
        .chunked(40)
        .take(6)
        .reduce { acc, s -> acc + '\n' + s }
}
