package cz.veleto.aoc.year2023

import kotlin.test.Test
import kotlin.test.assertEquals

class Day16Test {

    @Test
    fun testPart1() {
        val task = Day16(Year2023Config("Day16_test"))
        assertEquals("46", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day16(Year2023Config("Day16_test"))
        assertEquals("51", task.part2())
    }
}
