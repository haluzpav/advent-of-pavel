package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day11Test {
    private val task = Day11(AocDay.Config("Day11_test"))

    @Test
    fun testPart1() {
        assertEquals("10605", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("2713310158", task.part2())
    }
}
