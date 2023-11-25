package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.readInput
import kotlin.math.abs

class Day25(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    private val snafus = listOf(-2, -1, 0, 1, 2)
    private val maxSnafuOrder = 27 // so 2*5^maxSnafuOrder < Long.MAX_VALUE
    private val snafuPowers: List<Long> = (0..maxSnafuOrder).map { 5.pow(it) }
    private val snafuPowerSums: List<Long> = snafuPowers
        .runningFold(0L) { acc, power -> acc + power * 2 }
        .drop(1)

    fun part1(): String = input
        .map(::snafuToDec)
        .sum()
        .let(::decToSnafu)

    private fun snafuToDec(snafu: String): Long = snafu.toList().asReversed()
        .map { c ->
            when (c) {
                '=' -> -2
                '-' -> -1
                else -> c.digitToInt().also { check(it in 0..2) }
            }
        }
        .mapIndexed { index, snafuDigit -> snafuPowers[index] * snafuDigit }
        .sum()

    private fun decToSnafu(dec: Long): String = buildString {
        val maxOrder = snafuPowerSums.indexOfFirst { dec in -it..it }
        var remainingDec = dec
        for (order in maxOrder downTo 0) {
            val (snafuDigit, newRemainingDec) = snafus
                .map { it to remainingDec - snafuPowers[order] * it }
                .minBy { (_, newRemainingDec) -> abs(newRemainingDec) }
            val snafuChar = when (snafuDigit) {
                -2 -> '='
                -1 -> '-'
                else -> snafuDigit.digitToChar()
            }
            append(snafuChar)
            remainingDec = newRemainingDec
        }
    }

    private fun Int.pow(exp: Int): Long = (0 until exp)
        .also { check(exp in 0..maxSnafuOrder) }
        .fold(1L) { acc, _ -> acc * this }
}

fun main() {
    val task = Day25("Day25")
    println(task.part1())
}
