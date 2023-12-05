package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay

class Day05(config: Config) : AocDay(config) {

    data class Problem(
        val seeds: List<Long>,
        val maps: List<Map>,
    ) {
        data class Map(
            val name: String,
            val ranges: List<Range>,
        )

        data class Range(
            val source: LongRange,
            val destination: LongRange,
        )
    }

    override fun part1(): String {
        val problem = input.parse()
        val locations = problem.mapSeedToLocation()
        return locations.min().toString()
    }

    override fun part2(): String {
        // TODO
        return ""
    }

    private fun Sequence<String>.parse(): Problem {
        val iterator = iterator()
        val seeds = iterator.next().drop(7).split(' ').map { it.toLong() }
        iterator.next()
        val maps = buildList {
            while (iterator.hasNext()) {
                this += iterator.parseMap()
            }
        }
        return Problem(
            seeds = seeds,
            maps = maps,
        )
    }

    private fun Iterator<String>.parseMap(): Problem.Map = Problem.Map(
        name = next().split(' ').first(),
        ranges = asSequence()
            .takeWhile { it.isNotBlank() }
            .map { line ->
                val (destinationStart, sourceStart, length) = line.split(' ').map { it.toLong() }
                Problem.Range(
                    source = sourceStart..<sourceStart + length,
                    destination = destinationStart..<destinationStart + length,
                )
            }
            .toList(),
    )

    private fun Problem.mapSeedToLocation(): List<Long> = maps.fold(seeds) { seeds, map ->
        seeds.map { seed ->
            map.ranges
                .firstOrNull { seed in it.source }
                ?.let { range -> range.destination.first + seed - range.source.first }
                ?: seed
        }
    }
}
