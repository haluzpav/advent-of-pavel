package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.abs
import cz.veleto.aoc.core.minus
import cz.veleto.aoc.core.toIterable
import cz.veleto.aoc.core.toPos

class Day09(override val config: Year2025Config) : AocDay(config) {

    override fun part1(): String {
        val tiles = input
            .map { line ->
                line.split(',').map { it.toInt() }.toPos()
            }
            .toList()
        return tiles
            .asSequence()
            .withIndex()
            .flatMap { t1 ->
                tiles
                    .asSequence()
                    .withIndex()
                    .drop(t1.index + 1)
                    .map { t1.value to it.value }
            }
            .maxOf { (t1, t2) ->
                t1
                    .minus(t2)
                    .abs()
                    .toIterable()
                    .map { it.toLong() + 1 }
                    .reduce(Long::times)
            }
            .toString()
    }

    override fun part2(): String {
        // TODO
        return ""
    }
}
