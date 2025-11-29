package cz.veleto.aoc.year2023

import kotlin.test.Test
import kotlin.test.assertEquals

class Day09Test {

    @Test
    fun testPart1() {
        val task = Day09(Year2023Config("Day09_test"))
        assertEquals("114", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day09(Year2023Config("Day09_test"))
        assertEquals("2", task.part2())
    }
}
