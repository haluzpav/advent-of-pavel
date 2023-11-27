package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day22Test {
    private val task = Day22(AocDay.Config("Day22_test"))

    @Test
    fun testPart1() {
        assertEquals("6032", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("5031", task.part2())
    }
}
