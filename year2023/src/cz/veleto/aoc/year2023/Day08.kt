package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay

class Day08(config: Config) : AocDay(config) {

    private val nodeRegex = Regex("""^(\w{3}) = \((\w{3}), (\w{3})\)$""")

    override fun part1(): String {
        val (instructions, nodes) = cachedInput.parse()
        val startNode = "AAA".also { check(it in nodes) }
        val endNode = "ZZZ".also { check(it in nodes) }
        val startState = State1(
            currentNode = nodes[startNode]!!,
            stepsTaken = 0,
        )

        fun State1.isEndState(): Boolean = currentNode.name == endNode
        return instructions
            .runningFold(startState) { state, instruction ->
                State1(
                    currentNode = state.currentNode.navigateToNextNode(nodes, instruction),
                    stepsTaken = state.stepsTaken + 1,
                )
            }
            .first(State1::isEndState)
            .stepsTaken
            .toString()
    }

    override fun part2(): String {
        val (instructions, nodes) = cachedInput.parse()
        val startState = State2(
            currentNodes = nodes.values.filter { it.name.endsWith('A') }.also { check(it.isNotEmpty()) },
            stepsTaken = 0,
        )

        fun Node.isEndNode(): Boolean = name.endsWith('Z')
        fun State2.isEndState(): Boolean = currentNodes.all(Node::isEndNode)

        if (config.log) println("Starting at ${startState.nodeNames}")
        return instructions
            .runningFold(startState) { state, instruction ->
                State2(
                    currentNodes = state.currentNodes.map { it.navigateToNextNode(nodes, instruction) },
                    stepsTaken = state.stepsTaken + 1,
                ).also { newState ->
                    if (config.log) {
                        if (newState.stepsTaken % 10_000_000 == 0L) {
                            println("Step ${newState.stepsTaken}, navigating to ${newState.nodeNames}")
                        }
                        if (newState.currentNodes.count { it.isEndNode() } >= 4) {
                            println("Step ${newState.stepsTaken}, found 4 or more nodes in end-state ${newState.nodeNames}")
                        }
                    }
                }
            }
            .first(State2::isEndState)
            .stepsTaken
            .toString()
    }

    private fun Node.navigateToNextNode(nodes: Map<String, Node>, instruction: Char): Node {
        val newNode = when (instruction) {
            'L' -> left
            'R' -> right
            else -> error("undefined instruction $instruction")
        }
        return nodes[newNode]!!
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
        val stepsTaken: Long,
    ) {
        val nodeNames: List<String>
            get() = currentNodes.map { it.name }
    }
}
