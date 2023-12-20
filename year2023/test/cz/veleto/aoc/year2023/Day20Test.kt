package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day20Test {

    @Test
    fun testPart1() {
        val task = Day20(AocDay.Config("Day20_test"))
        assertEquals("1000", task.part1())
    }
}
