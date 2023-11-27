package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day18Test {
    private val task = Day18(AocDay.Config("Day18_test"))

    @Test
    fun testPart1() {
        assertEquals("64", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("58", task.part2())
    }
}
