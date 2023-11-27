package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.Pos
import cz.veleto.aoc.core.get
import cz.veleto.aoc.core.rotateBy

class Day08(config: Config) : AocDay(config) {
    private val treesInRow: Int = cachedInput.lastIndex

    override fun part1(): String {
        val visiblePoses = mutableSetOf<Pos>()
        for (direction in Direction.entries) {
            val sideStepDirection = direction.rotateBy(-1)
            var rowStart: Pos? = when (direction) {
                Direction.N -> treesInRow to treesInRow
                Direction.E -> treesInRow to 0
                Direction.S -> 0 to 0
                Direction.W -> 0 to treesInRow
            }
            while (rowStart != null) {
                var pos: Pos? = rowStart
                var m = Char.MIN_VALUE
                while (pos != null) {
                    val h = cachedInput[pos]
                    if (h > m) {
                        visiblePoses += pos
                        m = h
                    }
                    pos = nextInDirection(direction, pos)
                }
                rowStart = nextInDirection(sideStepDirection, rowStart)
            }
        }
        return visiblePoses.count().toString()
    }

    override fun part2(): String {
        var mv = Int.MIN_VALUE
        for (bx in 0..treesInRow) {
            for (by in 0..treesInRow) {
                val b = bx to by
                val bh = cachedInput[b]
                val bv = Direction.entries.map { direction ->
                    var c: Pos? = nextInDirection(direction, b)
                    var count = 0
                    while (c != null) {
                        val ch = cachedInput[c]
                        count++
                        if (ch >= bh) break
                        c = nextInDirection(direction, c)
                    }
                    count
                }.reduce { acc, i -> acc * i }
                if (bv > mv) mv = bv
            }
        }
        return mv.toString()
    }

    private fun nextInDirection(direction: Direction, current: Pos): Pos? {
        val (x, y) = current
        return when (direction) {
            Direction.N -> (x - 1).takeUnless { it < 0 }?.let { it to y }
            Direction.E -> (y + 1).takeUnless { it > treesInRow }?.let { x to it }
            Direction.S -> (x + 1).takeUnless { it > treesInRow }?.let { it to y }
            Direction.W -> (y - 1).takeUnless { it < 0 }?.let { x to it }
        }
    }

    private enum class Direction {
        N, E, S, W
    }
}
