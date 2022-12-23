class Day22(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    fun part1(): Int {
        val (tiles, walls, instructions) = parseInput()
        var pos = tiles.first()
        var direction = Direction.Right
        for (instruction in instructions) {
            if (instruction is Instruction.Forward) {
                for (step in 1..instruction.count) {
                    pos = when (val forwardPos = pos + getStepMove(direction)) {
                        in tiles -> forwardPos
                        in walls -> pos
                        else -> when (val wrapAroundPos = findWrapAroundPos(tiles, walls, pos, direction)) {
                            in tiles -> wrapAroundPos
                            in walls -> pos
                            else -> error("stepped back out of grid")
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

    fun part2(): Int = -1

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

    private sealed interface Instruction {
        data class Forward(val count: Int) : Instruction

        object TurnLeft : Instruction {
            override fun toString(): String = "TurnLeft"
        }

        object TurnRight : Instruction {
            override fun toString(): String = "TurnRight"
        }
    }

    private enum class Direction {
        Right, Down, Left, Up
    }
}

fun main() {
    val task = Day22("Day22")
    println(task.part1())
    println(task.part2())
}
