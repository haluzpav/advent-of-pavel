package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay

class Day19(override val config: Year2022Config) : AocDay(config) {

    private val inputRegex = Regex(
        """
            ^Blueprint ([0-9]+):
            Each ore robot costs ([0-9]+) ore.
            Each clay robot costs ([0-9]+) ore.
            Each obsidian robot costs ([0-9]+) ore and ([0-9]+) clay.
            Each geode robot costs ([0-9]+) ore and ([0-9]+) obsidian.$
        """.trimIndent().replace("\n", " ")
    )

    override fun part1(): String = parseBlueprints()
        .sumOf { it.id * calcMaxGeodes(it, minutes = 24, pruningSlope = 0.5) }
        .toString()

    override fun part2(): String = parseBlueprints()
        .take(3)
        .fold(1) { acc, blueprint -> acc * calcMaxGeodes(blueprint, minutes = 32, pruningSlope = 1.4) }
        .toString()

    fun parseBlueprints(): Sequence<Blueprint> = input.map { line ->
        val ints = inputRegex.matchEntire(line)!!.groupValues.drop(1).map { it.toInt() }
        Blueprint(
            id = ints[0],
            robots = listOf(
                Robot(
                    produces = Resource.Ore,
                    costs = listOf(ints[1], 0, 0, 0),
                ),
                Robot(
                    produces = Resource.Clay,
                    costs = listOf(ints[2], 0, 0, 0),
                ),
                Robot(
                    produces = Resource.Obsidian,
                    costs = listOf(ints[3], ints[4], 0, 0),
                ),
                Robot(
                    produces = Resource.Geode,
                    costs = listOf(ints[5], 0, ints[6], 0),
                ),
            ),
        )
    }

    fun calcMaxGeodes(blueprint: Blueprint, minutes: Int, pruningSlope: Double): Int {
        check(blueprint.robots.size == Resource.entries.size)
        check(blueprint.robots.zip(Resource.entries) { robot, resource -> robot.produces == resource }.all { it })
        val zeroResources = List(Resource.entries.size) { 0 }
        val startNode = Node(
            robotCounts = List(blueprint.robots.size) { if (it == 0) 1 else 0 },
            resourceCounts = zeroResources,
            skippedRobots = emptySet(),
            performance = 0, // whatever
        )
        val nodeComparator: (Node, Node) -> Int = { p0, p1 ->
            // effectively make the Set based only on Node.resourceCounts and Node.robotCounts
            sequence {
                for (i in Resource.entries.indices) yield(p0.resourceCounts[i].compareTo(p1.resourceCounts[i]))
                for (i in blueprint.robots.indices) yield(p0.robotCounts[i].compareTo(p1.robotCounts[i]))
            }.firstOrNull { it != 0 } ?: 0
        }
        var leafNodes = sortedSetOf<Node>(nodeComparator, startNode)
        val maxResourcesForRobot = Resource.entries.map { resource ->
            when (resource) {
                Resource.Geode -> Int.MAX_VALUE
                else -> blueprint.robots.maxOf { it.costs[resource.ordinal] }
            }
        }
        var totalPruned = 0
        for (minute in 1..minutes) {
            val minPerformanceCoefficient = (pruningSlope / minutes * minute).coerceIn(0.0..0.9)
            val maxPerformance = leafNodes.maxOf { it.performance }
            listOf(
                "blueprint ${blueprint.id}",
                "minute $minute",
                "leaf nodes ${leafNodes.size}",
                "lastMinuteMaxPerformance $maxPerformance",
                "minPerformanceCoefficient %.2f".format(minPerformanceCoefficient),
            ).joinToString().also { if (config.log) println(it) }

            val newLeafNodes = sortedSetOf<Node>(nodeComparator)
            for (node in leafNodes) {
                if (minute > 13 && node.performance < maxPerformance * minPerformanceCoefficient) {
                    totalPruned++
                    continue
                }
                val newActions = if (minute < minutes) {
                    val buildRobotActions = blueprint.robots
                        .filterIndexed { index, robot ->
                            index !in node.skippedRobots
                                && node.robotCounts[index] < maxResourcesForRobot[robot.produces.ordinal]
                                && haveEnoughQuantities(robot.costs, node.resourceCounts)
                        }
                        .map { FactoryAction.Build(it) }
                    val skippedRobots = buildRobotActions.map { blueprint.robots.indexOf(it.robot) }.toSet()
                    listOf(FactoryAction.Wait(skippedRobots)) + buildRobotActions
                } else {
                    listOf(null)
                }
                newLeafNodes += newActions.map { action ->
                    val resourceCounts = Resource.entries.mapIndexed { index, resource ->
                        val robot = blueprint.robots[index]
                        check(robot.produces == resource)
                        val newRobotCosts = if (action is FactoryAction.Build) action.robot.costs else zeroResources
                        node.resourceCounts[index] + node.robotCounts[index] - newRobotCosts[index]
                    }
                    val builtRobotIndex = (action as? FactoryAction.Build)?.robot?.let { blueprint.robots.indexOf(it) }
                    val robotCounts = node.robotCounts.mapIndexed { index, count ->
                        if (index == builtRobotIndex) count + 1 else count
                    }
                    val performance = calcPerformance(blueprint, minutes, minute, resourceCounts, robotCounts)
                    val newSkippedRobots = if (action is FactoryAction.Wait) {
                        node.skippedRobots + action.skippedRobots
                    } else {
                        emptySet()
                    }
                    Node(
                        robotCounts = robotCounts,
                        resourceCounts = resourceCounts,
                        skippedRobots = newSkippedRobots,
                        performance = performance,
                    )
                }
            }
            leafNodes = newLeafNodes
        }
        if (config.log) println("totalPruned $totalPruned")
        return leafNodes.maxOf { it.resourceCounts[3] }
    }

    private fun calcPerformance(
        blueprint: Blueprint,
        minutes: Int,
        minute: Int,
        resourceCounts: List<Int>,
        robotCounts: List<Int>,
    ): Int {
        val resourcesValue = Resource.entries.mapIndexed { i, resource -> resource.value * resourceCounts[i] }.sum()
        val robotsValue = blueprint.robots.mapIndexed { i, robot ->
            val robotCostValue = Resource.entries.mapIndexed { j, resource -> resource.value * robot.costs[j] }.sum()
            val robotPotentialResourcesValue = robot.produces.value * (minutes - minute)
            val robotIntrinsicValue = 5 + robot.produces.value
            val robotValue = robotCostValue + robotPotentialResourcesValue + robotIntrinsicValue
            robotValue * robotCounts[i]
        }.sum()
        return resourcesValue + robotsValue
    }

    private fun haveEnoughQuantities(costs: List<Int>, stock: List<Int>): Boolean =
        Resource.entries.indices.all { stock[it] >= costs[it] }

    private data class Node(
        val robotCounts: List<Int>,
        val resourceCounts: List<Int>,
        val skippedRobots: Set<Int>,
        val performance: Int,
    )

    private sealed interface FactoryAction {
        data class Wait(
            val skippedRobots: Set<Int>,
        ) : FactoryAction

        data class Build(
            val robot: Robot,
        ) : FactoryAction
    }

    data class Blueprint(
        val id: Int,
        val robots: List<Robot>,
    )

    data class Robot(
        val produces: Resource,
        val costs: List<Int>,
    )

    enum class Resource(val value: Int) {
        Ore(1),
        Clay(2),
        Obsidian(4),
        Geode(8),
    }
}
