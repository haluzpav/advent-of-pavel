package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.Pos
import cz.veleto.aoc.core.plus
import cz.veleto.aoc.core.readInput
import cz.veleto.aoc.core.rotateBy

class Day23(inputName: String, private val log: Boolean = false) {
    private val input: Sequence<String> = readInput(inputName)

    fun part1(): Int {
        var elves = parseElves()
        for (round in 1..10) {
            elves = moveElves(round, elves)
        }
        val minX = elves.minOf { (x, _) -> x }
        val maxX = elves.maxOf { (x, _) -> x }
        val minY = elves.minOf { (_, y) -> y }
        val maxY = elves.maxOf { (_, y) -> y }
        val rectSize = (maxX - minX + 1) * (maxY - minY + 1)
        return rectSize - elves.size
    }

    fun part2(): Int {
        var elves = parseElves()
        var round = 0
        var anyElvesMoved = true
        while (anyElvesMoved) {
            round++
            val newElves = moveElves(round, elves)
            anyElvesMoved = newElves != elves
            if (log) println("Round $round, movedElves ${(newElves - elves).size}")
            elves = newElves
        }
        return round
    }

    private fun parseElves(): Set<Pair<Int, Int>> = input.flatMapIndexed { x: Int, line: String ->
        line.mapIndexedNotNull { y, c ->
            if (c == '#') x to y else null
        }
    }.toSet()

    private fun moveElves(round: Int, elves: Set<Pos>): Set<Pos> {
        // first half
        val directions = Direction.entries.map { it.rotateBy(round - 1) }
        val proposedMoves = mutableMapOf<Pos, List<Pos>>() // proposed pos, to proposing elves
        val surroundedElves = mutableSetOf<Pos>()
        for (elf in elves) {
            val emptyDirections: List<Direction> = directions.filter { direction ->
                direction.neighborFov.all { (elf + it) !in elves }
            }
            if (emptyDirections.isEmpty() || emptyDirections.size == Direction.entries.size) {
                surroundedElves += elf
            } else {
                val proposedMove = elf + emptyDirections.first().neighborFov[1]
                val oldProposingElves = proposedMoves[proposedMove] ?: emptyList()
                proposedMoves[proposedMove] = oldProposingElves + elf
            }
        }

        // second half
        val collidingElves = mutableSetOf<Pos>()
        val movedElves = mutableSetOf<Pos>()
        for ((proposedMove, proposingElves) in proposedMoves) {
            if (proposingElves.size == 1) {
                movedElves += proposedMove
            } else {
                collidingElves += proposingElves
            }
        }

        val newElves = surroundedElves + collidingElves + movedElves
        check(newElves.size == elves.size)
        return newElves
    }

    private enum class Direction(val neighborFov: List<Pos>) {
        North(listOf(-1 to -1, -1 to 0, -1 to 1)),
        South(listOf(1 to -1, 1 to 0, 1 to 1)),
        West(listOf(-1 to -1, 0 to -1, 1 to -1)),
        East(listOf(-1 to 1, 0 to 1, 1 to 1)),
    }
}
