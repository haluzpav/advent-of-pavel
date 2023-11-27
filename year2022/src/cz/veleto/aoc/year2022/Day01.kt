package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.loadInput
import cz.veleto.aoc.core.splitBy

class Day01(inputName: String) {
    private val input: List<String> = loadInput(inputName)

    fun part1(): Int = input
        .splitBy(predicate = { it == "" }, mapElement = { it.toInt() })
        .maxOf { it.sum() }

    fun part2(): Int = input
        .splitBy(predicate = { it == "" }, mapElement = { it.toInt() })
        .map { it.sum() }
        .sorted()
        .takeLast(3)
        .sum()
}
