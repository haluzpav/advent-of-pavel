package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay

class Day10(override val config: Year2025Config) : AocDay(config) {

    override fun part1(): String = input
        .map { line ->
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
                joltages = elements
                    .last()
                    .drop(1)
                    .dropLast(1)
                    .split(',')
                    .map { it.toInt() },
            )
        }
        .sumOf { machine ->
            getMinTransitionCount(
                targetLights = machine.targetLights,
                buttonTransitions = machine.buttonTransitions,
            )!!
        }
        .toString()

    override fun part2(): String {
        // TODO
        return ""
    }

    private fun getMinTransitionCount(
        targetLights: Set<Int>,
        buttonTransitions: List<Set<Int>>,
        currentLights: Set<Int> = emptySet(),
    ): Int? = when {
        currentLights == targetLights -> 0
        buttonTransitions.isEmpty() -> null
        else -> buttonTransitions
            .withIndex()
            .mapNotNull {
                getMinTransitionCount(
                    targetLights = targetLights,
                    buttonTransitions = buttonTransitions.drop(it.index + 1),
                    currentLights = currentLights.asLightsWithAppliedButton(it.value),
                )
            }
            .minOfOrNull { it + 1 }
    }

    private fun Set<Int>.asLightsWithAppliedButton(transitions: Set<Int>): Set<Int> =
        (this - transitions) + (transitions - this)

    private data class Machine(
        val targetLights: Set<Int>,
        val buttonTransitions: List<Set<Int>>,
        val joltages: List<Int>,
    )
}
