package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.Pos
import cz.veleto.aoc.core.allSame
import cz.veleto.aoc.core.flip
import cz.veleto.aoc.core.get
import cz.veleto.aoc.core.move
import cz.veleto.aoc.core.plus
import cz.veleto.aoc.core.rotateClockwise

class Day10(config: Config) : AocDay(config) {

    data class State(
        val pipeEnds: Set<PipeEnd>,
        val step: Long = 0,
    )

    data class PipeEnd(
        val pos: Pos,
        val orientation: PipeOrientation,
        val loopPoses: Set<Pos> = setOf(pos),
        val leftInsideCandidates: Set<Pos> = orientation.leftInsideCandidates.move(pos).toSet(),
        val rightInsideCandidates: Set<Pos> = orientation.rightInsideCandidates.move(pos).toSet(),
        val clockwiseQuarterRotations: Long = orientation.clockwiseQuarterRotations,
    )

    data class PipeOrientation(
        val sourceDirection: Pos,
        val targetDirection: Pos,
        val leftInsideCandidates: Set<Pos>,
        val rightInsideCandidates: Set<Pos>,
        val clockwiseQuarterRotations: Long,
    )

    object Direction {
        val N = -1 to 0
        val E = 0 to 1
        val S = 1 to 0
        val W = 0 to -1
        val NE = N + E
        val SE = S + E
        val SW = S + W
        val NW = N + W
    }

    private val pipeOrientations: Map<Char, Set<PipeOrientation>> = buildMap {
        val southToNorthOrientation = PipeOrientation(
            sourceDirection = Direction.S,
            targetDirection = Direction.N,
            leftInsideCandidates = setOf(Direction.W),
            rightInsideCandidates = setOf(Direction.E),
            clockwiseQuarterRotations = 0,
        )
        val southToEastOrientation = PipeOrientation(
            sourceDirection = Direction.S,
            targetDirection = Direction.E,
            leftInsideCandidates = setOf(Direction.W, Direction.NW, Direction.N),
            rightInsideCandidates = emptySet(),
            clockwiseQuarterRotations = 1,
        )
        val northToEastOrientation = PipeOrientation(
            sourceDirection = Direction.N,
            targetDirection = Direction.E,
            leftInsideCandidates = emptySet(),
            rightInsideCandidates = setOf(Direction.W, Direction.SW, Direction.S),
            clockwiseQuarterRotations = -1,
        )
        this['|'] = setOf(southToNorthOrientation, southToNorthOrientation.rotate().rotate())
        this['-'] = setOf(southToNorthOrientation.rotate(), southToNorthOrientation.rotate().rotate().rotate())
        this['F'] = setOf(southToEastOrientation, northToEastOrientation.rotate())
        this['7'] = setOf(southToEastOrientation.rotate(), northToEastOrientation.rotate().rotate())
        this['J'] = setOf(southToEastOrientation.rotate().rotate(), northToEastOrientation.rotate().rotate().rotate())
        this['L'] = setOf(southToEastOrientation.rotate().rotate().rotate(), northToEastOrientation)
    }

    override fun part1(): String = findEndState(cachedInput)
        .step
        .toString()

    override fun part2(): String = findEndState(cachedInput)
        .findAllInsides()
        .toString()

    private fun findEndState(field: Field): State = generateSequence { }
        .runningFold(State(field.findStartEnds())) { state, _ ->
            State(
                pipeEnds = state.pipeEnds.map { it.move(field) }.toSet(),
                step = state.step + 1,
            )
        }
        .first { it.pipeEnds.map(PipeEnd::pos).allSame() && field[it.pipeEnds.first().pos] != 'S' }

    private fun State.findAllInsides(): Int {
        val insideCandidates = pipeEnds.getInsideCandidates()
        val loopPoses = pipeEnds.flatMap { it.loopPoses }.toSet()
        val inside = insideCandidates - loopPoses
        val allInside = expandInside(inside, loopPoses)
        return allInside.size
    }

    // hard-code FTW
    private fun Field.findStartEnds(): Set<PipeEnd> = when {
        getOrNull(17)?.getOrNull(83) == 'S' -> createInitialPipeEnds(17 to 83, 'J')
        getOrNull(1)?.getOrNull(1) == 'S' -> createInitialPipeEnds(1 to 1, 'F')
        getOrNull(2)?.getOrNull(0) == 'S' -> createInitialPipeEnds(2 to 0, 'F')
        getOrNull(4)?.getOrNull(12) == 'S' -> createInitialPipeEnds(4 to 12, 'F')
        getOrNull(0)?.getOrNull(4) == 'S' -> createInitialPipeEnds(0 to 4, '7')
        else -> error("unknown field")
    }

    private fun createInitialPipeEnds(pos: Pos, pipe: Char): Set<PipeEnd> = pipeOrientations[pipe]!!
        .map { PipeEnd(pos = pos, orientation = it) }
        .toSet()

    private fun Set<PipeEnd>.getInsideCandidates(): Set<Pos> {
        check(size == 2)
        val (end1, end2) = toList()
        val firstRight = end1.clockwiseQuarterRotations - end2.clockwiseQuarterRotations > 0
        return (if (firstRight) end1.rightInsideCandidates else end1.leftInsideCandidates) +
            (if (firstRight) end2.leftInsideCandidates else end2.rightInsideCandidates)
    }

    private fun expandInside(insideBoundary: Set<Pos>, loopPoses: Set<Pos>): Set<Pos> {
        return generateSequence { }
            .runningFold(insideBoundary) { inside, _ ->
                val expanded = inside.flatMap { insidePos ->
                    listOf(Direction.N, Direction.E, Direction.S, Direction.W).move(insidePos)
                }.toSet()
                inside + expanded - loopPoses
            }
            .zipWithNext()
            .first { (previous, next) -> previous.size == next.size }
            .second
    }

    private fun PipeEnd.move(field: Field): PipeEnd {
        val pos = pos + orientation.targetDirection
        val pipe = field[pos]
        val orientation = pipeOrientations[pipe]!!.single { it.sourceDirection == orientation.targetDirection.flip() }
        return this + PipeEnd(pos = pos, orientation = orientation)
    }

    private operator fun PipeEnd.plus(other: PipeEnd): PipeEnd = other.copy(
        loopPoses = loopPoses + other.loopPoses,
        leftInsideCandidates = leftInsideCandidates + other.leftInsideCandidates,
        rightInsideCandidates = rightInsideCandidates + other.rightInsideCandidates,
        clockwiseQuarterRotations = clockwiseQuarterRotations + other.clockwiseQuarterRotations,
    )

    private fun PipeOrientation.rotate(): PipeOrientation = PipeOrientation(
        sourceDirection = sourceDirection.rotateClockwise(),
        targetDirection = targetDirection.rotateClockwise(),
        leftInsideCandidates = leftInsideCandidates.map { it.rotateClockwise() }.toSet(),
        rightInsideCandidates = rightInsideCandidates.map { it.rotateClockwise() }.toSet(),
        clockwiseQuarterRotations = clockwiseQuarterRotations,
    )
}

private typealias Field = List<String>
