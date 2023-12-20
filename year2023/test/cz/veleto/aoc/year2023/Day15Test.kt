package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day15Test {

    @Test
    fun testPart1() {
        val task = Day15(AocDay.Config("Day15_test"))
        assertEquals("1320", task.part1())
    }
}
