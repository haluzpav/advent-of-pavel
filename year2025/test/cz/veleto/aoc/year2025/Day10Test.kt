package cz.veleto.aoc.year2025

import kotlin.test.Test
import kotlin.test.assertEquals

class Day10Test {

    @Test
    fun testPart1() {
        val task = Day10(Year2025Config("Day10_test"))
        assertEquals("7", task.part1())
    }
}
