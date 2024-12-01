package cz.veleto.aoc.year2024

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day01Test {

    @Test
    fun testPart1() {
        val task = Day01(AocDay.Config("Day01_test"))
        assertEquals("11", task.part1())
    }
}
