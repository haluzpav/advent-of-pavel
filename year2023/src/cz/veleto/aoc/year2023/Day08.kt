package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.allSame

class Day08(config: Config) : AocDay(config) {

    private val nodeRegex = Regex("""^(\w{3}) = \((\w{3}), (\w{3})\)$""")

    override fun part1(): String {
        val (instructions, nodes) = cachedInput.parse()
        val startState = State(
            currentNode = nodes["AAA"]!!,
            stepsTaken = 0,
        )
        return instructions
            .runningFold(startState) { state, instruction ->
                State(
                    currentNode = state.currentNode.navigateToNextNode(nodes, instruction),
                    stepsTaken = state.stepsTaken + 1,
                )
            }
            .first { it.isEndState(allZs = true) }
            .stepsTaken
            .toString()
    }

    override fun part2(): String {
        val (instructions, nodes) = cachedInput.parse()
        val startNodes = nodes.values.filter { it.name.endsWith('A') }.also { check(it.isNotEmpty()) }

        val cycleLengths = startNodes
            .map { startNode ->
                val startState = State(
                    currentNode = startNode,
                    stepsTaken = 0,
                )
                val endStates = instructions
                    .runningFold(startState) { state, instruction ->
                        State(
                            currentNode = state.currentNode.navigateToNextNode(nodes, instruction),
                            stepsTaken = state.stepsTaken + 1,
                        )
                    }
                    .filter { it.isEndState(allZs = false) }
                    .take(10) // could be just 1 it seems, but let's check that re-visit happens every N steps
                    .toList()
                check(endStates.map { it.currentNode }.allSame())
                val firstStepsToEnd = endStates[0].stepsTaken
                val cycleLengths = endStates.windowed(2, 1) { (a, b) -> b.stepsTaken - a.stepsTaken }
                check(cycleLengths.all { it == firstStepsToEnd })
                firstStepsToEnd
            }
        println(cycleLengths)

        return ""
    }

    private fun State.isEndState(allZs: Boolean): Boolean =
        if (allZs) currentNode.name == "ZZZ" else currentNode.name.endsWith('Z')

    private fun Node.navigateToNextNode(nodes: Map<String, Node>, instruction: IndexedValue<Char>): Node {
        val newNode = when (instruction.value) {
            'L' -> left
            'R' -> right
            else -> error("undefined instruction $instruction")
        }
        return nodes[newNode]!!
    }

    private fun List<String>.parse(): Pair<Sequence<IndexedValue<Char>>, Map<String, Node>> {
        val instructions = this[0].toList().withIndex()
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

    data class State(
        val currentNode: Node,
        val stepsTaken: Int,
    )
}
