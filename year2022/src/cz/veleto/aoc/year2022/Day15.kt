package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.Pos
import cz.veleto.aoc.core.manhattanTo
import kotlin.math.abs

class Day15(override val config: Year2022Config) : AocDay(config) {

    private val inputLineRegex = Regex("""^.*x=(-?[0-9]+), y=(-?[0-9]+).+x=(-?[0-9]+), y=(-?[0-9]+).*$""")

    override fun part1(): String = parseSensors()
        .map { sensor ->
            val row = config.day15part1row
            val (x, y) = sensor.pos
            val (bx, by) = sensor.closestBeacon
            val dToRow = abs(row - y)
            val halfIntersectSize = sensor.distance - dToRow
            (x - halfIntersectSize..x + halfIntersectSize).toSet()
                .let { if (by == row) it - bx else it }
        }
        .reduce { acc, longs -> acc.union(longs) }
        .count()
        .toString()

    override fun part2(): String {
        val max = config.day15part2max
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
            .toString()
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
