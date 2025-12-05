package cz.veleto.aoc.year2025

import kotlin.test.Test
import kotlin.test.assertEquals

class Day05Test {

    @Test
    fun testPart1() {
        val task = Day05(Year2025Config("Day05_test"))
        assertEquals("3", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day05(Year2025Config("Day05_test"))
        assertEquals("14", task.part2())
    }
}
