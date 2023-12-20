package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.Pos
import cz.veleto.aoc.core.plus

class Day18(config: Config) : AocDay(config) {

    override fun part1(): String {
        val startState = State()
        val endState = input
            .map { line ->
                val (rawDirection, rawStepCount, _) = line.split(' ')
                val direction = when (rawDirection.single()) {
                    'U' -> Direction.Up
                    'R' -> Direction.Right
                    'D' -> Direction.Down
                    'L' -> Direction.Left
                    else -> error("unknown direction $rawDirection")
                }
                direction to rawStepCount.toInt()
            }
            .fold(startState) { directionState, (direction, stepCount) ->
                (0..<stepCount).fold(directionState) { repeatState, _ ->
                    val newPos = repeatState.pos + direction
                    check(repeatState.pos !in repeatState.directions)
                    State(newPos, repeatState.directions + (repeatState.pos to direction))
                }
            }
        check(endState.pos == startState.pos)
        val lines = endState.directions.entries
            .groupBy { (pos, _) -> pos.first }
            .values
            .map { posDirections ->
                posDirections
                    .sortedBy { (pos, _) -> pos.second }
                    .associate { (pos, direction) -> pos.second to direction }
            }
        val lineCounts = lines
            .map { posDirections ->
                (posDirections.keys.first()..posDirections.keys.last())
                    .fold(State2()) { state, columnIndex ->
                        val direction = posDirections[columnIndex]
                        when {
                            !state.counting && direction != null -> state.copy(
                                counting = true, startDirection = direction, counted = state.counted + 1,
                            )
                            state.counting -> when (direction) {
                                Direction.Up -> {
                                    lines[]
                                }
                                null -> state.copy(counted = state.counted + 1)
                                else -> error("unknown direction $direction")
                            }
                            else -> state
                        }
                    }
                    .counted
            }
        return lineCounts
            .sum()
            .toString()
    }

    override fun part2(): String {
        // TODO
        return ""
    }

    object Direction {
        val Up = -1 to 0
        val Right = 0 to 1
        val Down = 1 to 0
        val Left = 0 to -1
    }

    data class State(
        val pos: Pos = 0 to 0,
        val directions: Map<Pos, Pos> = emptyMap(),
    )

    data class State2(
        val counting: Boolean = false,
        val startDirection: Pos? = null,
        val counted: Int = 0,
    )
}
