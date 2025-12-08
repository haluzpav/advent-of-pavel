package cz.veleto.aoc.year2025

import kotlin.test.Test
import kotlin.test.assertEquals

class Day08Test {

    @Test
    fun testPart1() {
        val task = Day08(Year2025Config("Day08_test", day08Part1PairCount = 10))
        assertEquals("40", task.part1())
    }
}
