package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay

class Day01(config: Config) : AocDay(config) {

    private val spelledOutDigits: Map<String, Int> = listOf(
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine",
    ).mapIndexed { index, s -> s to index + 1 }.toMap()

    private val spelledOutDigitsRegex = spelledOutDigits.keys.joinToString("|")
    private val anyDigitRegex = """($spelledOutDigitsRegex|[1-9])"""
    private val firstDigitRegex = """.*?$anyDigitRegex.*""".toRegex()
    private val lastDigitRegex = """.*$anyDigitRegex.*?""".toRegex()

    override fun part1(): String = solve(
        findFirstDigit = { line -> line.first { it.isDigit() }.digitToInt() },
        findLastDigit = { line -> line.last { it.isDigit() }.digitToInt() },
    )

    override fun part2(): String = solve(
        findFirstDigit = { it.findAnyDigit(firstDigitRegex) },
        findLastDigit = { it.findAnyDigit(lastDigitRegex) },
    )

    private fun solve(
        findFirstDigit: (line: String) -> Int,
        findLastDigit: (line: String) -> Int,
    ): String = input
        .map { line ->
            val first = findFirstDigit(line)
            val last = findLastDigit(line)
            "$first$last".toInt()
        }
        .sum()
        .toString()

    private fun String.findAnyDigit(regex: Regex): Int =
        regex.matchEntire(this)!!.groupValues[1].let { it.toIntOrNull() ?: spelledOutDigits[it]!! }
}
