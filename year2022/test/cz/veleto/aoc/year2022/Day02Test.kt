package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day02Test {
    private val task = Day02(AocDay.Config("Day02_test"))

    @Test
    fun testPart1() {
        assertEquals("15", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("12", task.part2())
    }
}
