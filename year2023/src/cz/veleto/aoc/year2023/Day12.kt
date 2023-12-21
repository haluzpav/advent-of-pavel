package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay

class Day12(config: Config) : AocDay(config) {

    override fun part1(): String = solve(folds = 1)

    override fun part2(): String = solve(folds = 5)

    private fun solve(folds: Int): String = input
        .map { line -> line.parse(folds) }
        .map { (row, counts) -> countArrangements(row, counts) }
        .sum()
        .toString()

    private fun String.parse(folds: Int): Pair<String, List<Int>> {
        val (foldedRow, foldedCounts) = split(' ')
        val row = List(folds) { foldedRow }.joinToString("?")
        val counts = foldedCounts.split(',').map { it.toInt() }
        val unfoldedCounts = List(folds) { counts }.flatten()
        return row to unfoldedCounts
    }

    private fun countArrangements(row: String, counts: List<Int>): Int {
        if (config.log) println("row $row, counts $counts")


        val unknowns = row.withIndex().filter { it.value == '?' }.map { it.index }
        val damaged = row.withIndex().filter { it.value == '#' }.map { it.index }

        counts[0]




        generateSequence { }
            .runningFold(List(counts.size) { 0 } as List<Int>?) { previousPositions, _ ->
                // create single arr
                generateSequence { }
                    .runningFoldIndexed(row as String?) { index, row, _ ->

                        null
                    }
                    .takeWhile { it != null }
                null
            }
            .takeWhile { it != null }
            .count()
        return 0
    }
}
