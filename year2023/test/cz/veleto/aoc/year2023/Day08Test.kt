package cz.veleto.aoc.year2023

import kotlin.test.Test
import kotlin.test.assertEquals

class Day08Test {

    @Test
    fun testPart1() {
        val task = Day08(Year2023Config("Day08_test_1"))
        assertEquals("2", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day08(Year2023Config("Day08_test_2"))
        assertEquals("6", task.part2())
    }
}
