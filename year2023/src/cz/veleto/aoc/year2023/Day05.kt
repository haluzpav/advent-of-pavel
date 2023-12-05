package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay

class Day05(config: Config) : AocDay(config) {

    data class Problem(
        val rawSeeds: List<Long>, // for part 1
        val seedRanges: List<LongRange>, // for part 2
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
        val locations = problem.mapSeedsToLocations()
        return locations.min().toString()
    }

    override fun part2(): String {
        val problem = input.parse()
        val locationRanges = problem.mapSeedRangesToLocations()
        return locationRanges.minOf { it.first }.toString()
    }

    private fun Sequence<String>.parse(): Problem {
        val iterator = iterator()
        val rawSeeds = iterator.next().drop(7).split(' ').map { it.toLong() }
        val seedRanges = rawSeeds.chunked(2) { (start, length) -> start..<start + length }
        iterator.next()
        val maps = buildList {
            while (iterator.hasNext()) {
                this += iterator.parseMap()
            }
        }
        return Problem(
            rawSeeds = rawSeeds,
            seedRanges = seedRanges,
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

    private fun Problem.mapSeedsToLocations(): List<Long> = maps.fold(rawSeeds) { seeds, map ->
        seeds.map { seed ->
            map.ranges
                .firstOrNull { seed in it.source }
                ?.let { range -> range.destination.first + seed - range.source.first }
                ?: seed
        }
    }

    private fun Problem.mapSeedRangesToLocations(): List<LongRange> {
        // TODO
        return emptyList()
    }
}
