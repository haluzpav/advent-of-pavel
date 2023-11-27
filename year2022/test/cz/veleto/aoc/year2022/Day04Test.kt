package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day04Test {
    private val task = Day04(AocDay.Config("Day04_test"))

    @Test
    fun testPart1() {
        assertEquals("2", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("4", task.part2())
    }
}
