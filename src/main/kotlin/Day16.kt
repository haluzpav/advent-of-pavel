class Day16(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    private val inputRegex = Regex("""^Valve ([A-Z]+) has flow rate=([0-9]+); tunnels? leads? to valves? (.+)$""")

    fun part1(): Int {
        var valves = parseValves()
        valves = valves.map { if (it.name == "AA") it.copy(cameFrom = listOf(-1)) else it }
        val minutes = 30
        for (minute in 1..minutes) {
            val newValves = valves.map { valve ->
                val (maxIncoming, maxIncomingFrom) = valve.leadsTo.fold(Int.MIN_VALUE to null as Int?) { (max, i), vi ->
                    val v = valves[vi]
                    if (v.cameFrom.isNotEmpty() && v.released > max) v.released to vi else max to i
                }
                val canOpenSelf = valve.cameFrom.isNotEmpty() && valve.i !in valve.opened && valve.flow > 0
                val selfReleasable = if (canOpenSelf) (minutes - minute) * valve.flow else 0
                val releasedAfterSelfOpen = valve.released + selfReleasable
                val exploredByIncoming = valve.cameFrom.isEmpty() && maxIncomingFrom != null
                val overridenByIncoming = maxIncoming > releasedAfterSelfOpen
                when {
                    exploredByIncoming || overridenByIncoming -> valve.copy(
                        cameFrom = valves[maxIncomingFrom!!].cameFrom + maxIncomingFrom,
                        opened = valves[maxIncomingFrom].opened,
                        released = maxIncoming,
                    )
                    canOpenSelf -> valve.copy(
                        opened = valve.opened + valve.i,
                        released = releasedAfterSelfOpen,
                    )
                    else -> valve
                }
            }
            valves = newValves
        }
        return valves.maxOf { it.released }
    }

    fun part2(): Int = -1

    private fun parseValves(): List<Valve> = input.mapIndexed { index, line ->
        val (name, flow, leadsTos) = inputRegex.matchEntire(line)!!.destructured
        Valve(
            i = index,
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
        val i: Int,
        val name: String,
        val flow: Int,
        val leadsTo: List<Int> = emptyList(),
        val cameFrom: List<Int> = emptyList(),
        val opened: List<Int> = emptyList(),
        val released: Int = 0,
    )
}

fun main() {
    val task = Day16("Day16")
    println(task.part1())
    println(task.part2())
}
