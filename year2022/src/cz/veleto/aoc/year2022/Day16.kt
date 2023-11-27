package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay

class Day16(config: Config) : AocDay(config) {
    
    private val inputRegex = Regex("""^Valve ([A-Z]+) has flow rate=([0-9]+); tunnels? leads? to valves? (.+)$""")

    override fun part1(): String {
        val valves = parseValves()
        val startValve = valves.first { it.name == "AA" }
        var paths = listOf(
            Path(
                actions = listOf(Action.Move(from = startValve, to = startValve)),
                releasedPressure = 0,
            )
        )
        val minutes = 30
        for (minute in 1..minutes) {
            if (config.log) println("Minute $minute, considering ${paths.size} paths")
            paths = paths.flatMap { path ->
                val openValves = path.actions.filterIsInstance<Action.OpenValve>().map { it.valve }
                val newPressure = path.releasedPressure + openValves.sumOf { it.flow }
                val currentValve = path.actions.filterIsInstance<Action.Move>().last().to
                val canOpenCurrentValve = currentValve !in openValves && currentValve.flow > 0
                val justCameFrom = path.actions.last().let { if (it is Action.Move) it.from else null }
                buildList {
                    for (leadsToIndex in currentValve.leadsTo) {
                        val leadsToValve = valves[leadsToIndex]
                        if (leadsToValve == justCameFrom) continue
                        this += Path(
                            actions = path.actions + Action.Move(from = currentValve, to = leadsToValve),
                            releasedPressure = newPressure
                        )
                    }
                    if (canOpenCurrentValve) {
                        this += Path(
                            actions = path.actions + Action.OpenValve(currentValve),
                            releasedPressure = newPressure,
                        )
                    }
                }
            }
            if (minute > 5) {
                // totally legit pruning 👌 😅
                val maxPressure = paths.maxOf { it.releasedPressure }
                paths = paths.filter { it.releasedPressure > maxPressure / 2 }
            }
        }
        return paths.maxOf { it.releasedPressure }.toString()
    }

    override fun part2(): String {
        val valves = parseValves()
        val startValve = valves.first { it.name == "AA" }
        var actionNodes = listOf(
            ActionNodePair(
                me = ActionNode(action = ActionNode.Action.Move, from = null, valve = startValve),
                elephant = ActionNode(action = ActionNode.Action.Move, from = null, valve = startValve),
                releasedPressure = 0,
            ),
        )
        val minutes = 26
        for (minute in 1..minutes) {
            if (config.log) println("Minute $minute, considering ${actionNodes.size} nodes")
            actionNodes = actionNodes.flatMap { actionNodePair ->
                val openValves: List<Valve> = buildList {
                    var n: ActionNode? = actionNodePair.me
                    while (n != null) {
                        if (n.action == ActionNode.Action.Open) this += n.valve
                        n = n.from
                    }
                    n = actionNodePair.elephant
                    while (n != null) {
                        if (n.action == ActionNode.Action.Open) this += n.valve
                        n = n.from
                    }
                }
                val newPressure = actionNodePair.releasedPressure + openValves.sumOf { it.flow }
                buildList {
                    val appendableActorActionNodes: List<List<ActionNode>> = listOf(
                        actionNodePair.me,
                        actionNodePair.elephant
                    ).map { actionNode ->
                        buildList {
                            val currentValve = listOf(actionNode, actionNode.from)
                                .firstNotNullOf { if (it?.action == ActionNode.Action.Move) it.valve else null }
                            val canOpenCurrentValve = currentValve !in openValves && currentValve.flow > 0
                            val justCameFrom = actionNode.from?.valve
                            for (leadsToIndex in currentValve.leadsTo) {
                                val leadsToValve = valves[leadsToIndex]
                                if (leadsToValve == justCameFrom) continue
                                this += ActionNode(action = ActionNode.Action.Move, from = actionNode, valve = leadsToValve)
                            }
                            if (canOpenCurrentValve) this += ActionNode(
                                action = ActionNode.Action.Open,
                                from = actionNode,
                                valve = currentValve,
                            )
                        }
                    }
                    for (myActionNode in appendableActorActionNodes[0]) {
                        for (elephantActionNode in appendableActorActionNodes[1]) {
                            val bothOpeningSameValve = myActionNode.action == ActionNode.Action.Open
                                && elephantActionNode.action == ActionNode.Action.Open
                                && myActionNode.valve == elephantActionNode.valve
                            if (bothOpeningSameValve) continue
                            this += ActionNodePair(
                                me = myActionNode,
                                elephant = elephantActionNode,
                                releasedPressure = newPressure,
                            )
                        }
                    }
                }
            }
            if (minute > 5) {
                // totally legit pruning 👌 😅 I totally not spend an hour tuning 🤣
                val maxPressure = actionNodes.maxOf { it.releasedPressure }
                val minPressureToPassCoef = (0.03 * minute + 0.35).coerceIn(0.0..0.82)
                val minPressureToPass = maxPressure * minPressureToPassCoef
                actionNodes = actionNodes.filter { it.releasedPressure > minPressureToPass }
                if (config.log) println("\tpruned with coef $minPressureToPassCoef")
            }
        }
        return actionNodes.maxOf { it.releasedPressure }.toString()
    }

    private fun parseValves(): List<Valve> = input.map { line ->
        val (name, flow, leadsTos) = inputRegex.matchEntire(line)!!.destructured
        Valve(
            name = name,
            flow = flow.toInt(),
        ) to leadsTos.split(", ")
    }.toMap().let { valvesToLeadsToNames ->
        valvesToLeadsToNames.keys.map { valve ->
            valve.copy(
                leadsTo = valvesToLeadsToNames[valve]!!.map { leadsToName ->
                    valvesToLeadsToNames.keys.indexOfFirst { it.name == leadsToName }
                },
            )
        }
    }

    private data class Valve(
        val name: String,
        val flow: Int,
        val leadsTo: List<Int> = emptyList(),
    )

    private sealed interface Action {
        data class Move(
            val from: Valve,
            val to: Valve,
        ) : Action {
            override fun toString(): String = "move to ${to.name}"
        }

        data class OpenValve(
            val valve: Valve,
        ) : Action {
            override fun toString(): String = "open ${valve.name}"
        }
    }

    private data class Path(
        val actions: List<Action>,
        val releasedPressure: Int,
    )

    private data class ActionNode(
        val action: Action,
        val from: ActionNode?,
        val valve: Valve,
    ) {
        override fun toString(): String = when (action) {
            Action.Move -> "move to ${valve.name}"
            Action.Open -> "open ${valve.name}"
        }

        enum class Action {
            Move, Open
        }
    }

    private data class ActionNodePair(
        val me: ActionNode,
        val elephant: ActionNode,
        val releasedPressure: Int,
    )
}
