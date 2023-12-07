package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import kotlin.math.pow

class Day07(config: Config) : AocDay(config) {

    @Suppress("SpellCheckingInspection")
    private val cards = "AKQJT98765432"

    override fun part1(): String {
        val sortedHands = input
            .parse()
            .map { (hand, bid) -> Triple(hand, hand.getHandTotalStrength(), bid) }
            .onEach { (hand, strength, _) -> if (config.log) println("hand total strength of '$hand': $strength") }
            .toList()
            .sortedByDescending { (_, strength, _) -> strength }
        return sortedHands
            .mapIndexed { index, (hand, _, bid) -> Triple(hand, sortedHands.size - index, bid) }
            .sumOf { (_, rank, bid) -> bid * rank }
            .toString()
    }

    override fun part2(): String {
        // TODO
        return ""
    }

    private fun Sequence<String>.parse(): Sequence<Pair<String, Int>> = map { line ->
        line.split(' ').let { (hand, bid) -> hand to bid.toInt() }
    }

    private fun String.getHandType(): Int {
        check(length == 5)
        val cardCounts = groupBy { it }.values.map { it.size }.sortedDescending()
        check(cardCounts.sum() == 5)
        return when {
            cardCounts[0] == 5 -> 7
            cardCounts[0] == 4 -> 6
            cardCounts[0] == 3 && cardCounts[1] == 2 -> 5
            cardCounts[0] == 3 -> 4
            cardCounts[0] == 2 && cardCounts[1] == 2 -> 3
            cardCounts[0] == 2 -> 2
            cardCounts.all { it == 1 } -> 1
            else -> error("unknown hand type: '$this'")
        }
    }

    private fun String.getHandCardStrength(): Int {
        check(length == 5)
        val base = cards.length
        val cards = cards.reversed()
        return reversed()
            .map { card -> cards.indexOf(card) }
            .mapIndexed { index, value -> value * base.toDouble().pow(index).toInt() }
            .sum()
    }

    private fun String.getHandTotalStrength(): Int {
        check(length == 5)
        val maxHandCardStrength = 1_000_000 // more like 300k but whatever
        return getHandType() * maxHandCardStrength + getHandCardStrength()
    }
}
