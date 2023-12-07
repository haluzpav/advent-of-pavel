package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day07Test {

    @Test
    fun testPart1() {
        val task = Day07(AocDay.Config("Day07_test"))
        assertEquals("6440", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day07(AocDay.Config("Day07_test"))
        assertEquals("5905", task.part2())
    }
}
