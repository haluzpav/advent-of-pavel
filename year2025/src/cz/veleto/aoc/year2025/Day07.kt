package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay

class Day07(override val config: Year2025Config) : AocDay(config) {

    override fun part1(): String = input
        .fold(State()) { state, line ->
            if (state.beams.isEmpty()) {
                return@fold state.copy(beams = setOf(line.indexOf('S')))
            }

            val activatedSplitters = line
                .withIndex()
                .filter { it.value == '^' }
                .map { it.index }
                .filter { it in state.beams }

            val passingBeams = state.beams - activatedSplitters.toSet()
            val splitBeams = activatedSplitters
                .flatMap { listOf(it - 1, it + 1) }
                .toSet()

            state.copy(
                beams = passingBeams + splitBeams,
                splitCount = state.splitCount + activatedSplitters.size,
            )
        }
        .splitCount
        .toString()

    override fun part2(): String {
        // TODO
        return ""
    }

    private data class State(
        val beams: Set<Int> = emptySet(),
        val splitCount: Int = 0,
    )
}
