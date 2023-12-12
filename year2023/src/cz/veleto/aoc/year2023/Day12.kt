package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import kotlin.math.pow

class Day12(config: Config) : AocDay(config) {

    override fun part1(): String = solve(folds = 1)

    override fun part2(): String = solve(folds = 5)

    private fun solve(folds: Int): String = input
        .map { line -> line.parse(folds) }
        .map { (row, regex) -> countArrangements(row, regex) }
        .sum()
        .toString()

    private fun String.parse(folds: Int): Pair<String, Regex> {
        val (foldedRow, foldedCounts) = split(' ')
        val row = List(folds) { foldedRow }.joinToString("?")
        val counts = foldedCounts.split(',')
        val regex = List(folds) { counts }
            .flatten()
            .joinToString(prefix = "^\\.*", separator = "\\.+", postfix = "\\.*$") { "#{$it}" }
            .toRegex()
        return row to regex
    }

    private fun countArrangements(row: String, regex: Regex): Int {
        val unknownCount = row.count { it == '?' }
        val maxArrs = 2.0.pow(unknownCount).toInt()
        if (config.log) println("row $row, regex ${regex.pattern}")
        return (0..<maxArrs)
            .asSequence()
            .map { arrInt ->
                (0..<unknownCount)
                    .map { shift -> arrInt shr shift and 1 == 1 }
                    .map { if (it) '#' else '.' }
                    .fold(row) { row, replacement -> row.replaceFirst('?', replacement) }
            }
            .map { arr -> arr to regex.matches(arr) }
            .onEach { (arr, matches) ->
                if (config.log && (matches || config.verboseLog)) println("\t$arr -> $matches")
            }
            .count { (_, matches) -> matches }
            .also { if (config.log) println("\tcount $it") }
    }
}
