package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day04Test {

    @Test
    fun testPart1() {
        val task = Day04(AocDay.Config("Day04_test"))
        assertEquals("13", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day04(AocDay.Config("Day04_test"))
        assertEquals("30", task.part2())
    }
}
