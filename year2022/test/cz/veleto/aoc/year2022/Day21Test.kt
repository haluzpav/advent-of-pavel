package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day21Test {
    private val task = Day21(AocDay.Config("Day21_test"))

    @Test
    fun testPart1() {
        assertEquals("152", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("301", task.part2())
    }
}
