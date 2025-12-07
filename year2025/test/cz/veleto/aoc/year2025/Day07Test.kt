package cz.veleto.aoc.year2025

import kotlin.test.Test
import kotlin.test.assertEquals

class Day07Test {

    @Test
    fun testPart1() {
        val task = Day07(Year2025Config("Day07_test"))
        assertEquals("21", task.part1())
    }
}
