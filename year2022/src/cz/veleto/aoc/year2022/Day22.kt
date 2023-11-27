package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.Pos
import cz.veleto.aoc.core.plus
import cz.veleto.aoc.core.readInput
import cz.veleto.aoc.core.rotateBy
import kotlin.math.roundToInt
import kotlin.math.sqrt

class Day22(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    fun part1(): Int {
        val (tiles, walls, instructions) = parseInput()
        return followInstructions(tiles, walls, instructions) { pos, direction ->
            findWrapAroundPos(tiles, walls, pos, direction) to direction
        }
    }

    fun part2(): Int {
        val (tiles, walls, instructions) = parseInput()
        val grid = tiles + walls
        val innerGridCorners = findInnerCorners(grid)
        val sews = sewCube(grid, innerGridCorners)
        return followInstructions(tiles, walls, instructions) { pos, direction ->
            sews[pos to direction]!!
        }
    }

    private fun parseInput(): Triple<Set<Pos>, Set<Pos>, List<Instruction>> {
        val tiles = mutableSetOf<Pos>()
        val walls = mutableSetOf<Pos>()
        val instructions = mutableListOf<Instruction>()
        input.forEachIndexed { x, line ->
            when {
                line.isEmpty() -> Unit
                line[0] in listOf('.', ' ', '#') -> line.forEachIndexed { y, c ->
                    when (c) {
                        ' ' -> Unit
                        '.' -> tiles += x to y
                        '#' -> walls += x to y
                        else -> error("unknown tile $c")
                    }
                }
                else -> {
                    var firstDigitIndex: Int? = null
                    for (i in line.indices) {
                        when (val c = line[i]) {
                            in '0'..'9' -> if (firstDigitIndex == null) firstDigitIndex = i
                            else -> {
                                if (firstDigitIndex != null) {
                                    instructions += Instruction.Forward(
                                        count = line.substring(firstDigitIndex until i).toInt(),
                                    )
                                    firstDigitIndex = null
                                }
                                instructions += when (c) {
                                    'R' -> Instruction.TurnRight
                                    'L' -> Instruction.TurnLeft
                                    else -> error("unknown instruction $c")
                                }
                            }
                        }
                    }
                    if (firstDigitIndex != null) {
                        instructions += Instruction.Forward(
                            count = line.substring(firstDigitIndex..line.lastIndex).toInt(),
                        )
                    }
                }
            }
        }
        return Triple(tiles, walls, instructions)
    }

    private fun followInstructions(
        tiles: Set<Pos>,
        walls: Set<Pos>,
        instructions: List<Instruction>,
        travelThroughEdge: (Pos, Direction) -> Pair<Pos, Direction>,
    ): Int {
        var pos = tiles.first()
        var direction = Direction.Right
        for (instruction in instructions) {
            if (instruction is Instruction.Forward) {
                for (step in 1..instruction.count) {
                    pos = when (val forwardPos = pos + getStepMove(direction)) {
                        in tiles -> forwardPos
                        in walls -> pos
                        else -> {
                            val (newPos, newDirection) = travelThroughEdge(pos, direction)
                            when (newPos) {
                                in tiles -> {
                                    direction = newDirection
                                    newPos
                                }
                                in walls -> pos
                                else -> error("traveled out of grid")
                            }
                        }
                    }
                }
            } else {
                direction = updateDirection(direction, instruction)
            }
        }
        val (x, y) = pos
        return 1_000 * (x + 1) + 4 * (y + 1) + direction.ordinal
    }

    private fun updateDirection(current: Direction, instruction: Instruction): Direction = when (instruction) {
        is Instruction.Forward -> 0
        Instruction.TurnLeft -> -1
        Instruction.TurnRight -> 1
    }.let { current.rotateBy(it) }

    private fun getStepMove(direction: Direction): Pos = when (direction) {
        Direction.Right -> 0 to 1
        Direction.Down -> 1 to 0
        Direction.Left -> 0 to -1
        Direction.Up -> -1 to 0
    }

    private fun findWrapAroundPos(tiles: Set<Pos>, walls: Set<Pos>, edgePos: Pos, edgeDirection: Direction): Pos {
        val backDirection = edgeDirection.rotateBy(2)
        var backPos = edgePos
        while (true) {
            val newBackPos = backPos + getStepMove(backDirection)
            if (newBackPos !in tiles && newBackPos !in walls) break
            backPos = newBackPos
        }
        return backPos
    }

    private fun findInnerCorners(grid: Set<Pos>): Set<Triple<Pos, Direction, Direction>> = buildSet {
        val neighborMoves = Direction.entries.map { getStepMove(it) }
        val diagonalNeighborMoves = Direction.entries
            .zip(Direction.entries.map { it.rotateBy(1) })
            .map { (d1, d2) -> Triple(d1, d2, getStepMove(d1) + getStepMove(d2)) }
        for (pos in grid) {
            val emptyDiagonalNeighbor = diagonalNeighborMoves.singleOrNull { pos + it.third !in grid }
            val isCorner = neighborMoves.all { pos + it in grid } && emptyDiagonalNeighbor != null
            if (isCorner) {
                val (d1, d2) = emptyDiagonalNeighbor!!
                this += Triple(pos, d1, d2)
            }
        }
    }

    private fun sewCube(
        grid: Set<Pos>,
        corners: Set<Triple<Pos, Direction, Direction>>,
    ): Map<Pair<Pos, Direction>, Pair<Pos, Direction>> = buildMap {
        check(grid.size.rem(6) == 0)
        val sidePointCount = grid.size / 6
        val edgePointCount = sqrt(sidePointCount.toDouble()).roundToInt()
        check(edgePointCount * edgePointCount == sidePointCount)
        val edgePoints = mutableListOf<Pair<Pair<Pos, Direction>, Pair<Pos, Direction>>>()
        for ((pos, d1, d2) in corners) {
            edgePoints += (pos + getStepMove(d1) to d1) to (pos + getStepMove(d2) to d2)
        }
        while (edgePoints.isNotEmpty()) {
            // leftSewSide sews on right, rightSewSide sews on left
            val (leftSewSide, rightSewSide) = edgePoints.removeFirst()
            val (leftCornerPos, leftSideDirection) = leftSewSide
            val (rightCornerPos, rightSideDirection) = rightSewSide
            val leftOfLeftSideDirection = leftSideDirection.rotateBy(-1)
            val rightOfLeftSideDirection = leftSideDirection.rotateBy(1)
            val leftOfRightSideDirection = rightSideDirection.rotateBy(-1)
            val rightOfRightSideDirection = rightSideDirection.rotateBy(1)
            // skip if already sewed up before
            if (leftCornerPos to rightOfLeftSideDirection in this) continue
            if (rightCornerPos to leftOfRightSideDirection in this) continue
            var leftEdgePos = leftCornerPos
            var rightEdgePos = rightCornerPos
            for (edgePoint in 1..edgePointCount) {
                this[leftEdgePos to rightOfLeftSideDirection] = rightEdgePos to rightOfRightSideDirection
                this[rightEdgePos to leftOfRightSideDirection] = leftEdgePos to leftOfLeftSideDirection
                if (edgePoint < edgePointCount) {
                    leftEdgePos += getStepMove(leftSideDirection)
                    rightEdgePos += getStepMove(rightSideDirection)
                }
            }
            val newLeftSide = findWhereToTurnTo(grid, leftEdgePos, leftSideDirection, isLeftSide = true)
            val newRightSide = findWhereToTurnTo(grid, rightEdgePos, rightSideDirection, isLeftSide = false)
            edgePoints += newLeftSide to newRightSide
        }
        check(size == (12 - 5) * 2 * edgePointCount)
    }

    private fun findWhereToTurnTo(
        grid: Set<Pos>,
        edgePos: Pos,
        sideDirection: Direction,
        isLeftSide: Boolean,
    ): Pair<Pos, Direction> {
        val sign = if (isLeftSide) 1 else -1
        val forwardPos = edgePos + getStepMove(sideDirection)
        check(forwardPos + getStepMove(sideDirection.rotateBy(1 * sign)) !in grid) { "there shouldn't be a corner again" }
        val turnDirection = sideDirection.rotateBy(-1 * sign)
        val turnPos = edgePos + getStepMove(turnDirection)
        return when {
            forwardPos in grid -> forwardPos to sideDirection
            turnPos in grid -> edgePos to turnDirection
            else -> error("no direction to turn into?!")
        }
    }

    private sealed interface Instruction {
        data class Forward(val count: Int) : Instruction
        data object TurnLeft : Instruction
        data object TurnRight : Instruction
    }

    private enum class Direction {
        Right, Down, Left, Up
    }
}
