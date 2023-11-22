class Day08(inputName: String) {
    private val input: List<String> = loadInput(inputName)
    private val treesInRow: Int = input.lastIndex

    fun part1(): Int {
        val visiblePoses = mutableSetOf<Pos>()
        for (direction in Direction.values()) {
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
                    val h = input[pos]
                    if (h > m) {
                        visiblePoses += pos
                        m = h
                    }
                    pos = nextInDirection(direction, pos)
                }
                rowStart = nextInDirection(sideStepDirection, rowStart)
            }
        }
        return visiblePoses.count()
    }

    fun part2(): Int {
        var mv = Int.MIN_VALUE
        for (bx in 0..treesInRow) {
            for (by in 0..treesInRow) {
                val b = bx to by
                val bh = input[b]
                val bv = Direction.values().map { direction ->
                    var c: Pos? = nextInDirection(direction, b)
                    var count = 0
                    while (c != null) {
                        val ch = input[c]
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

    private enum class Direction {
        N, E, S, W
    }
}

fun main() {
    val task = Day08("Day08")
    println(task.part1())
    println(task.part2())
}
