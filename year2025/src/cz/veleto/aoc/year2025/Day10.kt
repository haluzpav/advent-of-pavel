package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay

class Day10(override val config: Year2025Config) : AocDay(config) {

    override fun part1(): String = input
        .parseMachines()
        .sumOf { machine ->
            getMinTransitionCountToTurnOn(
                targetLights = machine.targetLights,
                buttonTransitions = machine.buttonTransitions,
            )!!
        }
        .toString()

    override fun part2(): String = input
        .parseMachines()
        .mapIndexed { index, machine ->
            if (config.log) println("Machine $index: $machine")
            getMinTransitionCountToMatchJoltage(
                targetJoltages = machine.targetJoltages,
                buttonTransitions = machine.buttonTransitions,
            ).also {
                if (config.log) println("\tmin transitions to match joltage = $it")
            }
        }
        .sum()
        .toString()

    private fun Sequence<String>.parseMachines(): Sequence<Machine> = map { line ->
        val elements = line.split(' ')
        Machine(
            targetLights = elements
                .first()
                .drop(1)
                .dropLast(1)
                .withIndex()
                .filter { it.value == '#' }
                .map { it.index }
                .toSet(),
            buttonTransitions = elements
                .drop(1)
                .dropLast(1)
                .map { transition ->
                    transition
                        .drop(1)
                        .dropLast(1)
                        .split(',')
                        .map { it.toInt() }
                        .toSet()
                },
            targetJoltages = elements
                .last()
                .drop(1)
                .dropLast(1)
                .split(',')
                .map { it.toInt() },
        )
    }

    private fun getMinTransitionCountToTurnOn(
        targetLights: Set<Int>,
        buttonTransitions: List<Set<Int>>,
        currentLights: Set<Int> = emptySet(),
    ): Int? = when {
        currentLights == targetLights -> 0
        buttonTransitions.isEmpty() -> null
        else -> buttonTransitions
            .withIndex()
            .mapNotNull {
                getMinTransitionCountToTurnOn(
                    targetLights = targetLights,
                    buttonTransitions = buttonTransitions.drop(it.index + 1),
                    currentLights = currentLights.asLightsWithAppliedButton(it.value),
                )
            }
            .minOfOrNull { it + 1 }
    }

    private fun Set<Int>.asLightsWithAppliedButton(transitions: Set<Int>): Set<Int> =
        (this - transitions) + (transitions - this)

    private fun getMinTransitionCountToMatchJoltage(
        targetJoltages: List<Int>,
        buttonTransitions: List<Set<Int>>,
    ): Int {
        TODO()
    }

    private fun List<Int>.asJoltagesWithAppliedButton(transitions: Set<Int>): List<Int> = this
        .mapIndexed { index, joltage ->
            joltage + when (index) {
                in transitions -> 1
                else -> 0
            }
        }

    private data class Machine(
        val targetLights: Set<Int>,
        val buttonTransitions: List<Set<Int>>,
        val targetJoltages: List<Int>,
    )
}
