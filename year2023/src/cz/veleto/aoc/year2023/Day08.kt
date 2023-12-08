package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay

class Day08(config: Config) : AocDay(config) {

    private val nodeRegex = Regex("""^([A-Z]{3}) = \(([A-Z]{3}), ([A-Z]{3})\)$""")
    private val startNode = "AAA"
    private val endNode = "ZZZ"

    override fun part1(): String {
        val instructions = sequence {
            while (true) yieldAll(cachedInput[0].asSequence())
        }
        val nodes = cachedInput.drop(2).map { it.parseNode() }.associateBy(Node::name)
        check(startNode in nodes)
        check(endNode in nodes)
        return instructions
            .runningFold(State(nodes[startNode]!!, 0)) { state, instruction ->
                val newNode = when (instruction) {
                    'L' -> state.currentNode.left
                    'R' -> state.currentNode.right
                    else -> error("undefined instruction $instruction")
                }
                State(nodes[newNode]!!, state.stepsTaken + 1)
            }
            .first { state -> state.currentNode.name == endNode }
            .stepsTaken
            .toString()
    }

    override fun part2(): String {
        // TODO
        return ""
    }

    private fun String.parseNode(): Node {
        val (name, left, right) = nodeRegex.matchEntire(this)!!.destructured
        return Node(name, left, right)
    }

    data class Node(
        val name: String,
        val left: String,
        val right: String,
    )

    data class State(
        val currentNode: Node,
        val stepsTaken: Int,
    )
}
