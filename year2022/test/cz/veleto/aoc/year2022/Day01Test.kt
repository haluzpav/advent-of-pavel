package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day01Test {
    private val task = Day01(AocDay.Config("Day01_test"))

    @Test
    fun testPart1() {
        assertEquals("24000", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("45000", task.part2())
    }
}
