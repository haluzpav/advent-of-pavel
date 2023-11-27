package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day16Test {
    private val task = Day16(AocDay.Config("Day16_test"))

    @Test
    fun testPart1() {
        assertEquals("1651", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("1707", task.part2())
    }
}
