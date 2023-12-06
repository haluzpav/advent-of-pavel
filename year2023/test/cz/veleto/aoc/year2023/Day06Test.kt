package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day06Test {

    @Test
    fun testPart1() {
        val task = Day06(AocDay.Config("Day06_test"))
        assertEquals("288", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day06(AocDay.Config("Day06_test"))
        assertEquals("71503", task.part2())
    }
}
