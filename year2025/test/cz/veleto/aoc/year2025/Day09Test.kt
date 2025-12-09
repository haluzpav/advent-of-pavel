package cz.veleto.aoc.year2025

import kotlin.test.Test
import kotlin.test.assertEquals

class Day09Test {

    @Test
    fun testPart1() {
        val task = Day09(Year2025Config("Day09_test"))
        assertEquals("50", task.part1())
    }
}
