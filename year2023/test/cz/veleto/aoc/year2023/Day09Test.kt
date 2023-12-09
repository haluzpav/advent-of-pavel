package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day09Test {

    @Test
    fun testPart1() {
        val task = Day09(AocDay.Config("Day09_test"))
        assertEquals("114", task.part1())
    }
}
