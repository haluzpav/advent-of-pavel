package cz.veleto.aoc.year2025

import kotlin.test.Test
import kotlin.test.assertEquals

class Day06Test {

    @Test
    fun testPart1() {
        val task = Day06(Year2025Config("Day06_test"))
        assertEquals("4277556", task.part1())
    }
}
