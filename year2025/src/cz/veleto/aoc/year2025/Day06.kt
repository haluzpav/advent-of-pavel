package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.splitBy
import cz.veleto.aoc.core.transpose

class Day06(override val config: Year2025Config) : AocDay(config) {

    override fun part1(): String {
        val operations = input
            .dropWhile { it.isNumberLine() }
            .first()
            .split(' ')
            .filter { it.isNotBlank() }
            .map { it.single().toOperation() }
        return input
            .takeWhile { it.isNumberLine() }
            .map { line ->
                line.split(' ').filter { it.isNotBlank() }.map { it.toLong() }
            }
            .reduce { result, numbers ->
                check(result.size == numbers.size)
                check(result.size == operations.size)
                result.indices.map { index ->
                    operations[index](result[index], numbers[index])
                }
            }
            .sum()
            .toString()
    }

    override fun part2(): String = cachedInput
        .transpose()
        .splitBy { it.isBlank() }
        .sumOf { problem ->
            problem
                .map { line ->
                    line.filter { it in '0'..'9' }.toLong()
                }
                .reduce(problem.first().last().toOperation())
        }
        .toString()

    private fun String.isNumberLine(): Boolean = this[0].let { firstChar ->
        firstChar in '0'..'9' || firstChar == ' '
    }

    private fun Char.toOperation(): (Long, Long) -> Long = when (this) {
        '+' -> Long::plus
        '*' -> Long::times
        else -> error("Unknown operator $this")
    }
}
