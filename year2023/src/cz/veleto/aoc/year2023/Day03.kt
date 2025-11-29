package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.coerceIn
import cz.veleto.aoc.core.expand
import cz.veleto.aoc.core.overlapsWith
import cz.veleto.aoc.core.shift

class Day03(override val config: Year2023Config) : AocDay(config) {

    private val numberRegex = Regex("[0-9]+")
    private val symbolRegex = Regex("[^0-9.]")
    private val gearRegex = Regex("\\*")

    private val linesBounds = cachedInput.indices
    private val columnsBounds = cachedInput.first().indices

    override fun part1(): String = cachedInput
        .flatMapIndexed { lineIndex, line ->
            val neighborLines = cachedInput.getNeighbors(lineIndex)
            numberRegex.findAll(line).mapNotNull { match ->
                val columnRange = match.range.expand(1).coerceIn(columnsBounds)
                val neighbors = neighborLines.joinToString(separator = "") { it.slice(columnRange) }
                val isSymbolAround = symbolRegex.containsMatchIn(neighbors)
                match.value.takeIf { isSymbolAround }?.toInt()
            }
        }
        .sum()
        .toString()

    override fun part2(): String = cachedInput
        .flatMapIndexed { lineIndex, line ->
            val neighborLines = cachedInput.getNeighbors(lineIndex)
            gearRegex.findAll(line).mapNotNull { gearMatch ->
                val columnRange = gearMatch.range.expand(3).coerceIn(columnsBounds)
                neighborLines
                    .flatMap { neighborLine ->
                        numberRegex.findAll(neighborLine.slice(columnRange)).mapNotNull { numberMatch ->
                            val numberRange = numberMatch.range.shift(columnRange.first)
                            val isGearNumber = gearMatch.range.expand(1).overlapsWith(numberRange)
                            if (config.log) println("Found number ${numberMatch.value}, near a gear = $isGearNumber")
                            numberMatch.value.takeIf { isGearNumber }?.toInt()
                        }
                    }
                    .takeIf { it.size > 1 }
                    ?.also { check(it.size == 2) }
                    ?.also { if (config.log) println("Multiplying $it") }
                    ?.reduce(Int::times)
            }
        }
        .also { if (config.log) println("Summing $it") }
        .sum()
        .toString()

    private fun List<String>.getNeighbors(around: Int): List<String> =
        slice((around..around).expand(1).coerceIn(linesBounds))
}
