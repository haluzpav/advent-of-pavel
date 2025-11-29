package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.Pos
import cz.veleto.aoc.core.contains
import cz.veleto.aoc.core.getWrapped
import cz.veleto.aoc.core.manhattanTo
import cz.veleto.aoc.core.plus
import cz.veleto.aoc.core.rotateBy
import java.util.PriorityQueue

class Day24(override val config: Year2022Config) : AocDay(config) {

    override fun part1(): String = goThroughValley(SnacksState.Forgotten)

    override fun part2(): String = goThroughValley(SnacksState.ReturningWith)

    private fun goThroughValley(targetSnacks: SnacksState): String {
        val winds = parseWinds()
        val valleyRegionX = winds[Direction.Right.ordinal].indices
        val valleyRegionY = winds[Direction.Down.ordinal].indices
        val valleyRegion = valleyRegionX to valleyRegionY
        val startPos = valleyRegionX.first - 1 to valleyRegionY.first
        val endPos = valleyRegionX.last + 1 to valleyRegionY.last
        val startToEndManhattan = startPos.manhattanTo(endPos)

        fun Pos.isValid(): Boolean = this in valleyRegion || this == startPos || this == endPos

        fun SnacksState.hiddenDistance() = (targetSnacks.ordinal - ordinal) * startToEndManhattan

        fun SnacksState.targetPos() = when (this) {
            SnacksState.Forgotten, SnacksState.ReturningWith -> endPos
            SnacksState.ReturningFor -> startPos
        }

        fun SnacksState.canUpdate(pos: Pos) = this < targetSnacks && when (this) {
            SnacksState.Forgotten -> pos == endPos
            SnacksState.ReturningFor -> pos == startPos
            SnacksState.ReturningWith -> false
        }

        fun Node.priority(): Int = snacks.hiddenDistance() + pos.manhattanTo(snacks.targetPos()) + minute

        fun Node.isAtEndWithSnacks(): Boolean = pos == endPos && snacks == targetSnacks

        val nodeComparator = Comparator<Node> { node0, node1 -> node0.priority() - node1.priority() }
        val nodes = PriorityQueue(nodeComparator) // these java collections fking suck
        nodes += Node(startPos, minute = 0, snacks = SnacksState.Forgotten)
        var exploredNodes = 0

        fun logState(node: Node) {
            if (exploredNodes.rem(10_000) == 0) listOf(
                "$exploredNodes explored nodes",
                "${nodes.size} nodes to explore",
                "current priority is ${node.priority()} " + listOf(
                    "${node.snacks}",
                    "distance to target ${node.pos.manhattanTo(node.snacks.targetPos())}",
                    "minute ${node.minute}",
                ).joinToString(prefix = "(", postfix = ")"),
            ).joinToString().also { println(it) }
        }

        while (nodes.isNotEmpty() && !nodes.peek().isAtEndWithSnacks()) {
            val node = nodes.poll()
            if (config.log) logState(node)
            val nextMinute = node.minute + 1
            if (!hasAnyWind(node.pos, nextMinute, winds)) nodes.addIfNotPresent(node.copy(minute = nextMinute))
            for (direction in Direction.entries) {
                val newPos = node.pos + getStepMove(direction)
                when {
                    !newPos.isValid() || hasAnyWind(newPos, nextMinute, winds) -> continue
                    node.snacks.canUpdate(newPos) -> {
                        nodes.clear() // we can just wait at entrances if needed
                        val newSnacks = node.snacks.rotateBy(1)
                        nodes += Node(newPos, nextMinute, newSnacks)
                        break
                    }
                    else -> nodes.addIfNotPresent(node.copy(pos = newPos, minute = nextMinute))
                }
            }
            exploredNodes++
        }
        if (config.log) println("reached end after $exploredNodes nodes explored")

        return nodes.peek().minute.toString()
    }

    private fun hasAnyWind(pos: Pos, minute: Int, winds: List<List<List<Boolean>>>): Boolean {
        val (x, y) = pos // x could be at entrances where there are no winds
        return Direction.entries.any { direction ->
            val directionWinds = winds[direction.ordinal]
            val minuteShift = when (direction) {
                Direction.Right, Direction.Down -> -minute
                Direction.Left, Direction.Up -> minute
            }
            when (direction) {
                Direction.Right, Direction.Left -> directionWinds.getOrNull(x)?.getWrapped(y + minuteShift) ?: false
                Direction.Down, Direction.Up -> directionWinds[y].getWrapped(x + minuteShift)
            }
        }
    }

    /** @return wind direction, to list of currents, to winds in a current */
    private fun parseWinds(): List<List<List<Boolean>>> {
        val windDirections: MutableList<MutableList<MutableList<Boolean>>> =
            MutableList(Direction.entries.size) { mutableListOf() }
        for (line in input) {
            val width = line.length - 2
            if (line[1] == '.' && line[2] == '#') { // top wall
                // init vertical currents
                for (direction in listOf(Direction.Down, Direction.Up)) {
                    windDirections[direction.ordinal] += List(width) { mutableListOf() }
                }
            }
            if (line[2] == '#') continue // top or bottom wall
            // init horizontal currents
            for (direction in listOf(Direction.Right, Direction.Left)) {
                windDirections[direction.ordinal] += mutableListOf<Boolean>()
            }
            line.substring(1..width).forEachIndexed { y, c ->
                windDirections[Direction.Right.ordinal].last() += c == '>'
                windDirections[Direction.Down.ordinal][y] += c == 'v'
                windDirections[Direction.Left.ordinal].last() += c == '<'
                windDirections[Direction.Up.ordinal][y] += c == '^'
            }
        }
        return windDirections
    }

    private fun getStepMove(direction: Direction): Pos = when (direction) {
        Direction.Right -> 0 to 1
        Direction.Down -> 1 to 0
        Direction.Left -> 0 to -1
        Direction.Up -> -1 to 0
    }

    private fun <T> PriorityQueue<T>.addIfNotPresent(item: T): Boolean = if (item !in this) add(item) else false

    private enum class Direction {
        Right, Down, Left, Up
    }

    private data class Node(
        val pos: Pos,
        val minute: Int,
        val snacks: SnacksState,
    )

    private enum class SnacksState {
        Forgotten, ReturningFor, ReturningWith
    }
}
