package cz.veleto.aoc.year2024

import kotlin.test.Test
import kotlin.test.assertEquals

class Day01Test {

    @Test
    fun testPart1() {
        val task = Day01(Year2024Config("Day01_test"))
        assertEquals("11", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day01(Year2024Config("Day01_test"))
        assertEquals("31", task.part2())
    }
}
