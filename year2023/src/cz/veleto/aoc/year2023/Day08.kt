package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay

class Day08(config: Config) : AocDay(config) {

    private val nodeRegex = Regex("""^(\w{3}) = \((\w{3}), (\w{3})\)$""")

    override fun part1(): String {
        val (instructions, nodes) = cachedInput.parse()
        val startState = State1(
            currentNode = nodes["AAA"]!!,
            stepsTaken = 0,
        )
        return instructions
            .runningFold(startState) { state, instruction ->
                State1(
                    currentNode = state.currentNode.navigateToNextNode(nodes, instruction),
                    stepsTaken = state.stepsTaken + 1,
                )
            }
            .first { it.isEndState() }
            .stepsTaken
            .toString()
    }

    var logged: Boolean = false

    override fun part2(): String {
        val (instructions, nodes) = cachedInput.parse()
        val startState = State2(
            currentNodes = nodes.values.filter { it.name.endsWith('A') }.also { check(it.isNotEmpty()) },
            stepsTaken = 0,
        )
        if (config.log) println("Starting at ${startState.nodeNames}")
        return instructions
            .runningFold(startState) { state, instruction ->
                val nextNodes = state.currentNodes.map { it.navigateToNextNode(nodes, instruction) }
                val stepsTaken = state.stepsTaken + 1
                val newVisitedNodes = nextNodes
                    .mapIndexed { index, node ->
                        val oldVisits = state.visitedNodes.getOrElse(node) { emptyList() }
                        val newVisit = NodeVisit(
                            startNode = startState.currentNodes[index],
                            atStep = stepsTaken,
                            atInstruction = instruction.index,
                        )
                        node to oldVisits + newVisit
                    }
                    .toMap()
                State2(
                    currentNodes = nextNodes,
                    stepsTaken = stepsTaken,
                    visitedNodes = state.visitedNodes + newVisitedNodes,
                )
            }
            .onEach { if (config.log) it.log() }
            .onEach { state ->
                if (config.log) {
                    if (!logged && state.stepsTaken == 100_000L) {
                        state.visitedNodes
                            .also { println("before ${it.size}") }
                            .filter { (node, _) -> node.isEndNode() }
                            .also { println("after ${it.size}") }
                            .forEach { (node, allVisits) ->
                                allVisits
                                    .groupBy { it.startNode }
                                    .filter { (_, visits) -> visits.size > 1 }
                                    .forEach { (startNode, visits) ->
                                        println("Found repeated visit of ${node.name} by strand ${startNode.name}, visits:")
                                        visits.zip(listOf(null) + visits).forEach { (visit, previousVisit) ->
                                            val stepsSincePrevious = previousVisit?.let { visit.atStep - it.atStep }
                                            println("\tstep ${visit.atStep}, steps since previous $stepsSincePrevious, instruction ${visit.atInstruction}")
                                        }
                                    }
                            }
                        logged = true
                    }
                }
            }
            .first { it.isEndState() }
            .stepsTaken
            .toString()
    }

    private fun State1.isEndState(): Boolean =
        currentNode.name == "ZZZ"

    private fun Node.isEndNode(): Boolean =
        name.endsWith('Z')

    private fun State2.isEndState(): Boolean =
        currentNodes.all { it.isEndNode() }

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

    private fun State2.log() {
        if (stepsTaken % 10_000_0 == 0L) {
            println("Step $stepsTaken, navigating to $nodeNames")
        }
        if (currentNodes.count { it.isEndNode() } >= 4) {
            println("Step $stepsTaken, reached 4 or more end-nodes $nodeNames")
        }
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
        val visitedNodes: Map<Node, List<NodeVisit>> = emptyMap(),
    ) {
        val nodeNames: List<String>
            get() = currentNodes.map { it.name }
    }

    /*
    when there's a new visit, check
     - if the visiting strand has the same starting node
     - if the atInstruction is the same
    if both are true, WE HAVE A LOOP! Then
     - current step - atStep = loop length
     - we also have/need: steps to reach the loop, end-nodes along the loop
    then do some mod math to quickly find when all strands reach an end-node
     */
    data class NodeVisit(
        val startNode: Node,
        val atStep: Long,
        val atInstruction: Int,
    )
}
