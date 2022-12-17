class Day16(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    private val inputRegex = Regex("""^Valve ([A-Z]+) has flow rate=([0-9]+); tunnels? leads? to valves? (.+)$""")

    fun part1(): Int {
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
            println("Minute $minute")
            if (minute > 7) {
                // totally legit pruning 👌 😅
                val maxPressure = paths.maxOf { it.releasedPressure }
                paths = paths.filter { it.releasedPressure > maxPressure / 3 }
            }
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
        }
        return paths.maxOf { it.releasedPressure }
    }

    fun part2(): Int = -1

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
}

fun main() {
    val task = Day16("Day16")
    println(task.part1())
    println(task.part2())
}
