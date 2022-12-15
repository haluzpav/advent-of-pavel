import kotlin.math.abs

class Day15(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    private val inputLineRegex = Regex("""^.*x=(-?[0-9]+), y=(-?[0-9]+).+x=(-?[0-9]+), y=(-?[0-9]+).*$""")

    fun part1(row: Int): Int = parseSensors()
        .map { sensor ->
            val (x, y) = sensor.pos
            val (bx, by) = sensor.closestBeacon
            val dToRow = abs(row - y)
            val halfIntersectSize = sensor.distance - dToRow
            (x - halfIntersectSize..x + halfIntersectSize).toSet()
                .let { if (by == row) it - bx else it }
        }
        .reduce { acc, longs -> acc.union(longs) }
        .count()

    fun part2(max: Int): Long {
        val sensors = parseSensors().toList()
        return sensors
            .flatMap { sensor ->
                val (x, y) = sensor.pos
                sequence {
                    val dJustOutOfRange = sensor.distance + 1
                    for (i in 0 until dJustOutOfRange) {
                        yield(x + i to y - dJustOutOfRange + i)
                        yield(x + dJustOutOfRange - i to y + i)
                        yield(x - i to y + dJustOutOfRange - i)
                        yield(x - dJustOutOfRange + i to y - i)
                    }
                }
            }
            .filter { (x, y) -> x in 0..max && y in 0..max }
            .filter { pos ->
                sensors.all { sensor -> pos.manhattanTo(sensor.pos) > sensor.distance }
            }
            .toSet()
            .single()
            .let { (x, y) -> x * 4_000_000L + y }
    }

    private fun parseSensors(): Sequence<Sensor> = input
        .map { line ->
            val (sensorX, sensorY, beaconX, beaconY) = inputLineRegex.matchEntire(line)!!
                .groupValues.drop(1).map { it.toInt() }
            val sensorPos = sensorX to sensorY
            val beaconPos = beaconX to beaconY
            Sensor(
                pos = sensorPos,
                closestBeacon = beaconPos,
                distance = sensorPos.manhattanTo(beaconPos),
            )
        }

    private data class Sensor(
        val pos: Pos,
        val closestBeacon: Pos,
        val distance: Int,
    )
}

fun main() {
    val task = Day15("Day15")
    println(task.part1(row = 2_000_000))
    println(task.part2(max = 4_000_000))
}
