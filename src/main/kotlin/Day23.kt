class Day23(inputName: String) {
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
            println("Round $round, movedElves ${(newElves - elves).size}")
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
        val directions = Direction.values().indices.map { i ->
            Direction.values()[(i + round - 1).rem(Direction.values().size)]
        }
        val proposedMoves = mutableMapOf<Pos, List<Pos>>() // proposed pos, to proposing elves
        val surroundedElves = mutableSetOf<Pos>()
        for (elf in elves) {
            val (x, y) = elf
            val emptyDirections: List<Direction> = directions.filter { direction ->
                when (direction) {
                    Direction.North -> listOf(x - 1 to y - 1, x - 1 to y, x - 1 to y + 1)
                    Direction.South -> listOf(x + 1 to y - 1, x + 1 to y, x + 1 to y + 1)
                    Direction.West -> listOf(x - 1 to y - 1, x to y - 1, x + 1 to y - 1)
                    Direction.East -> listOf(x - 1 to y + 1, x to y + 1, x + 1 to y + 1)
                }.all { it !in elves }
            }
            val proposedMove: Pos? = emptyDirections
                .takeIf { it.size < Direction.values().size }
                ?.firstNotNullOfOrNull { direction ->
                    when (direction) {
                        Direction.North -> x - 1 to y
                        Direction.South -> x + 1 to y
                        Direction.West -> x to y - 1
                        Direction.East -> x to y + 1
                    }
                }
            if (proposedMove != null) {
                val oldProposingElves = proposedMoves[proposedMove] ?: emptyList()
                proposedMoves[proposedMove] = oldProposingElves + elf
            } else {
                surroundedElves += elf
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

    private enum class Direction {
        North, South, West, East
    }
}

fun main() {
    val task = Day23("Day23")
    println(task.part1())
    println(task.part2())
}
