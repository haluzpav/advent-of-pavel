package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay

class Day02(override val config: Year2022Config) : AocDay(config) {

    override fun part1(): String = input.sumOf { calcScore1(it[0], it[2]) }.toString()

    override fun part2(): String = input.sumOf { calcScore2(it[0], it[2]) }.toString()

    private fun calcScore1(o: Char, m: Char): Int {
        val oCode = o.code - 64
        val mCode = m.code - 87
        val rScore = arrayOf(3, 6, 0)[(mCode - oCode + 3).rem(3)]
        return mCode + rScore
    }

    private fun calcScore2(o: Char, r: Char): Int {
        val oCode = o.code - 64
        val rCode = r.code - 87
        val table = arrayOf(
            arrayOf(3, 1, 2),
            arrayOf(1, 2, 3),
            arrayOf(2, 3, 1),
        )
        val mCode = table[rCode - 1][oCode - 1]
        return mCode + (rCode - 1) * 3
    }
}
