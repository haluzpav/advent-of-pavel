package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay

class Day09(override val config: Year2023Config) : AocDay(config) {

    override fun part1(): String = input
        .mapToEndStates()
        .map { it.lastNumbers.sum() } // b = a + c
        .sum()
        .toString()

    override fun part2(): String = input
        .mapToEndStates()
        .map { state -> // a = b - c
            state.firstNumbers.asReversed()
                .reduce { extrapolated, firstNumber -> firstNumber - extrapolated }
        }
        .sum()
        .toString()

    private fun Sequence<String>.mapToEndStates(): Sequence<State> = this
        .map { line -> line.split(' ').map { it.toLong() } }
        .map { history ->
            history
                .indices // maximum number of sequences
                .asSequence()
                .runningFold(State(history)) { state, _ ->
                    state.extend(state.lastSequence.zipWithNext { a, b -> b - a })
                }
                .onEach { if (config.log) println(it.lastSequence.joinToString(" ")) }
                .first { state -> state.lastSequence.all { it == 0L } }
        }

    private fun State.extend(newSequence: List<Long>): State = State(
        lastSequence = newSequence,
        firstNumbers = firstNumbers + newSequence.first(),
        lastNumbers = lastNumbers + newSequence.last(),
    )

    data class State(
        val lastSequence: List<Long>,
        val firstNumbers: List<Long> = listOf(lastSequence.first()),
        val lastNumbers: List<Long> = listOf(lastSequence.last()),
    )
}
