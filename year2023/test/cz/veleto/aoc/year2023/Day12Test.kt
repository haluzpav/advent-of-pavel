package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {

    @Test
    fun testPart1_1() {
        val task = Day12(AocDay.Config("Day12_test_1"))
        assertEquals("21", task.part1())
    }

    @Test
    fun testPart1_2() {
        val task = Day12(AocDay.Config("Day12_test_2", verboseLog = false))
        assertEquals("86", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day12(AocDay.Config("Day12_test_1", verboseLog = false))
        assertEquals("525152", task.part2())
    }
}
