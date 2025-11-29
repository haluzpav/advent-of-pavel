package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.splitBy

class Day01(override val config: Year2022Config) : AocDay(config) {

    override fun part1(): String = input
        .toList()
        .splitBy(predicate = { it == "" }, mapElement = { it.toInt() })
        .maxOf { it.sum() }
        .toString()

    override fun part2(): String = input
        .toList()
        .splitBy(predicate = { it == "" }, mapElement = { it.toInt() })
        .map { it.sum() }
        .sorted()
        .takeLast(3)
        .sum()
        .toString()
}
