package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay

class Day08(config: Config) : AocDay(config) {

    private val nodeRegex = Regex("""^([A-Z]{3}) = \(([A-Z]{3}), ([A-Z]{3})\)$""")

    override fun part1(): String {
        val (instructions, nodes) = cachedInput.parse()
        val startNode = "AAA"
        val endNode = "ZZZ"
        check(startNode in nodes)
        check(endNode in nodes)
        return instructions
            .runningFold(State1(nodes[startNode]!!, 0)) { state, instruction ->
                val newNode = when (instruction) {
                    'L' -> state.currentNode.left
                    'R' -> state.currentNode.right
                    else -> error("undefined instruction $instruction")
                }
                State1(nodes[newNode]!!, state.stepsTaken + 1)
            }
            .first { state -> state.currentNode.name == endNode }
            .stepsTaken
            .toString()
    }

    override fun part2(): String {
        // TODO
        return ""
    }

    private fun List<String>.parse(): Pair<Sequence<Char>, Map<String, Node>> {
        val instructions = this[0].toList()
        val repeatingInstructions = sequence {
            while (true) yieldAll(instructions)
        }
        val nodes = drop(2).map { it.parseNode() }.associateBy(Node::name)
        return repeatingInstructions to nodes
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

    data class State1(
        val currentNode: Node,
        val stepsTaken: Int,
    )

    data class State2(
        val currentNodes: List<Node>,
        val stepsTaken: Int,
    )
}
