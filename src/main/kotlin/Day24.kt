import java.util.PriorityQueue

class Day24(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    fun part1(): Int = goThroughValley(SnacksState.Forgotten)

    fun part2(): Int = goThroughValley(SnacksState.ReturningWith)

    private fun goThroughValley(targetSnacks: SnacksState): Int {
        val winds = parseWinds()
        val valleyRegionX = winds[Direction.Right.ordinal].indices
        val valleyRegionY = winds[Direction.Down.ordinal].indices
        val startPos = valleyRegionX.first - 1 to valleyRegionY.first
        val endPos = valleyRegionX.last + 1 to valleyRegionY.last
        val startToEndManhattan = startPos.manhattanTo(endPos)

        fun SnacksState.hiddenDistance() = (targetSnacks.ordinal - ordinal) * startToEndManhattan

        fun SnacksState.targetPos() = when (this) {
            SnacksState.Forgotten, SnacksState.ReturningWith -> endPos
            SnacksState.ReturningFor -> startPos
        }

        fun Node.priority(): Int = snacks.hiddenDistance() + pos.manhattanTo(snacks.targetPos()) + minute

        fun Node.isAtEndWithSnacks(): Boolean = pos == endPos && snacks == targetSnacks

        val nodeComparator = Comparator<Node> { node0, node1 -> node0.priority() - node1.priority() }
        val nodes = PriorityQueue(nodeComparator) // these java collections fking suck
        nodes += Node(startPos, minute = 0, snacks = SnacksState.Forgotten)
        var handledNodes = 0
        while (nodes.isNotEmpty() && !nodes.peek().isAtEndWithSnacks()) {
            val node = nodes.poll()
            val (pos, minute) = node
            if (handledNodes.rem(10_000) == 0) listOf(
                "$handledNodes handled nodes",
                "${nodes.size} nodes to explore",
                "current priority is ${node.priority()} (" +
                    listOf(
                        "${node.snacks}",
                        "distance to target ${pos.manhattanTo(node.snacks.targetPos())}",
                        "minute $minute",
                    ).joinToString() +
                    ")",
            ).joinToString().also { println(it) }
            val nextMinute = minute + 1
            if (!hasAnyWind(pos, nextMinute, winds)) nodes.addIfNotPresent(node.copy(minute = nextMinute))
            for (direction in Direction.values()) {
                val newPos = pos + getStepMove(direction)
                val (nx, ny) = newPos
                val isInValley = nx in valleyRegionX && ny in valleyRegionY
                val isAtEntrances = newPos == startPos || newPos == endPos
                if (!isInValley && !isAtEntrances) continue
                if (hasAnyWind(newPos, nextMinute, winds)) continue
                val updateSnacks = node.snacks < targetSnacks && when (node.snacks) {
                    SnacksState.Forgotten -> newPos == endPos
                    SnacksState.ReturningFor -> newPos == startPos
                    SnacksState.ReturningWith -> false
                }
                val newSnacks = node.snacks.let { if (updateSnacks) it.rotateBy(1) else it }
                nodes.addIfNotPresent(Node(newPos, nextMinute, newSnacks))
            }
            handledNodes++
        }
        println("reached end after $handledNodes nodes explored")
        return nodes.peek().minute
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

    private fun <T> PriorityQueue<T>.addIfNotPresent(item: T): Boolean = if (item !in this) add(item) else false

    private enum class Direction {
        Right, Down, Left, Up
    }

    private data class Node(
        val pos: Pos,
        val minute: Int,
        val snacks: SnacksState,
    )

    private enum class SnacksState {
        Forgotten, ReturningFor, ReturningWith
    }
}

fun main() {
    val task = Day24("Day24")
    println(task.part1())
    println(task.part2())
}
