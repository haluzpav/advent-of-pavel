package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.Pos
import cz.veleto.aoc.core.contains
import cz.veleto.aoc.core.get
import cz.veleto.aoc.core.plus
import cz.veleto.aoc.core.print
import cz.veleto.aoc.core.set

class Day04(override val config: Year2025Config) : AocDay(config) {

    private val neighborDirections = listOf(
        -1 to -1,
        -1 to 0,
        -1 to 1,
        0 to -1,
        0 to 1,
        1 to -1,
        1 to 0,
        1 to 1,
    )

    private val paperRoll = '@'
    private val paperRollAccessibilityThreshold = 4
    private val removedPaperRoll = 'x'

    override fun part1(): String = cachedInput
        .getAccessiblePaperRolls()
        .count()
        .toString()

    override fun part2(): String = (0..Int.MAX_VALUE)
        .asSequence()
        .runningFold(cachedInput to 0) { (floor, _), _ -> floor.removeAccessiblePaperRolls() }
        .onEach { (floor, removedPaperRolls) ->
            if (config.verboseLog) {
                println("Remove $removedPaperRolls rolls of paper:")
                floor.print()
                println()
            }
        }
        .map { (_, removedPaperRolls) -> removedPaperRolls }
        .drop(1)
        .takeWhile { it > 0 }
        .reduce(Int::plus)
        .toString()

    private fun List<String>.removeAccessiblePaperRolls(): Pair<List<String>, Int> {
        val accessiblePaperRolls = getAccessiblePaperRolls()
        val newFloor = toMutableList()
        accessiblePaperRolls.forEach { newFloor[it] = removedPaperRoll }
        return newFloor to accessiblePaperRolls.count()
    }

    private fun List<String>.getAccessiblePaperRolls(): Sequence<Pos> = this
        .indices
        .asSequence()
        .flatMap { lineIndex ->
            this[lineIndex].indices.asSequence().map { lineIndex to it }
        }
        .filter { this[it] == paperRoll }
        .filter { pos ->
            neighborDirections
                .map { pos + it }
                .filter { it in this }
                .count { this[it] == paperRoll } < paperRollAccessibilityThreshold
        }
}
