class Day24(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    fun part1(): Int {
        val winds = parseWinds()
        val valleyRegionX = winds[Direction.Right.ordinal].indices
        val valleyRegionY = winds[Direction.Down.ordinal].indices
        val startPos = valleyRegionX.first - 1 to valleyRegionY.first
        val endPos = valleyRegionX.last + 1 to valleyRegionY.last
        var minute = 0
        var poses = listOf(startPos)
        while (minute < Int.MAX_VALUE && endPos !in poses) {
            minute++
            println("Minute $minute, poses ${poses.size}")
            val newPoses = mutableListOf<Pos>()
            for (pos in poses) {
                if (!hasAnyWind(pos, minute, winds)) newPoses += pos
                for (direction in Direction.values()) {
                    val newPos = pos + getStepMove(direction)
                    val (nx, ny) = newPos
                    val isInValley = nx in valleyRegionX && ny in valleyRegionY
                    val isAtEntrances = newPos == startPos || newPos == endPos
                    if (!isInValley && !isAtEntrances) continue
                    if (hasAnyWind(newPos, minute, winds)) continue
                    newPoses += newPos
                }
            }
            poses = newPoses
        }
        return minute
    }

    private fun hasAnyWind(pos: Pos, minute: Int, winds: List<List<List<Boolean>>>): Boolean {
        val (x, y) = pos
        return Direction.values().any { direction ->
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

    fun part2(): Int = -1

    /** @return wind direction, to list of currents, to winds in a current */
    private fun parseWinds(): List<List<List<Boolean>>> {
        val windDirections: MutableList<MutableList<MutableList<Boolean>>> =
            MutableList(Direction.values().size) { mutableListOf() }
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

    private enum class Direction {
        Right, Down, Left, Up
    }
}

fun main() {
    val task = Day24("Day24")
    println(task.part1())
    println(task.part2())
}
