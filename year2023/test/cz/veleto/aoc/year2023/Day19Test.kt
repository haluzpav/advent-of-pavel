package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day19Test {

    @Test
    fun testPart1() {
        val task = Day19(AocDay.Config("Day19_test"))
        assertEquals("19114", task.part1())
    }
}
