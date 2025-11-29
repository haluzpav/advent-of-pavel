package cz.veleto.aoc.year2023

import kotlin.test.Test
import kotlin.test.assertEquals

class Day05Test {

    @Test
    fun testPart1() {
        val task = Day05(Year2023Config("Day05_test"))
        assertEquals("35", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day05(Year2023Config("Day05_test"))
        assertEquals("46", task.part2())
    }
}
