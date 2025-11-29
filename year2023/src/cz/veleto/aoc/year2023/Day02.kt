package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay

class Day02(override val config: Year2023Config) : AocDay(config) {

    private val availableCubes = mapOf(
        Cube.Red to 12,
        Cube.Green to 13,
        Cube.Blue to 14,
    )

    override fun part1(): String = input
        .parse()
        .filter { (_, grabs) ->
            grabs
                .all { grab ->
                    grab.all { (cube, count) -> count <= availableCubes[cube]!! }
                }
        }
        .map(Game::first)
        .sum()
        .toString()

    override fun part2(): String = input
        .parse()
        .map { (_, grabs) ->
            grabs
                .flatMap(Grab::entries)
                .groupBy(
                    keySelector = { (cube, _) -> cube },
                    valueTransform = { (_, count) -> count },
                )
                .values
                .map { counts -> counts.max() }
                .reduce(Int::times)
        }
        .sum()
        .toString()

    private fun Sequence<String>.parse(): Sequence<Game> = map { line ->
        val (label, grabs) = line.split(": ")
        val gameId = label.drop(5).toInt()
        gameId to grabs
            .split("; ")
            .map { grab ->
                grab
                    .split(", ")
                    .associate { singleTypeGrab ->
                        val (count, type) = singleTypeGrab.split(" ")
                        val cube = Cube.entries.first { it.name.lowercase() == type }
                        cube to count.toInt()
                    }
            }
    }

    enum class Cube {
        Red, Green, Blue
    }
}

private typealias Game = Pair<Int, Grabs>
private typealias Grabs = List<Grab>
private typealias Grab = Map<Day02.Cube, Int>
