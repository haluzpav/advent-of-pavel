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
            shape = '⏌',
            blocks = listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2, 2 to 2),
            width = 3,
            height = 3,
        ),
        Rock(
            shape = '|',
            blocks = listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0),
            width = 1,
            height = 4,
        ),
        Rock(
            shape = '⬜',
            blocks = listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1),
            width = 2,
            height = 2,
        ),
    )
    private val chamberWidth = 7

    fun part1(): Long = letTheRocksFall(totalRocks = 2_022)

    fun part2(): Long = letTheRocksFall(totalRocks = 1_000_000_000_000)

    private fun letTheRocksFall(totalRocks: Long): Long {
        val jetMoves: List<Int> = input.single().map { jet ->
            when (jet) {
                '>' -> 1
                '<' -> -1
                else -> error("unknown jet $jet")
            }
        }
        var lastRockIndex = baseRocks.lastIndex
        var fallingRock: Rock? = null
        var lastJetIndex = jetMoves.lastIndex
        var stoppedRockCount = 0
        val chamber = mutableListOf<MutableList<Boolean>>()
        val fallMove: Pos = -1 to 0
        while (stoppedRockCount < totalRocks) {
            var rock: Rock = fallingRock ?: run {
                val i = (lastRockIndex + 1).rem(baseRocks.size)
                    .also { lastRockIndex = it }
                baseRocks[i].copy(pos = chamber.size + 3 to 2)
            }
            val jetMove: Pos = run {
                val i = (lastJetIndex + 1).rem(jetMoves.size)
                    .also { lastJetIndex = it }
                0 to jetMoves[i]
            }
            rock = if (isCollision(chamber, rock, jetMove)) rock else rock.copy(pos = rock.pos + jetMove)
            if (isCollision(chamber, rock, fallMove)) {
                stopRock(chamber, rock)
                fallingRock = null
                stoppedRockCount += 1
                // printChamber(chamber)
                if (stoppedRockCount.rem(1_000_000) == 0) println(stoppedRockCount)
                check(chamber.size / 2 < Int.MAX_VALUE)
            } else {
                fallingRock = rock.copy(pos = rock.pos + fallMove)
            }
        }
        return chamber.size.toLong()
    }

    private fun isCollision(chamber: List<List<Boolean>>, rock: Rock, move: Pos): Boolean {
        val newRockPos = rock.pos + move
        return rock.blocks.map { it + newRockPos }.all { (x, y) ->
            y in 0 until chamberWidth && (x > chamber.lastIndex || (x >= 0 && !chamber[x][y]))
        }.not()
    }

    private fun stopRock(chamber: MutableList<MutableList<Boolean>>, rock: Rock) {
        rock.blocks.map { it + rock.pos }.forEach { (x, y) ->
            while (x > chamber.lastIndex) chamber += MutableList(chamberWidth) { false }
            check(!chamber[x][y])
            chamber[x][y] = true
        }
    }

    private fun printChamber(chamber: List<List<Boolean>>) {
        (0 until chamberWidth)
            .joinToString(separator = "", prefix = "⎡", postfix = "⎤") { "-" }
            .also { println(it) }
        for (line in chamber.asReversed()) {
            line.joinToString(separator = "", prefix = "|", postfix = "|") { if (it) "█" else " " }
                .also { println(it) }
        }
        (0 until chamberWidth)
            .joinToString(separator = "", prefix = "⎣", postfix = "⎦") { "-" }
            .also { println(it) }
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
