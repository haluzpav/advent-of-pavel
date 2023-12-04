package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.popFirstOrElse
import cz.veleto.aoc.core.zipLongest
import kotlin.math.pow

class Day04(config: Config) : AocDay(config) {

    override fun part1(): String = input
        .parseAndMatch()
        .map { matchingCount ->
            when (matchingCount) {
                0 -> 0
                else -> 2.0.pow(matchingCount - 1).toInt()
            }.also { if (config.log) println("\tPoints $it") }
        }
        .sum()
        .toString()

    override fun part2(): String = input
        .parseAndMatch()
        .runningFold(emptyList<Int>()) { copies, matchingCount ->
            val (copyCountOfThisCard, remainingCopies) = copies.popFirstOrElse { 0 }
            val createdCopies = List(matchingCount) { 1 + copyCountOfThisCard }
            remainingCopies.zipLongest(createdCopies, defaultValue = { 0 }, transform = Int::plus)
                .also { if (config.log) println("\tHandling copies $copies, new copies $it") }
        }
        .drop(1) // init value of fold above
        .map { copiesBuffer -> copiesBuffer.getOrElse(0) { 0 } }
        .map { copies -> 1 + copies }
        .sum()
        .toString()

    private fun Sequence<String>.parseAndMatch(): Sequence<Int> = mapIndexed { index, line ->
        val (_, allNumbers) = line.split(':')
        val (winning, my) = allNumbers.split('|').map {
            it.split(' ').filter(String::isNotBlank).toSet()
        }
        winning.intersect(my).size
            .also { if (config.log) println("Card ${index + 1}; winning $winning; my $my; matching $it") }
    }
}
