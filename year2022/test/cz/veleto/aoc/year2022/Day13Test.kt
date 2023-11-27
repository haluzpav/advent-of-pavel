package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day13Test {
    @Test
    fun testPart1() {
        val task = Day13(AocDay.Config("Day13_test"))
        assertEquals("13", task.part1())
    }
    @Test
    fun testPart1_2() {
        val task = Day13(AocDay.Config("Day13_test_2"))
        assertEquals("0", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day13(AocDay.Config("Day13_test"))
        assertEquals("140", task.part2())
    }
}
