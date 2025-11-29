package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.fullyIn
import cz.veleto.aoc.core.intersect
import cz.veleto.aoc.core.shift

class Day05(override val config: Year2023Config) : AocDay(config) {

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
        ) {
            companion object {
                val StartBound = Range(
                    source = Long.MIN_VALUE..Long.MIN_VALUE,
                    destination = LongRange.EMPTY,
                )
                val EndBound = Range(
                    source = Long.MAX_VALUE..Long.MAX_VALUE,
                    destination = LongRange.EMPTY,
                )
            }
        }
    }

    override fun part1(): String {
        val problem = input.parse()
        val locations = problem.mapSeedsToLocations()
        return locations.min().toString()
    }

    override fun part2(): String {
        val problem = input.parse()
        if (config.log) {
            println("Init seeds")
            problem.seedRanges.onEach { println("\t$it") }
        }
        val locationRanges = problem.mapSeedRangesToLocations()
        return locationRanges.minOf { it.first }.toString()
    }

    private fun Sequence<String>.parse(): Problem {
        val iterator = iterator()
        val rawSeeds = iterator.next().drop(7).split(' ').map { it.toLong() }
        val seedRanges = rawSeeds.chunked(2) { (start, length) -> start..<start + length }.sortedBy { it.first }
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
            .toList()
            .sortedBy { range -> range.source.first },
    )

    private fun Problem.mapSeedsToLocations(): List<Long> = maps.fold(rawSeeds) { seeds, map ->
        seeds.map { seed ->
            map.ranges
                .firstOrNull { seed in it.source }
                ?.map(seed)
                ?: seed
        }
    }

    private fun Problem.Range.map(seed: Long): Long =
        destination.first + seed - source.first

    private fun Problem.Range.map(seedRange: LongRange): LongRange {
        if (seedRange.isEmpty()) return seedRange
        require(seedRange.fullyIn(source))
        val shift = destination.first - source.first
        return seedRange.shift(shift)
    }

    private fun Problem.mapSeedRangesToLocations(): List<LongRange> = maps
        .fold(seedRanges) { seedRanges, map ->
            if (config.log) println("Map ${map.name}")
            seedRanges
                .also { if (config.log) println("\tMapped to (debug)") }
                .flatMap { seedRange ->
                    val mapRanges = buildList {
                        add(Problem.Range.StartBound)
                        addAll(map.ranges)
                        add(Problem.Range.EndBound)
                    }
                    mapRanges
                        .windowed(2) { (mapRange, nextMapRange) ->
                            listOf(
                                mapRange.map(seedRange.intersect(mapRange.source)),
                                seedRange.intersect(mapRange.source.last + 1..<nextMapRange.source.first),
                            ).also { if (config.log) println("\t\tSeeds $seedRange, map $mapRange, new seeds $it") }
                        }
                        .flatten()
                }
                .filterNot(LongRange::isEmpty)
                .also { if (config.log) println("\tMapped to (simplified)") }
                .onEach { if (config.log) println("\t\t$it") }
        }
        .toList()
}
