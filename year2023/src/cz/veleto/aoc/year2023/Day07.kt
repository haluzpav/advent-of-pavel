package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import kotlin.math.pow

class Day07(config: Config) : AocDay(config) {

    @Suppress("SpellCheckingInspection")
    private val cards = "23456789TJQKA"

    @Suppress("SpellCheckingInspection")
    private val jokerCards = "J23456789TQKA"

    private val base = cards.length
    private val maxHandCardStrength = 1_000_000 // more like 300k but whatever, 1M is readable

    override fun part1(): String = solve(joker = false)

    override fun part2(): String = solve(joker = true)

    private fun solve(joker: Boolean): String {
        val sortedHands = input
            .parse()
            .map { (hand, bid) -> Triple(hand, hand.getHandTotalStrength(joker = joker), bid) }
            .onEach { (hand, strength, _) -> if (config.log) println("hand total strength of '$hand': $strength") }
            .toList()
            .sortedByDescending { (_, strength, _) -> strength }
        return sortedHands
            .mapIndexed { index, (hand, _, bid) -> Triple(hand, sortedHands.size - index, bid) }
            .sumOf { (_, rank, bid) -> bid * rank }
            .toString()
    }

    private fun Sequence<String>.parse(): Sequence<Pair<String, Int>> = map { line ->
        line.split(' ').let { (hand, bid) -> hand.also { check(it.length == 5) } to bid.toInt() }
    }

    private fun String.getHandType(joker: Boolean): Int {
        val allCardCounts = groupBy { it }.mapValues { (_, chars) -> chars.size }
        check(allCardCounts.map { (_, count) -> count }.sum() == 5)
        val jokers = if (joker) allCardCounts['J'] ?: 0 else 0
        val cardCounts = allCardCounts
            .filter { (char, _) -> char != 'J' || !joker }
            .values
            .sortedDescending()
        val firstCardWithJokers = cardCounts.getOrElse(0) { 0 } + jokers
        fun isPlainThreePair(): Boolean = cardCounts[0] == 3 && cardCounts[1] == 2
        fun isPlainTwoPair(): Boolean = cardCounts[0] == 2 && cardCounts[1] == 2
        return when {
            firstCardWithJokers == 5 -> 7
            firstCardWithJokers == 4 -> 6
            isPlainThreePair() || isPlainTwoPair() && jokers == 1 -> 5
            firstCardWithJokers == 3 -> 4
            isPlainTwoPair() -> 3
            firstCardWithJokers == 2 -> 2
            cardCounts.all { it == 1 } -> 1.also { check(jokers == 0) }
            else -> error("unknown hand type: '$this'")
        }
    }

    // converting to base 13
    private fun String.getHandCardStrength(cards: String): Int = reversed()
        .map(cards::indexOf)
        .mapIndexed { index, value -> value * base.toDouble().pow(index).toInt() }
        .sum()

    // first digit of output is type, the remaining 6 is strength of cards
    private fun String.getHandTotalStrength(joker: Boolean): Int =
        getHandType(joker) * maxHandCardStrength + getHandCardStrength(if (joker) jokerCards else cards)
}
