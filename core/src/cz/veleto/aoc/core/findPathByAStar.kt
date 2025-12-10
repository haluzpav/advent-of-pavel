package cz.veleto.aoc.core

fun <Node> findPathByAStar(
    start: Node,
    goal: Node,
    h: (Node) -> Double,
    neighbors: (Node) -> Sequence<Pair<Node, Double>>,
    openSetImpl: MutableSet<Node> = mutableSetOf(),
    logStep: Int? = null,
): Pair<List<Node>, Double> {
    check(openSetImpl.isEmpty())
    val openSet: MutableSet<Node> = openSetImpl.apply { add(start) }
    val cameFrom: MutableMap<Node, Node> = mutableMapOf()
    val gScore: MutableMap<Node, Double> = mutableMapOf(start to 0.0)
        .withDefault { Double.POSITIVE_INFINITY }
    val fScore: MutableMap<Node, Double> = mutableMapOf(start to h(start))
        .withDefault { Double.POSITIVE_INFINITY }

    fun reconstructPath(from: Node): List<Node> {
        var current = from
        val totalPath: MutableList<Node> = mutableListOf(current)
        while (current in cameFrom) {
            current = cameFrom[current]!!
            totalPath.add(0, current)
        }
        return totalPath
    }

    var i = 0

    while (openSet.isNotEmpty()) {
        val current: Node = openSet.minBy { fScore.getValue(it) } // TODO better
        if (current == goal) return reconstructPath(current) to gScore.getValue(current)
        if (logStep != null && i % logStep == 0 && i > 0) {
            println("\t[${i++}] gScore=${gScore[current]}, h=${h(current)}, openSet=${openSet.size}, current $current")
        }
        openSet.remove(current)
        for ((neighbor, d) in neighbors(current)) {
            val tentativeGScore = gScore.getValue(current) + d
            if (tentativeGScore < gScore.getValue(neighbor)) {
                cameFrom[neighbor] = current
                gScore[neighbor] = tentativeGScore
                fScore[neighbor] = tentativeGScore + h(neighbor)
                openSet += neighbor
            }
        }
    }

    error("Path not found")
}
