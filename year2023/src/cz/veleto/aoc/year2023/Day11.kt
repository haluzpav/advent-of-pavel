package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.Pos
import cz.veleto.aoc.core.manhattanTo
import cz.veleto.aoc.core.plus
import cz.veleto.aoc.core.transpose

class Day11(override val config: Year2023Config) : AocDay(config) {

    private val spaceChar = '.'
    private val galaxyChar = '#'

    override fun part1(): String = solve(expandFactor = 2)

    override fun part2(): String = solve(expandFactor = config.day11part2expandFactor)

    private fun solve(expandFactor: Int): String {
        val verticalExpansion = cachedInput.mapToVerticalExpansion(expandFactor)
        val horizontalExpansion = cachedInput.transpose().mapToVerticalExpansion(expandFactor)
        val galaxies = cachedInput.findGalaxies()
        return galaxies
            .map { it.expand(verticalExpansion, horizontalExpansion) }
            .asSequence()
            .findManhattanBetweenAllPairs()
            .sum()
            .toString()
    }

    private fun List<String>.mapToVerticalExpansion(expandFactor: Int): List<Int> = this
        .map { line -> line.all { it == spaceChar } }
        .map { if (it) expandFactor - 1 else 0 }
        .runningReduce(Int::plus)

    private fun List<String>.findGalaxies(): List<Pos> = this
        .flatMapIndexed { lineIndex, line ->
            line.withIndex().filter { it.value == galaxyChar }.map { lineIndex to it.index }
        }

    private fun Pos.expand(verticalExpansion: List<Int>, horizontalExpansion: List<Int>): Pos =
        this + Pos(verticalExpansion[first], horizontalExpansion[second])

    private fun Sequence<Pos>.findManhattanBetweenAllPairs(): Sequence<Long> = this
        .flatMapIndexed { aIndex, aPos ->
            drop(aIndex + 1).map { bPos -> aPos.manhattanTo(bPos).toLong() }
        }
}
