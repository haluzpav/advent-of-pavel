package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.Pos
import cz.veleto.aoc.core.plus

class Day17(config: Config) : AocDay(config) {
    private val baseRocks = listOf(
        Rock(
            shape = '-',
            blocks = listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3),
        ),
        Rock(
            shape = '+',
            blocks = listOf(0 to 1, 1 to 0, 1 to 1, 1 to 2, 2 to 1),
        ),
        Rock(
            shape = 'L',
            blocks = listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2, 2 to 2),
        ),
        Rock(
            shape = 'I',
            blocks = listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0),
        ),
        Rock(
            shape = 'D',
            blocks = listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1),
        ),
    )
    private val chamberWidth = 7

    override fun part1(): String {
        val totalRocks = 2_022
        val jetMoves: List<Int> = parseJetMoves()
        val chamber: MutableList<MutableList<Char>> = mutableListOf()
        val engine = RockFallingEngine(baseRocks, jetMoves, chamber, chamberWidth)
        while (engine.stoppedRockCount < totalRocks) engine.letItFall()
        return chamber.size.toString()
    }

    override fun part2(): String {
        val totalRocks = 1_000_000_000_000
        val jetMoves: List<Int> = parseJetMoves()
        val chamber: MutableList<MutableList<Char>> = mutableListOf()
        val engine = RockFallingEngine(baseRocks, jetMoves, chamber, chamberWidth)
        val stepPeriod = baseRocks.size * jetMoves.size

        // period 1 - stabilizing
        while (engine.steps < stepPeriod || engine.fallingRock != null) engine.letItFall()
        val heightAfterFirstPeriod = chamber.size
        val rocksAfterFirstPeriod = engine.stoppedRockCount

        // period 2 - measuring
        engine.steps = 0
        while (engine.steps < stepPeriod) engine.letItFall()
        check(engine.fallingRock == null)
        val periodHeight = chamber.size - heightAfterFirstPeriod
        val periodRocks = engine.stoppedRockCount - rocksAfterFirstPeriod
        val rockCountRemainingAfterTwoPeriods = totalRocks - engine.stoppedRockCount
        val skippablePeriodCount = rockCountRemainingAfterTwoPeriods / periodRocks
        val rockCountToTopUp = rockCountRemainingAfterTwoPeriods.rem(periodRocks)

        // last incomplete period
        engine.stoppedRockCount = 0
        while (engine.stoppedRockCount < rockCountToTopUp) engine.letItFall()

        return (chamber.size + periodHeight * skippablePeriodCount).toString()
    }

    private fun parseJetMoves(): List<Int> = input.single().map { jet ->
        when (jet) {
            '>' -> 1
            '<' -> -1
            else -> error("unknown jet $jet")
        }
    }

    @Suppress("unused")
    private fun printChamber(chamber: List<List<Char>>, onlyTop: Int? = null) {
        (0 until chamberWidth)
            .joinToString(separator = "", prefix = "⎡", postfix = "⎤") { "=" }
            .also { println(it) }
        for (line in chamber.asReversed().let { if (onlyTop != null) it.take(onlyTop) else it }) {
            line.joinToString(separator = "", prefix = "|", postfix = "|")
                .also { println(it) }
        }
        if (onlyTop == null) {
            (0 until chamberWidth)
                .joinToString(separator = "", prefix = "⎣", postfix = "⎦") { "-" }
                .also { println(it) }
        }
    }

    private data class Rock(
        val shape: Char,
        val pos: Pos = -1 to -1,
        val blocks: List<Pos>,
    )

    private class RockFallingEngine(
        private val baseRocks: List<Rock>,
        private val jetMoves: List<Int>,
        private val chamber: MutableList<MutableList<Char>>,
        private val chamberWidth: Int,
    ) {
        private var lastRockIndex = baseRocks.lastIndex
        private var lastJetIndex = jetMoves.lastIndex
        var fallingRock: Rock? = null
            private set
        var stoppedRockCount = 0
        var steps = 0

        private val fallMove: Pos = -1 to 0

        fun letItFall() {
            var rock: Rock = fallingRock ?: (lastRockIndex + 1).rem(baseRocks.size)
                .also { lastRockIndex = it }
                .let { baseRocks[it].copy(pos = chamber.size + 3 to 2) }
            val jetMove = (lastJetIndex + 1).rem(jetMoves.size)
                .also { lastJetIndex = it }
                .let { 0 to jetMoves[it] }
            rock = if (isCollision(rock, jetMove)) {
                rock
            } else {
                rock.copy(pos = rock.pos + jetMove)
            }
            fallingRock = if (isCollision(rock, fallMove)) {
                stopRock(rock)
                stoppedRockCount += 1
                null
            } else {
                rock.copy(pos = rock.pos + fallMove)
            }
            steps++
        }

        private fun isCollision(rock: Rock, move: Pos): Boolean {
            val newRockPos = rock.pos + move
            return rock.blocks.map { it + newRockPos }.all { (x, y) ->
                y in 0 until chamberWidth && (x > chamber.lastIndex || (x >= 0 && chamber[x][y] == ' '))
            }.not()
        }

        private fun stopRock(rock: Rock) {
            rock.blocks.map { it + rock.pos }.forEach { (x, y) ->
                while (x > chamber.lastIndex) chamber += MutableList(chamberWidth) { ' ' }
                check(chamber[x][y] == ' ')
                chamber[x][y] = rock.shape
            }
        }
    }
}
