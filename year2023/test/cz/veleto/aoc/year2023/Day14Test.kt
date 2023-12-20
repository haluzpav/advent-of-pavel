package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day14Test {

    @Test
    fun testPart1() {
        val task = Day14(AocDay.Config("Day14_test"))
        assertEquals("136", task.part1())
    }
}
