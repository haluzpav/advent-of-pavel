package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.Pos
import cz.veleto.aoc.core.allSame
import cz.veleto.aoc.core.get
import cz.veleto.aoc.core.plus

class Day10(config: Config) : AocDay(config) {

    override fun part1(): String = generateSequence { }
        .runningFold(State(step = 1, poses = cachedInput.findStartNeighbors())) { state, _ ->
            val newPoses = state.poses.flatMap { pos ->
                cachedInput[pos].getNeighborDirections().map { pos + it }
            }.toSet()
            State(step = state.step + 1, poses = newPoses - state.previousPoses, previousPoses = state.poses)
        }
        .first { it.poses.allSame() }
        .step
        .toString()

    override fun part2(): String {
        // TODO
        return ""
    }

    // hard-code FTW
    private fun Field.findStartNeighbors(): Set<Pos> = when {
        getOrNull(17)?.getOrNull(83) == 'S' -> setOf(16 to 83, 17 to 82)
        getOrNull(1)?.getOrNull(1) == 'S' -> setOf(1 to 2, 2 to 1)
        getOrNull(2)?.getOrNull(0) == 'S' -> setOf(3 to 0, 2 to 1)
        getOrNull(4)?.getOrNull(12) == 'S' -> setOf(4 to 13, 5 to 12)
        getOrNull(0)?.getOrNull(4) == 'S' -> setOf(0 to 3, 1 to 4)
        else -> error("unknown field")
    }

    private fun Char.getNeighborDirections(): Set<Pos> = when (this) {
        '|' -> setOf(-1 to 0, 1 to 0)
        '-' -> setOf(0 to 1, 0 to -1)
        'L' -> setOf(-1 to 0, 0 to 1)
        'J' -> setOf(0 to -1, -1 to 0)
        '7' -> setOf(0 to -1, 1 to 0)
        'F' -> setOf(0 to 1, 1 to 0)
        'S' -> emptySet()
        else -> error("unknown pipe '$this'")
    }

    data class State(
        val step: Long,
        val poses: Set<Pos>,
        val previousPoses: Set<Pos> = emptySet(), // to prevent going back
    )
}

private typealias Field = List<String>
