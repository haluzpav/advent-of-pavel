package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.permute

class Day12(override val config: Year2023Config) : AocDay(config) {

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
        if (config.log) println("row $row, regex ${regex.pattern}")
        return permute(choiceA = '#', choiceB = '.', size = row.count { it == '?' })
            .map { permutation ->
                permutation.fold(row) { row, replacement -> row.replaceFirst('?', replacement) }
            }
            .map { arr -> arr to regex.matches(arr) }
            .onEach { (arr, matches) ->
                if (config.log && (matches || config.verboseLog)) println("\t$arr -> $matches")
            }
            .count { (_, matches) -> matches }
            .also { if (config.log) println("\tcount $it") }
    }
}
