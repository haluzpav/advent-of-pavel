package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.euclidTo
import cz.veleto.aoc.core.toPos3
import kotlin.math.max
import kotlin.math.min

class Day08(override val config: Year2025Config) : AocDay(config) {

    override fun part1(): String {
        val pairCount = requireNotNull(config.day08Part1PairCount)
        val boxes = input
            .map { line -> line.split(',').map { it.toInt() }.toPos3() }
            .toList()
        val sortedPairs = boxes
            .indices
            .flatMap { i1 ->
                boxes
                    .indices
                    .drop(i1 + 1)
                    .map { i2 -> Triple(i1, i2, boxes[i1].euclidTo(boxes[i2])) }
            }
            .sortedBy { it.third }
        val circuits = sortedPairs
            .asSequence()
            .take(pairCount)
            .fold(IntArray(boxes.size) { it }) { circuits, pair ->
                val (i1, i2) = pair
                val c1 = circuits[i1]
                val c2 = circuits[i2]
                when {
                    c1 == c2 -> if (config.verboseLog) println("\tboth $i1 and $i2 are already in circuit $c1")

                    else -> {
                        // merge
                        val minC = min(c1, c2)
                        val maxC = max(c1, c2)
                        if (config.verboseLog) println("\tmerging circuit $maxC to $minC")
                        circuits
                            .withIndex()
                            .filter { it.value == maxC }
                            .forEach { circuits[it.index] = minC }
                    }
                }
                if (config.verboseLog) println("circuits ${circuits.contentToString()}")
                circuits
            }
        val biggestCircuits = circuits
            .groupBy { it }
            .values
            .map { it.size }
            .sortedDescending()
        if (config.log) println("${biggestCircuits.size} circuits: $biggestCircuits")
        return biggestCircuits
            .take(3)
            .reduce(Int::times)
            .toString()
    }

    override fun part2(): String {
        // TODO
        return ""
    }
}
