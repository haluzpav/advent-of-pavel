package cz.veleto.aoc.year2022

class Day12(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    fun part1(): Int {
        val (nodes, start, end) = parseNodes()
        start.currentShortestPath = 0
        val nodesToHandle = mutableListOf(start)
        while (nodesToHandle.isNotEmpty()) {
            val node = nodesToHandle.removeFirst()
            if (node == end) break
            val neighbors = findNeighbors(nodes, node)
                .filter { it.height - node.height <= 1 }
            explore(node, neighbors, nodesToHandle)
        }
        return end.currentShortestPath!!
    }

    fun part2(): Int {
        val (nodes, _, end) = parseNodes()
        end.currentShortestPath = 0
        val nodesToHandle = mutableListOf(end)
        while (nodesToHandle.isNotEmpty()) {
            val node = nodesToHandle.removeFirst()
            if (node.height == 'a') return node.currentShortestPath!!
            val neighbors = findNeighbors(nodes, node)
                .filter { node.height - it.height <= 1 }
            explore(node, neighbors, nodesToHandle)
        }
        error("no 'a' found")
    }

    private fun parseNodes(): Triple<List<List<Node>>, Node, Node> {
        var start: Node? = null
        var end: Node? = null
        val nodes = input
            .mapIndexed { x, s ->
                s.mapIndexed { y, c ->
                    when (c) {
                        'S' -> Node(
                            pos = x to y,
                            height = 'a',
                        ).also { start = it }
                        'E' -> Node(
                            pos = x to y,
                            height = 'z',
                        ).also { end = it }
                        else -> Node(
                            pos = x to y,
                            height = c,
                        )
                    }
                }
            }
            .toList()
        return Triple(nodes, start!!, end!!)
    }

    private fun findNeighbors(nodes: List<List<Node>>, current: Node): Sequence<Node> {
        val (x, y) = current.pos
        return sequenceOf(
            x - 1 to y,
            x to y - 1,
            x + 1 to y,
            x to y + 1,
        ).filter { (x, y) -> x in 0..nodes.lastIndex && y in 0..nodes[0].lastIndex }
            .map { (x, y) -> nodes[x][y] }
    }

    private fun explore(current: Node, neighbors: Sequence<Node>, nodesToHandle: MutableList<Node>) {
        val nextPathLength = current.currentShortestPath!! + 1
        neighbors
            .filter { it.currentShortestPath?.let { it > nextPathLength } ?: true }
            .forEach {
                nodesToHandle.remove(it)
                it.currentShortestPath = nextPathLength
                it.previousInPath = current
                nodesToHandle.add(it)
            }
    }

    private data class Node(
        val pos: Pos,
        val height: Char,
        var currentShortestPath: Int? = null,
        var previousInPath: Node? = null,
    )
}

fun main() {
    val task = Day12("Day12")
    println(task.part1())
    println(task.part2())
}
