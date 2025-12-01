package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.positiveRem

class Day01(override val config: Year2025Config) : AocDay(config) {

    private val dialSize = 100
    private val dialStart = dialSize / 2

    override fun part1(): String = input
        .runningFold(dialStart) { dial, rotation ->
            val (sign, distance) = parseRotation(rotation)
            dial + sign * distance
        }
        .map { rawDial ->
            rawDial.positiveRem(dialSize).also {
                if (config.verboseLog) println("dial $it")
            }
        }
        .count { it == 0 }
        .toString()

    override fun part2(): String = input
        .flatMap { rotation ->
            val (sign, distance) = parseRotation(rotation)
            sequence {
                repeat(distance) {
                    yield(sign)
                }
            }
        }
        .runningFold(dialStart) { dial, distance ->
            dial + distance
        }
        .map {
            if (config.verboseLog) println("dial $it")
            it
        }
        .count { it % dialSize == 0 }
        .toString()

    private fun parseRotation(rotation: String): Pair<Int, Int> {
        val direction = rotation[0]
        val distance = rotation.substring(1..rotation.lastIndex).toInt()
        check(distance > 0) { "Distance is expected to be positive" }
        val sign = when (direction) {
            'L' -> -1
            'R' -> 1
            else -> error("unknown direction $direction")
        }
        return sign to distance
    }
}
