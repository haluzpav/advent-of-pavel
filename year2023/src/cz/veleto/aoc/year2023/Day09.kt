package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay

class Day09(config: Config) : AocDay(config) {

    override fun part1(): String = input
        .map { line -> line.split(' ').map { it.toLong() } }
        .map { history ->
            history
                .indices
                .asSequence()
                .runningFold(State(history)) { state, _ ->
                    val newSequence = state.lastSequence.zipWithNext { a, b -> b - a }
                    State(newSequence, state.lastNumbers + newSequence.last())
                }
                .onEach { if (config.log) println(it.lastSequence.joinToString(" ")) }
                .first { state -> state.lastSequence.all { it == 0L } }
                .lastNumbers
                .sum()
        }
        .sum()
        .toString()

    override fun part2(): String {
        // TODO
        return ""
    }

    data class State(
        val lastSequence: List<Long>,
        val lastNumbers: List<Long> = listOf(lastSequence.last()),
    )
}
