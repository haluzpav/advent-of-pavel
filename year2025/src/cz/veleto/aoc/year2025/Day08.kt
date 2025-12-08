package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.Pos3
import cz.veleto.aoc.core.euclidTo
import cz.veleto.aoc.core.toPos3
import kotlin.math.max
import kotlin.math.min

class Day08(override val config: Year2025Config) : AocDay(config) {

    override fun part1(): String {
        val pairCount = requireNotNull(config.day08Part1PairCount)
        val boxes = input.parseBoxes()
        val sortedPairs = boxes.sortByDistance()
        val (circuits, _) = sortedPairs
            .mergeCircuits(boxes.size)
            .drop(pairCount)
            .first()
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
        val boxes = input.parseBoxes()
        val sortedPairs = boxes.sortByDistance()
        val (_, lastMergedPairIndex) = sortedPairs
            .mergeCircuits(boxes.size)
            .first { (circuits, _) -> circuits.toSet().size == 1 }
        val (b1, b2) = sortedPairs[lastMergedPairIndex]
        return (boxes[b1].first.toLong() * boxes[b2].first).toString()
    }

    private fun Sequence<String>.parseBoxes(): List<Pos3> = this
        .map { line -> line.split(',').map { it.toInt() }.toPos3() }
        .toList()

    private fun List<Pos3>.sortByDistance(): List<Triple<Int, Int, Double>> = indices
        .flatMap { i1 ->
            indices
                .drop(i1 + 1)
                .map { i2 -> Triple(i1, i2, this[i1].euclidTo(this[i2])) }
        }
        .sortedBy { it.third }

    private fun List<Triple<Int, Int, Double>>.mergeCircuits(boxCount: Int): Sequence<Pair<IntArray, Int>> = this
        .asSequence()
        .runningFoldIndexed(IntArray(boxCount) { it } to -1) { index, (circuits, _), pair ->
            val (i1, i2) = pair
            val c1 = circuits[i1]
            val c2 = circuits[i2]
            when {
                c1 == c2 -> if (config.verboseLog) println("\t\tboth $i1 and $i2 are already in circuit $c1")

                else -> {
                    val minC = min(c1, c2)
                    val maxC = max(c1, c2)
                    if (config.verboseLog) println("\t\tmerging circuit $maxC to $minC")
                    circuits
                        .withIndex()
                        .filter { it.value == maxC }
                        .forEach { circuits[it.index] = minC }
                }
            }
            if (config.verboseLog) println("\t[$index] circuits ${circuits.contentToString()}")
            circuits to index
        }
}
