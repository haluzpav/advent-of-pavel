package cz.veleto.aoc.year2024

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day03Test {

    @Test
    fun testPart1() {
        val task = Day03(Year2024Config("Day03_test_1"))
        assertEquals("161", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day03(Year2024Config("Day03_test_2"))
        assertEquals("48", task.part2())
    }
}
