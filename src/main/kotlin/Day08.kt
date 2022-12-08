class Day08(filename: String) {
    private val input: List<String> = readInput(filename)
    private val treesInRow: Int = input.lastIndex
    private val dirSize = Direction.values().size

    fun part1(): Int {
        return Direction.values().map { direction ->
            val indices = indicesToLookAt(direction)
            indices.flatMap {
                buildList {
                    var m = Char.MIN_VALUE
                    for (pair in it) {
                        val (x, y) = pair
                        val h = input[x][y]
                        if (h > m) {
                            this += pair
                            m = h
                        }
                    }
                }
            }.toSet()
        }.reduce { acc, set -> acc + set }.count()
    }

    // TODO don't generate this shit when it's used just once ffs -> inline
    private fun indicesToLookAt(direction: Direction): List<List<Pos>> {
        val nextDirection = Direction.values()[(direction.ordinal - 1 + dirSize).rem(dirSize)]
        val start: Pos = when (direction) {
            Direction.N -> treesInRow to treesInRow
            Direction.E -> treesInRow to 0
            Direction.S -> 0 to 0
            Direction.W -> 0 to treesInRow
        }
        var s: Pos? = start
        val toLookAt: List<List<Pos>> = buildList {
            while (s != null) {
                this += buildList<Pos> {
                    var c: Pos? = s
                    while (c != null) {
                        this += c
                        c = nextInDirection(direction, c)
                    }
                }
                s = nextInDirection(nextDirection, s!!)
            }
        }
        return toLookAt
    }

    fun part2(): Int {
        var mv = Int.MIN_VALUE
        for (bx in 0..treesInRow) {
            for (by in 0..treesInRow) {
                val b = bx to by
                val bh = input[bx][by]
                val bv = Direction.values().map { direction ->
                    var c: Pos? = nextInDirection(direction, b)
                    var count = 0
                    while (c != null) {
                        val (x, y) = c
                        val ch = input[x][y]
                        count++
                        if (ch >= bh) break
                        c = nextInDirection(direction, c)
                    }
                    count
                }.reduce { acc, i -> acc * i }
                if (bv > mv) mv = bv
            }
        }
        return mv
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
}

private enum class Direction {
    N, E, S, W
}

typealias Pos = Pair<Int, Int>

fun main() {
    val task = Day08("Day08")
    println(task.part1())
    println(task.part2())
}
