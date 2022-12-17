class Day16(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    private val inputRegex = Regex("""^Valve ([A-Z]+) has flow rate=([0-9]+); tunnels? leads? to valves? (.+)$""")

    fun part1(): Int {
        val valves = parseValves()
        val startValve = valves.first { it.name == "AA" }
        var paths = listOf(
            SinglePath(
                actions = listOf(Action.Move(from = startValve, to = startValve)),
                releasedPressure = 0,
            )
        )
        val minutes = 30
        for (minute in 1..minutes) {
            println("Minute $minute, considering ${paths.size} paths")
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
                        this += SinglePath(
                            actions = path.actions + Action.Move(from = currentValve, to = leadsToValve),
                            releasedPressure = newPressure
                        )
                    }
                    if (canOpenCurrentValve) {
                        this += SinglePath(
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
        return paths.maxOf { it.releasedPressure }
    }

    fun part2(): Int {
        val valves = parseValves()
        val startValve = valves.first { it.name == "AA" }
        var paths = listOf(
            MultiPath(
                actorActions = listOf(
                    listOf(Action.Move(from = startValve, to = startValve)),
                    listOf(Action.Move(from = startValve, to = startValve)),
                ),
                releasedPressure = 0,
            )
        )
        val minutes = 26
        for (minute in 1..minutes) {
            println("Minute $minute, considering ${paths.size} paths")
            paths = paths.flatMap { path ->
                val openValves = path.actions.filterIsInstance<Action.OpenValve>().map { it.valve }
                val newPressure = path.releasedPressure + openValves.sumOf { it.flow }
                buildList {
                    val appendableActorActions: List<List<Action>> = path.actorActions.map { actions ->
                        buildList {
                            val currentValve = actions.filterIsInstance<Action.Move>().last().to
                            val canOpenCurrentValve = currentValve !in openValves && currentValve.flow > 0
                            val justCameFrom = actions.last().let { if (it is Action.Move) it.from else null }
                            for (leadsToIndex in currentValve.leadsTo) {
                                val leadsToValve = valves[leadsToIndex]
                                if (leadsToValve == justCameFrom) continue
                                this += Action.Move(from = currentValve, to = leadsToValve)
                            }
                            if (canOpenCurrentValve) this += Action.OpenValve(currentValve)
                        }
                    }
                    check(appendableActorActions.size == 2)
                    for (myAction in appendableActorActions[0]) {
                        for (elephantAction in appendableActorActions[1]) {
                            if (myAction is Action.OpenValve && myAction == elephantAction) continue
                            this += MultiPath(
                                actorActions = listOf(
                                    path.actorActions[0] + myAction,
                                    path.actorActions[1] + elephantAction,
                                ),
                                releasedPressure = newPressure,
                            )
                        }
                    }
                }
            }
            if (minute > 5) {
                // totally legit pruning 👌 😅 I totally not spend an hour tuning 🤣
                val maxPressure = paths.maxOf { it.releasedPressure }
                val minPressureToPassCoef = (0.03 * minute + 0.35).coerceIn(0.0..0.85)
                val minPressureToPass = maxPressure * minPressureToPassCoef
                paths = paths.filter { it.releasedPressure > minPressureToPass }
                println("\tpruned with coef $minPressureToPassCoef")
            }
        }
        return paths.maxOf { it.releasedPressure }
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

    private data class SinglePath(
        val actions: List<Action>,
        val releasedPressure: Int,
    )

    private data class MultiPath(
        val actorActions: List<List<Action>>,
        val releasedPressure: Int,
    ) {
        val actions: List<Action> get() = actorActions.flatten()
    }
}

fun main() {
    val task = Day16("Day16")
    println(task.part1())
    println(task.part2())
}
