package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day17Test {

    @Test
    fun testPart1() {
        val task = Day17(AocDay.Config("Day17_test"))
        assertEquals("102", task.part1())
    }
}
