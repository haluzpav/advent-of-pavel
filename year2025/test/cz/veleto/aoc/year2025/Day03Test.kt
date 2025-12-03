package cz.veleto.aoc.year2025

import kotlin.test.Test
import kotlin.test.assertEquals

class Day03Test {

    @Test
    fun testPart1() {
        val task = Day03(Year2025Config("Day03_test"))
        assertEquals("357", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day03(Year2025Config("Day03_test"))
        assertEquals("3121910778619", task.part2())
    }
}
