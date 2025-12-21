package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay
import kotlin.math.pow

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
                if (config.log) println(" min transitions to match joltage = $it")
                TODO("one found, ta-da")
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
    ): Int = dfs(
        joltages = List(targetJoltages.size) { 0 },
        targetJoltages = targetJoltages,
        buttonTransitions = buttonTransitions,
    )!!

    // TODO track+log discarded hits
    private fun dfs(
        joltages: List<Int>,
        targetJoltages: List<Int>,
        buttonTransitions: List<Set<Int>>,
        discardedJoltages: MutableSet<List<Int>> = mutableSetOf(),
        depth: Int = 0,
    ): Int? {
        val log = config.verboseLog || config.log && discardedJoltages.size % 100_000 == 0
        if (log) println("dfs joltages = $joltages, discarded = ${discardedJoltages.size}".prependIndent(depth + 1))
        return when {
            joltages in discardedJoltages -> null.also {
                if (config.verboseLog) println("-> already discarded".prependIndent(depth + 2))
            }

            joltages == targetJoltages -> depth.also {
                if (config.verboseLog) println("-> reached goal".prependIndent(depth + 2))
            }

            joltages.zip(targetJoltages) { a, b -> a > b }.any { it } -> null.also { discardedJoltages.add(joltages) }
                .also {
                    if (config.verboseLog) println("-> beyond goal, discarding".prependIndent(depth + 2))
                }

            else -> buttonTransitions
                .reorder(remainingJoltages = targetJoltages.zip(joltages, Int::minus), depth = depth)
                .asSequence()
                .map { buttonTransition ->
                    dfs(
                        joltages = joltages.asJoltagesWithAppliedButton(buttonTransition),
                        targetJoltages = targetJoltages,
                        buttonTransitions = buttonTransitions,
                        discardedJoltages = discardedJoltages,
                        depth = depth + 1,
                    )
                }
                .filterNotNull()
                .firstOrNull()
                .also {
                    if (it == null) discardedJoltages.add(joltages)
                    if (config.verboseLog) println(
                        "-> ${if (it == null) "child discarded" else "child reached goal at $it"}"
                            .prependIndent(depth + 2)
                    )
                }
        }
    }

    private fun String.prependIndent(level: Int = 1, element: String = " "): String =
        prependIndent(element.repeat(level))

    // TODO also prioritize buttons which affect joltages with least buttons
    // TODO group remaining joltages so the above can take effect
    private fun List<Set<Int>>.reorder(remainingJoltages: List<Int>, depth: Int = 0): List<Set<Int>> {
        val priorityJoltagesIndices = remainingJoltages.withIndex().sortedBy { it.value }.map { it.index }
        return sortedByDescending { buttonTransition ->
            buttonTransition.sumOf { 2.0.pow(priorityJoltagesIndices.indexOf(it)) }
        }.also {
            if (config.verboseLog) println(
                """
                    reorder:
                        remainingJoltages = $remainingJoltages
                        previousButtonTransitions = $this
                        newButtonTransitions      = $it
                """.replaceIndent(" ".repeat(depth + 1))
            )
        }
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
