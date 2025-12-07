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

    override fun part2(): String = input
        .foldIndexed(State2()) { lineIndex, state, line ->
            if (state.beams.isEmpty()) {
                return@foldIndexed state.copy(beams = mapOf(line.indexOf('S') to 1))
            }
            if (config.log) println("Line $lineIndex, beam paths ${state.beams.size}, beam count ${state.beams.values.sum()}")

            val activatedSplitters = line
                .withIndex()
                .filter { it.value == '^' }
                .map { it.index }
                .filter { it in state.beams }

            val passingBeams = state.beams - activatedSplitters.toSet()
            val splitBeams = activatedSplitters
                .flatMap { splitter ->
                    val count = state.beams[splitter]!!
                    listOf(splitter - 1 to count, splitter + 1 to count)
                }
            val mergedBeams = splitBeams
                .plus(passingBeams.toList())
                .groupBy(
                    keySelector = { it.first },
                    valueTransform = { it.second },
                )
                .mapValues { it.value.sum() }
                .toMap()

            state.copy(
                beams = mergedBeams,
            )
        }
        .beams
        .values
        .sum()
        .toString()

    private data class State(
        val beams: Set<Int> = emptySet(),
        val splitCount: Int = 0,
    )

    private data class State2(
        // index, count
        val beams: Map<Int, Long> = emptyMap(),
    )
}
