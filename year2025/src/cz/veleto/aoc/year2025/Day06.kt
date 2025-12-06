package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay

class Day06(override val config: Year2025Config) : AocDay(config) {

    override fun part1(): String {
        val operators = input
            .dropWhile { it.isNumberLine() }
            .first()
            .split(' ')
            .filter { it.isNotBlank() }
            .map { it.single() }
        return input
            .takeWhile { it.isNumberLine() }
            .map { line ->
                line.split(' ').filter { it.isNotBlank() }.map { it.toLong() }
            }
            .reduce { result, numbers ->
                result.indices.map { index ->
                    when (val operator = operators[index]) {
                        '+' -> result[index] + numbers[index]
                        '*' -> result[index] * numbers[index]
                        else -> error("Unknown operator $operator")
                    }
                }
            }
            .sum()
            .toString()
    }

    override fun part2(): String {
        // TODO
        return ""
    }

    private fun String.isNumberLine(): Boolean = this[0].let { firstChar ->
        firstChar in '0'..'9' || firstChar == ' '
    }
}
