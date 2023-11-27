package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.loadInput

class Day06(inputName: String) {
    private val input: List<String> = loadInput(inputName)

    fun part1(): List<Int> = findRepetitions(4)

    fun part2(): List<Int> = findRepetitions(14)

    private fun findRepetitions(count: Int): List<Int> = input.map { s ->
        (0..s.length - count)
            .first { s.substring(it until it + count).toSet().size == count }
            .let { it + count }
    }
}
