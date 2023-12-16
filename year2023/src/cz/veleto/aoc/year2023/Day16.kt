package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.Pos
import cz.veleto.aoc.core.get
import cz.veleto.aoc.core.plus

class Day16(config: Config) : AocDay(config) {

    override fun part1(): String {
        return generateSequence { }
            .runningFold(State(beams = setOf(Pos(0, 0) to Direction.Right))) { state, _ ->
                val newBeams = state.beams.flatMap { step(it, cachedInput) }
                State(
                    beams = newBeams.toSet(),
                    energized = state.energized + newBeams,
                )
            }
            .zipWithNext()
            .first { (old, new) -> old.energized == new.energized }
            .first
            .energized
            .map { it.first }
            .toSet()
            .size
            .toString()
    }

    override fun part2(): String {
        // TODO
        return ""
    }

    private fun step(beam: Pair<Pos, Direction>, map: List<String>): List<Pair<Pos, Direction>> {
        val (beamPos, beamDirection) = beam
        val tile = map[beamPos]
        val right = beamPos + Pos(0, 1) to Direction.Right
        val down = beamPos + Pos(1, 0) to Direction.Down
        val left = beamPos + Pos(0, -1) to Direction.Left
        val up = beamPos + Pos(-1, 0) to Direction.Up
        val newBeams = when {
            tile == '\\' -> listOf(
                when (beamDirection) {
                    Direction.Right -> down
                    Direction.Down -> right
                    Direction.Left -> up
                    Direction.Up -> left
                }
            )

            tile == '/' -> listOf(
                when (beamDirection) {
                    Direction.Right -> up
                    Direction.Down -> left
                    Direction.Left -> down
                    Direction.Up -> right
                }
            )

            tile == '|' && (beamDirection == Direction.Right || beamDirection == Direction.Left) -> listOf(
                up,
                down,
            )

            tile == '-' && (beamDirection == Direction.Down || beamDirection == Direction.Up) -> listOf(
                right,
                left,
            )

            else -> listOf(
                when (beamDirection) {
                    Direction.Right -> right
                    Direction.Down -> down
                    Direction.Left -> left
                    Direction.Up -> up
                }
            )
        }
        return newBeams.filter { (pos, _) ->
            val (lineIndex, columnIndex) = pos
            lineIndex in map.indices && columnIndex in map[0].indices
        }
    }

    enum class Direction {
        Right, Down, Left, Up
    }

    data class State(
        val beams: Set<Pair<Pos, Direction>>,
        val energized: Set<Pair<Pos, Direction>> = beams,
    )
}
