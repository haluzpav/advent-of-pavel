package cz.veleto.aoc.year2024

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day02Test {

    @Test
    fun testPart1() {
        val task = Day02(AocDay.Config("Day02_test"))
        assertEquals("2", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day02(AocDay.Config("Day02_test"))
        assertEquals("4", task.part2())
    }
}
