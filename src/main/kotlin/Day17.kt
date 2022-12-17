class Day17(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)
    private val baseRocks = listOf(
        Rock(
            shape = '-',
            blocks = listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3),
            width = 4,
            height = 1,
        ),
        Rock(
            shape = '+',
            blocks = listOf(0 to 1, 1 to 0, 1 to 1, 1 to 2, 2 to 1),
            width = 3,
            height = 3,
        ),
        Rock(
            shape = 'L',
            blocks = listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2, 2 to 2),
            width = 3,
            height = 3,
        ),
        Rock(
            shape = 'I',
            blocks = listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0),
            width = 1,
            height = 4,
        ),
        Rock(
            shape = 'D',
            blocks = listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1),
            width = 2,
            height = 2,
        ),
    )
    private val chamberWidth = 7

    fun part1(): Int {
        val totalRocks = 2_022
        val jetMoves: List<Int> = parseJetMoves()
        var lastRockIndex = baseRocks.lastIndex
        var fallingRock: Rock? = null
        var lastJetIndex = jetMoves.lastIndex
        var stoppedRockCount = 0
        val chamber = mutableListOf<MutableList<Char>>()
        val fallMove: Pos = -1 to 0
        while (stoppedRockCount < totalRocks) {
            var rock: Rock = getFallingRockOrSpawnNew(fallingRock, lastRockIndex, chamber.size).let { (rock, rockIndex) ->
                lastRockIndex = rockIndex
                rock
            }
            lastJetIndex = getNextJetIndex(jetMoves.size, lastJetIndex)
            val jetMove: Pos = 0 to jetMoves[lastJetIndex]
            rock = if (isCollision(chamber, rock, jetMove)) rock else rock.copy(pos = rock.pos + jetMove)
            if (isCollision(chamber, rock, fallMove)) {
                stopRock(chamber, rock)
                fallingRock = null
                stoppedRockCount += 1
            } else {
                fallingRock = rock.copy(pos = rock.pos + fallMove)
            }
        }
        return chamber.size
    }

    fun part2(): Long {
        val totalRocks = 1_000_000_000_000
        val jetMoves: List<Int> = parseJetMoves()
        var lastRockIndex = baseRocks.lastIndex
        var fallingRock: Rock? = null
        var lastJetIndex = jetMoves.lastIndex
        var stoppedRockCount = 0
        val chamber = mutableListOf<MutableList<Char>>()
        val fallMove: Pos = -1 to 0
        var steps = 0
        val stepPeriod = baseRocks.size * jetMoves.size
        while (steps < stepPeriod || fallingRock != null) {
            var rock: Rock = getFallingRockOrSpawnNew(fallingRock, lastRockIndex, chamber.size).let { (rock, rockIndex) ->
                lastRockIndex = rockIndex
                rock
            }
            lastJetIndex = getNextJetIndex(jetMoves.size, lastJetIndex)
            val jetMove: Pos = 0 to jetMoves[lastJetIndex]
            rock = if (isCollision(chamber, rock, jetMove)) rock else rock.copy(pos = rock.pos + jetMove)
            if (isCollision(chamber, rock, fallMove)) {
                stopRock(chamber, rock)
                fallingRock = null
                stoppedRockCount += 1
            } else {
                fallingRock = rock.copy(pos = rock.pos + fallMove)
            }
            steps++
        }
        val heightAfterFirstPeriod = chamber.size
        val rocksAfterFirstPeriod = stoppedRockCount
        steps = 0
        while (steps < stepPeriod) {
            var rock: Rock = getFallingRockOrSpawnNew(fallingRock, lastRockIndex, chamber.size).let { (rock, rockIndex) ->
                lastRockIndex = rockIndex
                rock
            }
            lastJetIndex = getNextJetIndex(jetMoves.size, lastJetIndex)
            val jetMove: Pos = 0 to jetMoves[lastJetIndex]
            rock = if (isCollision(chamber, rock, jetMove)) rock else rock.copy(pos = rock.pos + jetMove)
            if (isCollision(chamber, rock, fallMove)) {
                stopRock(chamber, rock)
                fallingRock = null
                stoppedRockCount += 1
            } else {
                fallingRock = rock.copy(pos = rock.pos + fallMove)
            }
            steps++
        }
        check(fallingRock == null)
        val periodHeight = chamber.size - heightAfterFirstPeriod
        val periodRocks = stoppedRockCount - rocksAfterFirstPeriod
        val rockCountRemainingAfterTwoPeriods = totalRocks - stoppedRockCount
        val skippablePeriodCount = rockCountRemainingAfterTwoPeriods / periodRocks
        val rockCountToTopUp = rockCountRemainingAfterTwoPeriods.rem(periodRocks)
        stoppedRockCount = 0
        while (stoppedRockCount < rockCountToTopUp) {
            var rock: Rock = getFallingRockOrSpawnNew(fallingRock, lastRockIndex, chamber.size).let { (rock, rockIndex) ->
                lastRockIndex = rockIndex
                rock
            }
            lastJetIndex = getNextJetIndex(jetMoves.size, lastJetIndex)
            val jetMove: Pos = 0 to jetMoves[lastJetIndex]
            rock = if (isCollision(chamber, rock, jetMove)) rock else rock.copy(pos = rock.pos + jetMove)
            if (isCollision(chamber, rock, fallMove)) {
                stopRock(chamber, rock)
                fallingRock = null
                stoppedRockCount += 1
            } else {
                fallingRock = rock.copy(pos = rock.pos + fallMove)
            }
        }
        return chamber.size + periodHeight * skippablePeriodCount
    }

    private fun parseJetMoves(): List<Int> = input.single().map { jet ->
        when (jet) {
            '>' -> 1
            '<' -> -1
            else -> error("unknown jet $jet")
        }
    }

    private fun getFallingRockOrSpawnNew(fallingRock: Rock?, lastRockIndex: Int, chamberSize: Int): Pair<Rock, Int> =
        fallingRock?.let { it to lastRockIndex } ?: run {
            val newRockIndex = (lastRockIndex + 1).rem(baseRocks.size)
            baseRocks[newRockIndex].copy(pos = chamberSize + 3 to 2) to newRockIndex
        }

    private fun getNextJetIndex(jetMovesCount: Int, lastJetIndex: Int): Int = (lastJetIndex + 1).rem(jetMovesCount)

    private fun isCollision(chamber: List<List<Char>>, rock: Rock, move: Pos): Boolean {
        val newRockPos = rock.pos + move
        return rock.blocks.map { it + newRockPos }.all { (x, y) ->
            y in 0 until chamberWidth && (x > chamber.lastIndex || (x >= 0 && chamber[x][y] == ' '))
        }.not()
    }

    private fun stopRock(chamber: MutableList<MutableList<Char>>, rock: Rock) {
        rock.blocks.map { it + rock.pos }.forEach { (x, y) ->
            while (x > chamber.lastIndex) chamber += MutableList(chamberWidth) { ' ' }
            check(chamber[x][y] == ' ')
            chamber[x][y] = rock.shape
        }
    }

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
        val width: Int,
        val height: Int,
    )
}

fun main() {
    val task = Day17("Day17")
    println(task.part1())
    println(task.part2())
}
