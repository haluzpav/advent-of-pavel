package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day13Test {

    @Test
    fun testPart1() {
        val task = Day13(AocDay.Config("Day13_test"))
        assertEquals("405", task.part1())
    }
}
