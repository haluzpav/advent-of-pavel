package cz.veleto.aoc.core

import java.util.TreeSet

// in JVM only because we need Min-Heap (TreeSet)
object AStar {
    private data class WrappedNode<Node>(
        val node: Node,
        val fScore: Double,
    )

    fun <Node> findPath(
        start: Node,
        goal: Node,
        h: (Node) -> Double,
        neighbors: (Node) -> Sequence<Pair<Node, Double>>,
        logStep: Int? = null,
    ): Pair<List<Node>, Double> {
        val openSet: MutableSet<WrappedNode<Node>> = TreeSet<WrappedNode<Node>>(compareBy { it.fScore })
            .apply { add(WrappedNode(start, h(start))) }
        val cameFrom: MutableMap<Node, Node> = mutableMapOf()
        val gScore: MutableMap<Node, Double> = mutableMapOf(start to 0.0)
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

        fun WrappedNode<Node>.describe(): String = "\t[${i}] " +
            "fScore=${fScore.round(3)}, " +
            "gScore=${gScore[node]!!.round(3)}, " +
            "h=${h(node).round(3)}, " +
            "openSet=${openSet.size}, " +
            "current=$node"

        while (openSet.isNotEmpty()) {
            val wrappedCurrent = openSet.first()
            val current: Node = wrappedCurrent.node
            if (current == goal) return reconstructPath(current) to gScore.getValue(current)
            if (logStep != null) {
                if (i % logStep == 0 && i > 0) println(wrappedCurrent.describe())
                i++
            }
            openSet.remove(wrappedCurrent)
            for ((neighbor, d) in neighbors(current)) {
                val tentativeGScore = gScore.getValue(current) + d
                if (tentativeGScore < gScore.getValue(neighbor)) {
                    cameFrom[neighbor] = current
                    gScore[neighbor] = tentativeGScore
                    val fScore = tentativeGScore + h(neighbor)
                    openSet += WrappedNode(neighbor, fScore)
                }
            }
        }

        error("Path not found")
    }
}
