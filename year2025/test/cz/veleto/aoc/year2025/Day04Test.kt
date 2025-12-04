package cz.veleto.aoc.year2025

import kotlin.test.Test
import kotlin.test.assertEquals

class Day04Test {

    @Test
    fun testPart1() {
        val task = Day04(Year2025Config("Day04_test"))
        assertEquals("13", task.part1())
    }
}
