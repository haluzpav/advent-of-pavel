package cz.veleto.aoc.year2023

import kotlin.test.Test
import kotlin.test.assertEquals

class Day03Test {

    @Test
    fun testPart1() {
        val task = Day03(Year2023Config("Day03_test_1"))
        assertEquals("4361", task.part1())
    }

    @Test
    fun testPart2_1() {
        val task = Day03(Year2023Config("Day03_test_1"))
        assertEquals("467835", task.part2())
    }

    @Test
    fun testPart2_2() {
        val task = Day03(Year2023Config("Day03_test_2"))
        assertEquals("501320", task.part2())
    }
}
