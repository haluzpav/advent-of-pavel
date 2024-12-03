package cz.veleto.aoc.year2024

import cz.veleto.aoc.core.AocDay

class Day03(config: Config) : AocDay(config) {

    private val mulRegex = Regex("""mul\(([0-9]{1,3}),([0-9]{1,3})\)""")
    private val enablingRegex = Regex("""(?:^|do\(\))(.*?)(?:don't\(\)|$)""")

    override fun part1(): String = input
        .map { it.findMultiplyAndSum() }
        .sum()
        .toString()

    override fun part2(): String = input
        .fold("") { acc, s -> acc + s.trim() }
        .let { line ->
            enablingRegex.findAll(line)
                .map { it.groupValues[1] }
                .map { it.findMultiplyAndSum() }
        }
        .sum()
        .toString()

    private fun String.findMultiplyAndSum(): Int = mulRegex.findAll(this)
        .map {
            val (a, b) = it.destructured
            a.toInt() * b.toInt()
        }
        .sum()
}
