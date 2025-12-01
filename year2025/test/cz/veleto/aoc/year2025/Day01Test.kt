package cz.veleto.aoc.year2025

import kotlin.test.Test
import kotlin.test.assertEquals

class Day01Test {

    @Test
    fun testPart1() {
        val task = Day01(Year2025Config("Day01_test"))
        assertEquals("3", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day01(Year2025Config("Day01_test"))
        assertEquals("6", task.part2())
    }
}
